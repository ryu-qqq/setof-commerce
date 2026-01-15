# ========================================
# ECS Service: legacy-batch
# ========================================
# Spring Batch Jobs for Legacy System
# - Sellic 외부몰 주문 동기화
# - 배송 조회 (SweetTracker API)
# - 알림톡 발송
# - 주문 상태 업데이트
# Port: 8090 (Internal Only)
# ========================================

# ========================================
# Common Tags
# ========================================
locals {
  common_tags = {
    environment  = var.environment
    service_name = "${var.project_name}-legacy-batch"
    team         = "platform-team"
    owner        = "platform@ryuqqq.com"
    cost_center  = "engineering"
    project      = var.project_name
    data_class   = "internal"
  }

  # IAM 리소스용 태그 (environment/project 제외)
  # Provider default_tags에 Environment/Project가 있고
  # IAM 태그는 대소문자 구분 안 함 → 중복 방지
  iam_tags = {
    service_name = "${var.project_name}-legacy-batch"
    team         = "platform-team"
    owner        = "platform@ryuqqq.com"
    cost_center  = "engineering"
    data_class   = "internal"
  }
}

# ========================================
# ECR Repository Reference
# ========================================
data "aws_ecr_repository" "legacy_batch" {
  name = "${var.project_name}-legacy-batch-${var.environment}"
}

# ========================================
# ECS Cluster Reference
# ========================================
data "aws_ecs_cluster" "main" {
  cluster_name = "${var.project_name}-cluster-${var.environment}"
}

data "aws_caller_identity" "current" {}

# VPC data source
data "aws_vpc" "main" {
  id = local.vpc_id
}

# ========================================
# KMS Key for CloudWatch Logs Encryption
# ========================================
resource "aws_kms_key" "logs" {
  description             = "KMS key for SetOf Commerce legacy-batch CloudWatch logs encryption"
  deletion_window_in_days = 30
  enable_key_rotation     = true

  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Sid    = "Enable IAM User Permissions"
        Effect = "Allow"
        Principal = {
          AWS = "arn:aws:iam::${data.aws_caller_identity.current.account_id}:root"
        }
        Action   = "kms:*"
        Resource = "*"
      },
      {
        Sid    = "Allow CloudWatch Logs"
        Effect = "Allow"
        Principal = {
          Service = "logs.${var.aws_region}.amazonaws.com"
        }
        Action = [
          "kms:Encrypt*",
          "kms:Decrypt*",
          "kms:ReEncrypt*",
          "kms:GenerateDataKey*",
          "kms:Describe*"
        ]
        Resource = "*"
        Condition = {
          ArnLike = {
            "kms:EncryptionContext:aws:logs:arn" = "arn:aws:logs:${var.aws_region}:${data.aws_caller_identity.current.account_id}:log-group:/aws/ecs/${var.project_name}-legacy-batch-${var.environment}/*"
          }
        }
      }
    ]
  })

  tags = merge(local.common_tags, {
    Name      = "${var.project_name}-legacy-batch-logs-kms-${var.environment}"
    Lifecycle = "production"
    ManagedBy = "terraform"
  })
}

resource "aws_kms_alias" "logs" {
  name          = "alias/${var.project_name}-legacy-batch-logs-${var.environment}"
  target_key_id = aws_kms_key.logs.key_id
}

# ========================================
# CloudWatch Log Group
# ========================================
module "legacy_batch_logs" {
  source = "git::https://github.com/ryu-qqq/Infrastructure.git//terraform/modules/cloudwatch-log-group?ref=main"

  name              = "/aws/ecs/${var.project_name}-legacy-batch-${var.environment}/application"
  retention_in_days = 30
  kms_key_id        = aws_kms_key.logs.arn

  environment  = local.common_tags.environment
  service_name = local.common_tags.service_name
  team         = local.common_tags.team
  owner        = local.common_tags.owner
  cost_center  = local.common_tags.cost_center
  project      = local.common_tags.project
  data_class   = local.common_tags.data_class
}

# ========================================
# Security Group (No ALB - Internal Only)
# ========================================
module "ecs_security_group" {
  source = "git::https://github.com/ryu-qqq/Infrastructure.git//terraform/modules/security-group?ref=main"

  name        = "${var.project_name}-legacy-batch-sg-${var.environment}"
  description = "Security group for legacy-batch ECS tasks (internal only)"
  vpc_id      = local.vpc_id

