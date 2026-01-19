# ========================================
# ECS Service: web-api-admin (Stage)
# ========================================
# Admin REST API server with Service Discovery (Cloud Map)
# Using Infrastructure modules
# Access: Internal VPC only via Service Discovery
# FQDN: web-api-admin-stage.connectly.local:8081
# ========================================

# ========================================
# Common Tags (for governance)
# ========================================
locals {
  common_tags = {
    environment  = var.environment
    service_name = "${var.project_name}-web-api-admin"
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
data "aws_ecr_repository" "web_api_admin" {
  name = "${var.project_name}-web-api-admin-stage"
}

# Note: ECR and ECS cluster use "-stage" suffix while Terraform uses "staging" for environment

# ========================================
# ECS Cluster Reference (Stage cluster)
# ========================================
data "aws_ecs_cluster" "main" {
  cluster_name = "${var.project_name}-cluster-stage"
}

# Note: Cluster uses "-stage" suffix

data "aws_caller_identity" "current" {}

# ========================================
# Service Discovery Namespace (from shared infrastructure)
# ========================================
data "aws_ssm_parameter" "service_discovery_namespace_id" {
  name = "/shared/service-discovery/namespace-id"
}

# VPC data source for internal communication
data "aws_vpc" "main" {
  id = local.vpc_id
}

# ========================================
# KMS Key for CloudWatch Logs Encryption
# ========================================
resource "aws_kms_key" "logs" {
  description             = "KMS key for SetOf Commerce web-api-admin Stage CloudWatch logs encryption"
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
            "kms:EncryptionContext:aws:logs:arn" = "arn:aws:logs:${var.aws_region}:${data.aws_caller_identity.current.account_id}:log-group:/aws/ecs/${var.project_name}-web-api-admin-${var.environment}/*"
          }
        }
      }
    ]
  })

  tags = merge(local.common_tags, {
    Name      = "${var.project_name}-web-api-admin-logs-kms-${var.environment}"
    Lifecycle = "staging"
    ManagedBy = "terraform"
  })
}

resource "aws_kms_alias" "logs" {
  name          = "alias/${var.project_name}-web-api-admin-logs-${var.environment}"
  target_key_id = aws_kms_key.logs.key_id
}

# ========================================
# CloudWatch Log Group (using Infrastructure module)
# ========================================
module "web_api_admin_logs" {
  source = "git::https://github.com/ryu-qqq/Infrastructure.git//terraform/modules/cloudwatch-log-group?ref=main"

  name              = "/aws/ecs/${var.project_name}-web-api-admin-${var.environment}/application"
  retention_in_days = 14 # Stage: 14 days retention
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
# Security Group (VPC Internal Only)
# ========================================
module "ecs_security_group" {
  source = "git::https://github.com/ryu-qqq/Infrastructure.git//terraform/modules/security-group?ref=main"

  name        = "${var.project_name}-web-api-admin-sg-${var.environment}"
  description = "Security group for web-api-admin ECS tasks Stage"
  vpc_id      = local.vpc_id

  type = "custom"

