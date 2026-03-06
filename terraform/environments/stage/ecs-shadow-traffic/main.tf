# ========================================
# ECS Scheduled Task: Shadow Traffic (Stage)
# ========================================
# Legacy API vs New API response comparison
# EventBridge Schedule -> ECS RunTask (Python)
# No DB/Redis needed - HTTP calls + CloudWatch metrics only
# ========================================

# ========================================
# Common Tags
# ========================================
locals {
  common_tags = {
    environment  = var.environment
    service_name = "${var.project_name}-shadow-traffic"
    team         = "platform-team"
    owner        = "platform@ryuqqq.com"
    cost_center  = "engineering"
    project      = var.project_name
    data_class   = "internal"
  }

  iam_tags = {
    service_name = "${var.project_name}-shadow-traffic"
    team         = "platform-team"
    owner        = "platform@ryuqqq.com"
    cost_center  = "engineering"
    data_class   = "internal"
  }
}

# ========================================
# ECR Repository Reference
# ========================================
data "aws_ecr_repository" "shadow_traffic" {
  name = "${var.project_name}-shadow-traffic-stage"
}

# ========================================
# ECS Cluster Reference
# ========================================
data "aws_ecs_cluster" "main" {
  cluster_name = "${var.project_name}-cluster-stage"
}

# VPC data source
data "aws_vpc" "main" {
  id = local.vpc_id
}

# ========================================
# KMS Key for CloudWatch Logs
# ========================================
resource "aws_kms_key" "logs" {
  description             = "KMS key for shadow-traffic Stage CloudWatch logs"
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
            "kms:EncryptionContext:aws:logs:arn" = "arn:aws:logs:${var.aws_region}:${data.aws_caller_identity.current.account_id}:log-group:/aws/ecs/${var.project_name}-shadow-traffic-${var.environment}/*"
          }
        }
      }
    ]
  })

  tags = merge(local.common_tags, {
    Name      = "${var.project_name}-shadow-traffic-logs-kms-${var.environment}"
    ManagedBy = "terraform"
  })
}

resource "aws_kms_alias" "logs" {
  name          = "alias/${var.project_name}-shadow-traffic-logs-${var.environment}"
  target_key_id = aws_kms_key.logs.key_id
}

# ========================================
# CloudWatch Log Group
# ========================================
module "shadow_traffic_logs" {
  source = "git::https://github.com/ryu-qqq/Infrastructure.git//terraform/modules/cloudwatch-log-group?ref=main"

  name              = "/aws/ecs/${var.project_name}-shadow-traffic-${var.environment}/application"
  retention_in_days = 14
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
# Security Group (Internal Only)
# ========================================
module "ecs_security_group" {
  source = "git::https://github.com/ryu-qqq/Infrastructure.git//terraform/modules/security-group?ref=main"

  name        = "${var.project_name}-shadow-traffic-sg-${var.environment}"
  description = "Security group for shadow-traffic ECS tasks (egress only)"
  vpc_id      = local.vpc_id

  type = "custom"

  # Shadow traffic only makes outbound HTTP calls
  # No ingress needed
  custom_ingress_rules = []

  environment  = local.common_tags.environment
  service_name = local.common_tags.service_name
  team         = local.common_tags.team
  owner        = local.common_tags.owner
  cost_center  = local.common_tags.cost_center
  project      = local.common_tags.project
  data_class   = local.common_tags.data_class
}

# ========================================
# IAM: Task Execution Role
# ========================================
module "task_execution_role" {
  source = "git::https://github.com/ryu-qqq/Infrastructure.git//terraform/modules/iam-role-policy?ref=main"

  role_name = "${var.project_name}-shadow-traffic-execution-role-${var.environment}"

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