  type = "custom"

  custom_ingress_rules = [
    {
      from_port   = 8090
      to_port     = 8090
      protocol    = "tcp"
      cidr_block  = data.aws_vpc.main.cidr_block
      description = "Internal VPC communication only"
    }
  ]

  environment  = local.common_tags.environment
  service_name = local.common_tags.service_name
  team         = local.common_tags.team
  owner        = local.common_tags.owner
  cost_center  = local.common_tags.cost_center
  project      = local.common_tags.project
  data_class   = local.common_tags.data_class
}

# ========================================
# IAM Roles
# ========================================

# ECS Task Execution Role
module "legacy_batch_task_execution_role" {
  source = "git::https://github.com/ryu-qqq/Infrastructure.git//terraform/modules/iam-role-policy?ref=main"

  role_name = "${var.project_name}-legacy-batch-execution-role-${var.environment}"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = "sts:AssumeRole"
        Effect = "Allow"
        Principal = {
          Service = "ecs-tasks.amazonaws.com"
        }
      }
    ]
  })

  attach_aws_managed_policies = [
    "arn:aws:iam::aws:policy/service-role/AmazonECSTaskExecutionRolePolicy"
  ]

  enable_secrets_manager_policy = true
  secrets_manager_secret_arns = [
    data.aws_secretsmanager_secret.rds.arn,
    data.aws_secretsmanager_secret.legacy.arn
  ]

  custom_inline_policies = {
    ssm-access = {
      policy = jsonencode({
        Version = "2012-10-17"
        Statement = [
          {
            Effect = "Allow"
            Action = [
              "ssm:GetParameters",
              "ssm:GetParameter"
            ]
            Resource = [
              "arn:aws:ssm:${var.aws_region}:*:parameter/shared/*",
              "arn:aws:ssm:${var.aws_region}:*:parameter/${var.project_name}/*"
            ]
          },
          {
            Effect = "Allow"
            Action = [
              "kms:Decrypt"
            ]
            Resource = [
              aws_kms_key.logs.arn
            ]
          }
        ]
      })
    }
  }

  environment  = var.environment
  service_name = local.common_tags.service_name
  team         = local.common_tags.team
  owner        = local.common_tags.owner
  cost_center  = local.common_tags.cost_center
  project      = var.project_name
  data_class   = local.common_tags.data_class
}

# ECS Task Role
module "legacy_batch_task_role" {
  source = "git::https://github.com/ryu-qqq/Infrastructure.git//terraform/modules/iam-role-policy?ref=main"

  role_name = "${var.project_name}-legacy-batch-task-role-${var.environment}"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = "sts:AssumeRole"
        Effect = "Allow"
        Principal = {
          Service = "ecs-tasks.amazonaws.com"
        }
      }
    ]
  })

  custom_inline_policies = {
    legacy-batch-access = {
      policy = jsonencode({
        Version = "2012-10-17"
        Statement = [
          {
            Sid    = "AMPRemoteWrite"
            Effect = "Allow"
            Action = [
              "aps:RemoteWrite"
            ]
            Resource = "arn:aws:aps:${var.aws_region}:*:workspace/*"
          },
          {
            Sid    = "XRayTracing"
            Effect = "Allow"
            Action = [
              "xray:PutTraceSegments",
              "xray:PutTelemetryRecords",
              "xray:GetSamplingRules",
              "xray:GetSamplingTargets",
              "xray:GetSamplingStatisticSummaries"
            ]
            Resource = "*"
          },
          {
            Sid    = "CloudWatchLogsAccess"
            Effect = "Allow"
            Action = [
              "logs:CreateLogGroup",
              "logs:CreateLogStream",
              "logs:PutLogEvents",
              "logs:DescribeLogStreams"
            ]
            Resource = [
              "arn:aws:logs:${var.aws_region}:${data.aws_caller_identity.current.account_id}:log-group:/ecs/${var.project_name}/otel:*",
              "arn:aws:logs:${var.aws_region}:${data.aws_caller_identity.current.account_id}:log-group:/ecs/${var.project_name}/otel-collector:*"
            ]
          },
          {
            Sid    = "CloudWatchMetricsAccess"
            Effect = "Allow"
            Action = [
              "cloudwatch:PutMetricData"
            ]
            Resource = "*"
            Condition = {
              StringEquals = {
                "cloudwatch:namespace" = "SetOfCommerce"
              }
            }
          }
        ]
      })
    }
  }

  environment  = var.environment
  service_name = local.common_tags.service_name
  team         = local.common_tags.team
  owner        = local.common_tags.owner
  cost_center  = local.common_tags.cost_center
  project      = var.project_name
  data_class   = local.common_tags.data_class
}

