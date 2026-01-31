# ========================================
# ECS Service: Scheduler (Production)
# ========================================
# Outbox Pattern Processing:
# - SellerAuthOutbox processing
# - Timeout Outbox recovery
# Port: 8083 (internal only)
# ========================================

# ========================================
# Common Tags (for governance)
# ========================================
locals {
  common_tags = {
    environment  = var.environment
    service_name = "${var.project_name}-scheduler"
    team         = "platform-team"
    owner        = "platform@ryuqqq.com"
    cost_center  = "engineering"
    project      = var.project_name
    data_class   = "internal"
  }
}

# ========================================
# ECR Repository Reference
# ========================================
data "aws_ecr_repository" "scheduler" {
  name = "${var.project_name}-scheduler-prod"
}

# ========================================
# RDS Proxy Security Group Reference
# ========================================
data "aws_db_proxy" "rds_proxy" {
  name = "prod-shared-mysql-proxy"
}

# ========================================
# ECS Cluster Reference
# ========================================
data "aws_ssm_parameter" "cluster_name" {
  name = "/${var.project_name}/ecs/cluster-name"
}

data "aws_ecs_cluster" "main" {
  cluster_name = data.aws_ssm_parameter.cluster_name.value
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
  description             = "KMS key for SetOf Commerce scheduler CloudWatch logs encryption"
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
            "kms:EncryptionContext:aws:logs:arn" = "arn:aws:logs:${var.aws_region}:${data.aws_caller_identity.current.account_id}:log-group:/aws/ecs/${var.project_name}-scheduler-${var.environment}/*"
          }
        }
      }
    ]
  })

  tags = merge(local.common_tags, {
    Name      = "${var.project_name}-scheduler-logs-kms-${var.environment}"
    ManagedBy = "terraform"
  })
}

resource "aws_kms_alias" "logs" {
  name          = "alias/${var.project_name}-scheduler-logs-${var.environment}"
  target_key_id = aws_kms_key.logs.key_id
}

# ========================================
# CloudWatch Log Group (using Infrastructure module)
# ========================================
module "scheduler_logs" {
  source = "git::https://github.com/ryu-qqq/Infrastructure.git//terraform/modules/cloudwatch-log-group?ref=main"

  name              = "/aws/ecs/${var.project_name}-scheduler-${var.environment}/application"
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
# Security Groups (Internal only - no ALB)
# ========================================
module "ecs_security_group" {
  source = "git::https://github.com/ryu-qqq/Infrastructure.git//terraform/modules/security-group?ref=main"

  name        = "${var.project_name}-scheduler-sg-${var.environment}"
  description = "Security group for scheduler ECS tasks - Internal only"
  vpc_id      = local.vpc_id

  type = "custom"