  custom_inline_policies = {
    kms-access = {
      policy = jsonencode({
        Version = "2012-10-17"
        Statement = [
          {
            Effect   = "Allow"
            Action   = ["kms:Decrypt"]
            Resource = [aws_kms_key.logs.arn]
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
# IAM: Task Role (CloudWatch Metrics)
# ========================================
module "task_role" {
  source = "git::https://github.com/ryu-qqq/Infrastructure.git//terraform/modules/iam-role-policy?ref=main"

  role_name = "${var.project_name}-shadow-traffic-task-role-${var.environment}"

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
    shadow-traffic-access = {
      policy = jsonencode({
        Version = "2012-10-17"
        Statement = [
          {
            Sid    = "CloudWatchMetrics"
            Effect = "Allow"
            Action = ["cloudwatch:PutMetricData"]
            Resource = "*"
            Condition = {
              StringEquals = {
                "cloudwatch:namespace" = "ShadowTraffic"
              }
            }
          },
          {
            Sid    = "CloudWatchLogs"
            Effect = "Allow"
            Action = [
              "logs:CreateLogStream",
              "logs:PutLogEvents"
            ]
            Resource = ["${module.shadow_traffic_logs.log_group_arn}:*"]
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
# ECS Task Definition (RunTask - no Service)
# ========================================
resource "aws_ecs_task_definition" "shadow_traffic" {
  family                   = "${var.project_name}-shadow-traffic-${var.environment}"
  network_mode             = "awsvpc"
  requires_compatibilities = ["FARGATE"]
  cpu                      = var.task_cpu
  memory                   = var.task_memory
  execution_role_arn       = module.task_execution_role.role_arn
  task_role_arn            = module.task_role.role_arn

  container_definitions = jsonencode([
    {
      name      = "shadow-traffic"
      image     = "${data.aws_ecr_repository.shadow_traffic.repository_url}:${var.image_tag}"
      essential = true

      environment = [
        { name = "LEGACY_URL", value = "http://${var.project_name}-legacy-api-${var.environment}.connectly.local:8088" },
        { name = "NEW_URL", value = "http://${var.project_name}-web-api-${var.environment}.connectly.local:8080" },
        { name = "AWS_REGION", value = var.aws_region },
        { name = "DRY_RUN", value = "false" },
        { name = "DOMAINS", value = "" },
        { name = "SLACK_WEBHOOK_URL", value = var.slack_webhook_url },
        { name = "DASHBOARD_URL", value = var.dashboard_url }
      ]

      logConfiguration = {
        logDriver = "awslogs"
        options = {
          "awslogs-group"         = module.shadow_traffic_logs.log_group_name
          "awslogs-region"        = var.aws_region
          "awslogs-stream-prefix" = "shadow-traffic"
        }
      }
    }
  ])

  tags = merge(local.common_tags, {
    Name = "${var.project_name}-shadow-traffic-task-${var.environment}"
  })
}

# ========================================
# EventBridge Schedule
# ========================================
resource "aws_iam_role" "eventbridge" {
  name = "${var.project_name}-eventbridge-shadow-traffic-role-${var.environment}"

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

resource "aws_iam_role_policy" "eventbridge" {
  name = "eventbridge-shadow-traffic-policy"
  role = aws_iam_role.eventbridge.id

  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Effect = "Allow"
        Action = ["ecs:RunTask"]
        Resource = [
          aws_ecs_task_definition.shadow_traffic.arn,
          "${aws_ecs_task_definition.shadow_traffic.arn_without_revision}:*"
        ]
      },
      {
        Effect = "Allow"
        Action = ["iam:PassRole"]
        Resource = [
          module.task_execution_role.role_arn,
          module.task_role.role_arn
        ]
      }
    ]
  })
}

resource "aws_scheduler_schedule" "shadow_traffic" {
  count = var.enable_schedule ? 1 : 0

  name       = "${var.project_name}-shadow-traffic-${var.environment}"
  group_name = "default"

  flexible_time_window {
    mode = "OFF"
  }

  schedule_expression = var.schedule_expression

  target {
    arn      = data.aws_ecs_cluster.main.arn
    role_arn = aws_iam_role.eventbridge.arn

    ecs_parameters {
      task_definition_arn = aws_ecs_task_definition.shadow_traffic.arn
      launch_type         = "FARGATE"
      task_count          = 1

      network_configuration {
        subnets          = local.private_subnets
        security_groups  = [module.ecs_security_group.security_group_id]
        assign_public_ip = false
      }
    }
  }

  state = "ENABLED"
}

# ========================================
# Log Streaming to OpenSearch (V2 - Kinesis)
# CloudWatch Logs -> Kinesis -> Lambda -> OpenSearch
# ========================================
module "log_streaming" {
  source = "git::https://github.com/ryu-qqq/Infrastructure.git//terraform/modules/log-subscription-filter-v2?ref=main"

  log_group_name = module.shadow_traffic_logs.log_group_name
  service_name   = "shadow-traffic"
}

# ========================================
# SNS Topic for Shadow Traffic Alarms
# ========================================
module "alarm_topic" {
  source = "git::https://github.com/ryu-qqq/Infrastructure.git//terraform/modules/sns?ref=main"

  name         = "${var.project_name}-shadow-traffic-alarm-${var.environment}"
  display_name = "Shadow Traffic Diff Alerts (Stage)"
  kms_key_id   = aws_kms_key.logs.arn

  subscriptions = var.alert_email != "" ? {
    email = {
      protocol = "email"
      endpoint = var.alert_email
    }
  } : {}

  enable_cloudwatch_alarms = false

  environment  = "staging"
  service      = "shadow-traffic"
  team         = local.common_tags.team
  owner        = local.common_tags.owner
  cost_center  = local.common_tags.cost_center
  project      = local.common_tags.project
  data_class   = local.common_tags.data_class
}

# ========================================
# CloudWatch Alarms - DiffRate per Domain
# ========================================
resource "aws_cloudwatch_metric_alarm" "diff_rate" {
  for_each = toset(var.alarm_domains)

  alarm_name        = "${var.project_name}-shadow-traffic-${each.value}-diff-rate-${var.environment}"
  alarm_description = "Shadow traffic diff rate exceeded ${var.alarm_diff_rate_threshold}% for domain: ${each.value}"

  namespace   = "ShadowTraffic"
  metric_name = "DiffRate"
  dimensions = {
    Domain = each.value
  }

  statistic           = "Average"
  period              = var.alarm_period
  evaluation_periods  = var.alarm_evaluation_periods
  threshold           = var.alarm_diff_rate_threshold
  comparison_operator = "GreaterThanThreshold"
  treat_missing_data  = "notBreaching"

  alarm_actions = [module.alarm_topic.topic_arn]
  ok_actions    = [module.alarm_topic.topic_arn]

  tags = merge(local.common_tags, {
    Name   = "${var.project_name}-shadow-traffic-${each.value}-diff-rate-${var.environment}"
    Domain = each.value
  })
}

# ========================================
# Outputs
# ========================================
output "task_definition_arn" {
  description = "ECS Task Definition ARN"
  value       = aws_ecs_task_definition.shadow_traffic.arn
}

output "task_definition_family" {
  description = "ECS Task Definition Family"
  value       = aws_ecs_task_definition.shadow_traffic.family
}

output "security_group_id" {
  description = "Security Group ID"
  value       = module.ecs_security_group.security_group_id
}

output "log_group_name" {
  description = "CloudWatch Log Group name"
  value       = module.shadow_traffic_logs.log_group_name
}

output "alarm_topic_arn" {
  description = "SNS Topic ARN for shadow traffic alarms"
  value       = module.alarm_topic.topic_arn
}