# ========================================
# ADOT Sidecar (Observability)
# ========================================
module "adot_sidecar" {
  source = "git::https://github.com/ryu-qqq/Infrastructure.git//terraform/modules/adot-sidecar?ref=main"

  project_name              = var.project_name
  service_name              = "legacy-batch"
  aws_region                = var.aws_region
  amp_workspace_arn         = local.amp_workspace_arn
  amp_remote_write_endpoint = local.amp_remote_write_url
  log_group_name            = module.legacy_batch_logs.log_group_name
  app_port                  = 8090
  cluster_name              = data.aws_ecs_cluster.main.cluster_name
  environment               = var.environment
  config_bucket             = "prod-connectly"
  config_version            = "20251215"
}

# ========================================
# ECS Task Definition (RunTask 전용 - Service 없음)
# ========================================
# EventBridge가 스케줄에 따라 RunTask로 실행
# 상시 실행 Service 없음 = 비용 효율적
# ========================================

resource "aws_ecs_task_definition" "legacy_batch" {
  family                   = "${var.project_name}-legacy-batch-${var.environment}"
  network_mode             = "awsvpc"
  requires_compatibilities = ["FARGATE"]
  cpu                      = var.batch_cpu
  memory                   = var.batch_memory
  execution_role_arn       = module.legacy_batch_task_execution_role.role_arn
  task_role_arn            = module.legacy_batch_task_role.role_arn

  container_definitions = jsonencode([
    {
      name      = "legacy-batch"
      image     = "${data.aws_ecr_repository.legacy_batch.repository_url}:${var.image_tag}"
      essential = true

      portMappings = [
        {
          containerPort = 8090
          protocol      = "tcp"
        }
      ]

      environment = [
        { name = "SPRING_PROFILES_ACTIVE", value = var.environment },
        # Primary Database (SetOf - Spring Batch metadata)
        { name = "DB_URL", value = "jdbc:mysql://${local.rds_host}:${local.rds_port}/${local.rds_dbname}?useSSL=false&serverTimezone=Asia/Seoul" },
        { name = "DB_HOST", value = local.rds_host },
        { name = "DB_PORT", value = local.rds_port },
        { name = "DB_NAME", value = local.rds_dbname },
        # Legacy DB 설정
        { name = "LEGACY_DB_URL", value = "jdbc:mysql://${local.rds_host}:${local.rds_port}/${local.rds_dbname}?useSSL=false&serverTimezone=Asia/Seoul" },
        # Redis
        { name = "REDIS_HOST", value = local.redis_host },
        { name = "REDIS_PORT", value = tostring(local.redis_port) },
        { name = "REDIS_DATABASE", value = "0" },
        # Server Port
        { name = "SERVER_PORT", value = "8090" },
        # Spring Admin API (Service Discovery DNS - ECS 내부 통신)
        # legacy-admin-prod는 bootstrap-legacy-web-api-admin 모듈 (port 8089)
        { name = "ADMIN_SERVER_URL", value = "http://setof-commerce-legacy-admin-prod.connectly.local:8089" }
        # JOB_NAME은 EventBridge containerOverrides로 주입
      ]

      secrets = [
        { name = "DB_USERNAME", valueFrom = "${data.aws_secretsmanager_secret.rds.arn}:username::" },
        { name = "DB_PASSWORD", valueFrom = "${data.aws_secretsmanager_secret.rds.arn}:password::" },
        { name = "LEGACY_DB_USERNAME", valueFrom = "${data.aws_secretsmanager_secret.rds.arn}:username::" },
        { name = "LEGACY_DB_PASSWORD", valueFrom = "${data.aws_secretsmanager_secret.rds.arn}:password::" },
        { name = "SELLIC_API_KEY", valueFrom = "${data.aws_secretsmanager_secret.legacy.arn}:sellic_api_key::" },
        { name = "SHIPMENT_TRACKER_API_KEY", valueFrom = "${data.aws_secretsmanager_secret.legacy.arn}:shipment_tracker_api_key::" },
        { name = "NHN_ALIMTALK_APP_KEY", valueFrom = "${data.aws_secretsmanager_secret.legacy.arn}:nhn_alimtalk_app_key::" },
        { name = "NHN_ALIMTALK_SECRET_KEY", valueFrom = "${data.aws_secretsmanager_secret.legacy.arn}:nhn_alimtalk_secret_key::" },
        { name = "NHN_ALIMTALK_SENDER_KEY", valueFrom = "${data.aws_secretsmanager_secret.legacy.arn}:nhn_alimtalk_sender_key::" }
      ]

      logConfiguration = {
        logDriver = "awslogs"
        options = {
          "awslogs-group"         = module.legacy_batch_logs.log_group_name
          "awslogs-region"        = var.aws_region
          "awslogs-stream-prefix" = "legacy-batch"
        }
      }

      # Batch는 Health Check 필요 없음 (RunTask 후 종료)
    },
    # ADOT Sidecar for Observability
    module.adot_sidecar.container_definition
  ])

  tags = merge(local.common_tags, {
    Name = "${var.project_name}-legacy-batch-task-${var.environment}"
  })
}