  custom_ingress_rules = [
    {
      from_port   = 8081
      to_port     = 8081
      protocol    = "tcp"
      cidr_block  = data.aws_vpc.main.cidr_block
      description = "Service Discovery - internal VPC communication"
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
module "web_api_admin_task_execution_role" {
  source = "git::https://github.com/ryu-qqq/Infrastructure.git//terraform/modules/iam-role-policy?ref=main"

  role_name = "${var.project_name}-web-api-admin-execution-role-${var.environment}"

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
  secrets_manager_secret_arns   = [data.aws_secretsmanager_secret.rds.arn]

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
module "web_api_admin_task_role" {
  source = "git::https://github.com/ryu-qqq/Infrastructure.git//terraform/modules/iam-role-policy?ref=main"

  role_name = "${var.project_name}-web-api-admin-task-role-${var.environment}"

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
    adot-amp-access = {
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
          },
          {
            Sid    = "S3OtelConfigAccess"
            Effect = "Allow"
            Action = [
              "s3:GetObject"
            ]
            Resource = "arn:aws:s3:::prod-connectly/otel-config/*"
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
  service_name              = "web-api-admin"
  aws_region                = var.aws_region
  amp_workspace_arn         = local.amp_workspace_arn
  amp_remote_write_endpoint = local.amp_remote_write_url
  log_group_name            = module.web_api_admin_logs.log_group_name
  app_port                  = 8081
  cluster_name              = data.aws_ecs_cluster.main.cluster_name
  environment               = var.environment
  config_bucket             = "prod-connectly"
  config_version            = "20251216"
}

# ========================================
# ECS Service (using Infrastructure module)
# ========================================
module "ecs_service" {
  source = "git::https://github.com/ryu-qqq/Infrastructure.git//terraform/modules/ecs-service?ref=main"

  # Service Configuration
  name            = "${var.project_name}-web-api-admin-${var.environment}"
  cluster_id      = data.aws_ecs_cluster.main.arn
  container_name  = "web-api-admin"
  container_image = "${data.aws_ecr_repository.web_api_admin.repository_url}:${var.image_tag}"
  container_port  = 8081
  cpu             = var.web_api_admin_cpu
  memory          = var.web_api_admin_memory
  desired_count   = var.web_api_admin_desired_count

  # IAM Roles
  execution_role_arn = module.web_api_admin_task_execution_role.role_arn
  task_role_arn      = module.web_api_admin_task_role.role_arn

  # Network Configuration
  subnet_ids         = local.private_subnets
  security_group_ids = [module.ecs_security_group.security_group_id]
  assign_public_ip   = false

  # Container Environment Variables
  container_environment = [
    { name = "SPRING_PROFILES_ACTIVE", value = var.environment },
    { name = "DB_HOST", value = local.rds_host },
    { name = "DB_PORT", value = local.rds_port },
    { name = "DB_NAME", value = local.rds_dbname },
    { name = "DB_USERNAME", value = local.rds_username },
    { name = "REDIS_HOST", value = local.redis_host },
    { name = "REDIS_PORT", value = tostring(local.redis_port) },
    # Service Token authentication enabled (for internal service communication)
    { name = "SECURITY_SERVICE_TOKEN_ENABLED", value = "true" }
  ]

  # Container Secrets
  container_secrets = [
    { name = "DB_PASSWORD", valueFrom = "${data.aws_secretsmanager_secret.rds.arn}:password::" },
    # Service Token Secret (for internal service authentication)
    { name = "SECURITY_SERVICE_TOKEN_SECRET", valueFrom = data.aws_ssm_parameter.service_token_secret.arn }
  ]

  # Health Check
  health_check_command      = ["CMD-SHELL", "wget --no-verbose --tries=1 --spider http://localhost:8081/actuator/health || exit 1"]
  health_check_interval     = 30
  health_check_timeout      = 5
  health_check_retries      = 3
  health_check_start_period = 120

  # Logging
  log_configuration = {
    log_driver = "awslogs"
    options = {
      "awslogs-group"         = module.web_api_admin_logs.log_group_name
      "awslogs-region"        = var.aws_region
      "awslogs-stream-prefix" = "web-api-admin"
    }
  }

  # ADOT Sidecar
  sidecars = [module.adot_sidecar.container_definition]

  # Auto Scaling (Stage: conservative settings)
  enable_autoscaling        = true
  autoscaling_min_capacity  = 0
  autoscaling_max_capacity  = 2
  autoscaling_target_cpu    = 70
  autoscaling_target_memory = 80

  # Enable ECS Exec for debugging
  enable_execute_command = true

  # Deployment Configuration
  deployment_circuit_breaker_enable   = true
  deployment_circuit_breaker_rollback = true

  # Service Discovery Configuration (Cloud Map)
  enable_service_discovery         = true
  service_discovery_namespace_id   = data.aws_ssm_parameter.service_discovery_namespace_id.value
  service_discovery_namespace_name = "connectly.local"

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
  description = "ECS Task Definition ARN"
  value       = module.ecs_service.task_definition_arn
}

output "security_group_id" {
  description = "Security Group ID"
  value       = module.ecs_security_group.security_group_id
}