  # Internal communication only
  custom_ingress_rules = [
    {
      from_port   = 8083
      to_port     = 8083
      protocol    = "tcp"
      cidr_block  = data.aws_vpc.main.cidr_block
      description = "Internal VPC communication for health checks"
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
# IAM Roles (using Infrastructure module)
# ========================================

# ECS Task Execution Role
module "scheduler_task_execution_role" {
  source = "git::https://github.com/ryu-qqq/Infrastructure.git//terraform/modules/iam-role-policy?ref=main"

  role_name = "${var.project_name}-scheduler-execution-role-${var.environment}"

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

  environment  = local.common_tags.environment
  service_name = local.common_tags.service_name
  team         = local.common_tags.team
  owner        = local.common_tags.owner
  cost_center  = local.common_tags.cost_center
  project      = local.common_tags.project
  data_class   = local.common_tags.data_class
}

# ECS Task Role
module "scheduler_task_role" {
  source = "git::https://github.com/ryu-qqq/Infrastructure.git//terraform/modules/iam-role-policy?ref=main"

  role_name = "${var.project_name}-scheduler-task-role-${var.environment}"

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
    scheduler-access = {
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

  environment  = local.common_tags.environment
  service_name = local.common_tags.service_name
  team         = local.common_tags.team
  owner        = local.common_tags.owner
  cost_center  = local.common_tags.cost_center
  project      = local.common_tags.project
  data_class   = local.common_tags.data_class
}

# ========================================
# ADOT Sidecar (using Infrastructure module)
# ========================================
module "adot_sidecar" {
  source = "git::https://github.com/ryu-qqq/Infrastructure.git//terraform/modules/adot-sidecar?ref=main"

  project_name              = var.project_name
  service_name              = "scheduler"
  aws_region                = var.aws_region
  amp_workspace_arn         = local.amp_workspace_arn
  amp_remote_write_endpoint = local.amp_remote_write_url
  log_group_name            = module.scheduler_logs.log_group_name
  app_port                  = 8083
  cluster_name              = data.aws_ecs_cluster.main.cluster_name
  environment               = var.environment
  config_bucket             = "prod-connectly"
  config_version            = "20251215"
}

# ========================================
# ECS Service (using Infrastructure module)
# ========================================
module "ecs_service" {
  source = "git::https://github.com/ryu-qqq/Infrastructure.git//terraform/modules/ecs-service?ref=main"

  # Service Configuration
  name            = "${var.project_name}-scheduler-${var.environment}"
  cluster_id      = data.aws_ecs_cluster.main.arn
  container_name  = "scheduler"
  container_image = "${data.aws_ecr_repository.scheduler.repository_url}:${var.image_tag}"
  container_port  = 8083
  cpu             = var.scheduler_cpu
  memory          = var.scheduler_memory
  desired_count   = var.scheduler_desired_count

  # IAM Roles
  execution_role_arn = module.scheduler_task_execution_role.role_arn
  task_role_arn      = module.scheduler_task_role.role_arn

  # Network Configuration (Internal only - no ALB)
  subnet_ids         = local.private_subnets
  security_group_ids = [module.ecs_security_group.security_group_id]
  assign_public_ip   = false

  # No Load Balancer (internal service)

  # Health Check Grace Period
  health_check_grace_period_seconds = 60

  # Container Environment Variables
  container_environment = [
    { name = "SPRING_PROFILES_ACTIVE", value = var.environment },
    # Database (using RDS Proxy)
    { name = "DB_HOST", value = local.rds_host },
    { name = "DB_PORT", value = local.rds_port },
    { name = "DB_NAME", value = local.rds_dbname },
    { name = "DB_USERNAME", value = local.rds_username },
    # Redis
    { name = "REDIS_HOST", value = local.redis_host },
    { name = "REDIS_PORT", value = tostring(local.redis_port) },
    { name = "REDIS_DATABASE", value = "0" },
    { name = "REDIS_SSL_ENABLED", value = "true" },
    # Security Service Token
    { name = "SECURITY_SERVICE_TOKEN_ENABLED", value = "true" }
  ]

  # Container Secrets (from Secrets Manager and SSM)
  container_secrets = [
    { name = "DB_PASSWORD", valueFrom = "${data.aws_secretsmanager_secret.rds.arn}:password::" },
    { name = "REDIS_PASSWORD", valueFrom = "arn:aws:ssm:${var.aws_region}:${data.aws_caller_identity.current.account_id}:parameter/${var.project_name}/elasticache/redis-auth-token" },
    { name = "SECURITY_SERVICE_TOKEN_SECRET", valueFrom = "arn:aws:ssm:${var.aws_region}:${data.aws_caller_identity.current.account_id}:parameter/shared/security/service-token-secret" }
  ]

  # Health Check
  health_check_command      = ["CMD-SHELL", "wget --no-verbose --tries=1 --spider http://localhost:8083/actuator/health || exit 1"]
  health_check_interval     = 30
  health_check_timeout      = 5
  health_check_retries      = 3
  health_check_start_period = 60

  # Logging
  log_configuration = {
    log_driver = "awslogs"
    options = {
      "awslogs-group"         = module.scheduler_logs.log_group_name
      "awslogs-region"        = var.aws_region
      "awslogs-stream-prefix" = "scheduler"
    }
  }

  # ADOT Sidecar
  sidecars = [module.adot_sidecar.container_definition]

  # Auto Scaling (Production)
  enable_autoscaling        = true
  autoscaling_min_capacity  = 1
  autoscaling_max_capacity  = 2
  autoscaling_target_cpu    = 70
  autoscaling_target_memory = 80

  # Enable ECS Exec for debugging
  enable_execute_command = true

  # Deployment Configuration - Zero-Downtime
  deployment_maximum_percent         = 200
  deployment_minimum_healthy_percent = 100
  deployment_circuit_breaker_enable   = true
  deployment_circuit_breaker_rollback = true

  # Tagging
  environment  = local.common_tags.environment
  service_name = local.common_tags.service_name
  team         = local.common_tags.team
  owner        = local.common_tags.owner
  cost_center  = local.common_tags.cost_center
  project      = local.common_tags.project
  data_class   = local.common_tags.data_class
}

# ========================================
# Outputs
# ========================================
output "service_name" {
  description = "ECS Service name"
  value       = module.ecs_service.service_name
}

output "task_definition_arn" {
  description = "Task Definition ARN"
  value       = module.ecs_service.task_definition_arn
}

output "security_group_id" {
  description = "Security Group ID"
  value       = module.ecs_security_group.security_group_id
}

output "log_group_name" {
  description = "CloudWatch Log Group name"
  value       = module.scheduler_logs.log_group_name
}