# ========================================
# EventBridge Schedules for Batch Jobs
# ========================================

# IAM Role for EventBridge to invoke ECS
resource "aws_iam_role" "eventbridge_batch" {
  name = "${var.project_name}-eventbridge-batch-role-${var.environment}"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = "sts:AssumeRole"
        Effect = "Allow"
        Principal = {
          Service = "scheduler.amazonaws.com"
        }
      }
    ]
  })

  tags = local.iam_tags
}

resource "aws_iam_role_policy" "eventbridge_batch" {
  name = "eventbridge-batch-policy"
  role = aws_iam_role.eventbridge_batch.id

  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Effect = "Allow"
        Action = [
          "ecs:RunTask"
        ]
        Resource = [
          # Task Definition ARN (버전 포함/미포함 모두 허용)
          aws_ecs_task_definition.legacy_batch.arn,
          "${aws_ecs_task_definition.legacy_batch.arn_without_revision}:*"
        ]
      },
      {
        Effect = "Allow"
        Action = [
          "iam:PassRole"
        ]
        Resource = [
          module.legacy_batch_task_execution_role.role_arn,
          module.legacy_batch_task_role.role_arn
        ]
      }
    ]
  })
}

# ========================================
# Schedule: 배송 조회 (매 30분)
# Job: trackingShipmentJob
# ========================================
resource "aws_scheduler_schedule" "shipment_tracking" {
  count = var.enable_eventbridge_schedules ? 1 : 0

  name       = "${var.project_name}-shipment-tracking-${var.environment}"
  group_name = "default"

  flexible_time_window {
    mode = "OFF"
  }

  schedule_expression = "rate(30 minutes)"

  target {
    arn      = data.aws_ecs_cluster.main.arn
    role_arn = aws_iam_role.eventbridge_batch.arn

    ecs_parameters {
      task_definition_arn = aws_ecs_task_definition.legacy_batch.arn
      launch_type         = "FARGATE"
      task_count          = 1

      network_configuration {
        subnets          = local.private_subnets
        security_groups  = [module.ecs_security_group.security_group_id]
        assign_public_ip = false
      }
    }

    input = jsonencode({
      containerOverrides = [
        {
          name = "legacy-batch"
          environment = [
            { name = "JOB_NAME", value = "trackingShipmentJob" }
          ]
        }
      ]
    })
  }

  state = "ENABLED"
}

# ========================================
# Schedule: Sellic 주문 동기화
# Job: syncSellicOrderJob
# 스케줄: 07, 10, 13, 16, 19, 22시 05분 (KST)
# ========================================
resource "aws_scheduler_schedule" "sellic_sync" {
  count = var.enable_eventbridge_schedules ? 1 : 0

  name       = "${var.project_name}-sellic-sync-${var.environment}"
  group_name = "default"

  flexible_time_window {
    mode = "OFF"
  }

  # KST 07,10,13,16,19,22시 05분 = UTC 22,01,04,07,10,13시 05분 (KST = UTC+9)
  schedule_expression          = "cron(5 22,1,4,7,10,13 * * ? *)"
  schedule_expression_timezone = "Asia/Seoul"

  target {
    arn      = data.aws_ecs_cluster.main.arn
    role_arn = aws_iam_role.eventbridge_batch.arn

    ecs_parameters {
      task_definition_arn = aws_ecs_task_definition.legacy_batch.arn
      launch_type         = "FARGATE"
      task_count          = 1

      network_configuration {
        subnets          = local.private_subnets
        security_groups  = [module.ecs_security_group.security_group_id]
        assign_public_ip = false
      }
    }

    input = jsonencode({
      containerOverrides = [
        {
          name = "legacy-batch"
          environment = [
            { name = "JOB_NAME", value = "syncSellicOrderJob" }
          ]
        }
      ]
    })
  }

  state = "ENABLED"
}

# ========================================
# Schedule: 알림톡 발송 (매 5분)
# Job: alimTalkNotifyJob
# ========================================
resource "aws_scheduler_schedule" "alimtalk_notify" {
  count = var.enable_eventbridge_schedules ? 1 : 0

  name       = "${var.project_name}-alimtalk-notify-${var.environment}"
  group_name = "default"

  flexible_time_window {
    mode = "OFF"
  }

  schedule_expression = "rate(5 minutes)"

  target {
    arn      = data.aws_ecs_cluster.main.arn
    role_arn = aws_iam_role.eventbridge_batch.arn

    ecs_parameters {
      task_definition_arn = aws_ecs_task_definition.legacy_batch.arn
      launch_type         = "FARGATE"
      task_count          = 1

      network_configuration {
        subnets          = local.private_subnets
        security_groups  = [module.ecs_security_group.security_group_id]
        assign_public_ip = false
      }
    }

    input = jsonencode({
      containerOverrides = [
        {
          name = "legacy-batch"
          environment = [
            { name = "JOB_NAME", value = "alimTalkNotifyJob" }
          ]
        }
      ]
    })
  }

  state = "ENABLED"
}

# ========================================
# Schedule: 완료 주문 상태 업데이트 (매 15분)
# Job: updateCompletedOrdersJob
# ========================================
resource "aws_scheduler_schedule" "order_completed" {
  count = var.enable_eventbridge_schedules ? 1 : 0

  name       = "${var.project_name}-order-completed-${var.environment}"
  group_name = "default"

  flexible_time_window {
    mode = "OFF"
  }

  schedule_expression = "rate(15 minutes)"

  target {
    arn      = data.aws_ecs_cluster.main.arn
    role_arn = aws_iam_role.eventbridge_batch.arn

    ecs_parameters {
      task_definition_arn = aws_ecs_task_definition.legacy_batch.arn
      launch_type         = "FARGATE"
      task_count          = 1

      network_configuration {
        subnets          = local.private_subnets
        security_groups  = [module.ecs_security_group.security_group_id]
        assign_public_ip = false
      }
    }

    input = jsonencode({
      containerOverrides = [
        {
          name = "legacy-batch"
          environment = [
            { name = "JOB_NAME", value = "updateCompletedOrdersJob" }
          ]
        }
      ]
    })
  }

  state = "ENABLED"
}

# ========================================
# Schedule: 정산 주문 상태 업데이트 (매 15분)
# Job: updateSettlementOrdersJob
# ========================================
resource "aws_scheduler_schedule" "order_settlement" {
  count = var.enable_eventbridge_schedules ? 1 : 0

  name       = "${var.project_name}-order-settlement-${var.environment}"
  group_name = "default"

  flexible_time_window {
    mode = "OFF"
  }

  schedule_expression = "rate(15 minutes)"

  target {
    arn      = data.aws_ecs_cluster.main.arn
    role_arn = aws_iam_role.eventbridge_batch.arn

    ecs_parameters {
      task_definition_arn = aws_ecs_task_definition.legacy_batch.arn
      launch_type         = "FARGATE"
      task_count          = 1

      network_configuration {
        subnets          = local.private_subnets
        security_groups  = [module.ecs_security_group.security_group_id]
        assign_public_ip = false
      }
    }

    input = jsonencode({
      containerOverrides = [
        {
          name = "legacy-batch"
          environment = [
            { name = "JOB_NAME", value = "updateSettlementOrdersJob" }
          ]
        }
      ]
    })
  }

  state = "ENABLED"
}

# ========================================
# Log Streaming to OpenSearch (V2 - Kinesis)
# CloudWatch Logs → Kinesis → Lambda → OpenSearch
# ========================================
module "log_streaming" {
  source = "git::https://github.com/ryu-qqq/Infrastructure.git//terraform/modules/log-subscription-filter-v2?ref=main"

  log_group_name = module.legacy_batch_logs.log_group_name
  service_name   = "legacy-batch"
}
