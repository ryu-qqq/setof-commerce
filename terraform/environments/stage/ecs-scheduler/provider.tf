# ========================================
# Terraform Provider Configuration
# ========================================
# Scheduler Stage - Outbox Processing
# ========================================

terraform {
  required_version = ">= 1.0"

  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }

  backend "s3" {
    bucket         = "prod-connectly"
    key            = "setof-commerce/ecs-scheduler-stage/terraform.tfstate"
    region         = "ap-northeast-2"
    dynamodb_table = "prod-connectly-tf-lock"
    encrypt        = true
    kms_key_id     = "arn:aws:kms:ap-northeast-2:646886795421:key/086b1677-614f-46ba-863e-23c215fb5010"
  }
}

provider "aws" {
  region = var.aws_region

  default_tags {
    tags = {
      Project     = var.project_name
      Environment = var.environment
      ManagedBy   = "terraform"
    }
  }
}

# ========================================
# Common Variables
# ========================================
variable "project_name" {
  description = "Project name"
  type        = string
  default     = "setof-commerce"
}

variable "environment" {
  description = "Environment name"
  type        = string
  default     = "stage"
}

variable "aws_region" {
  description = "AWS region"
  type        = string
  default     = "ap-northeast-2"
}

variable "scheduler_cpu" {
  description = "CPU units for scheduler task"
  type        = number
  default     = 256
}

variable "scheduler_memory" {
  description = "Memory for scheduler task"
  type        = number
  default     = 512
}

variable "scheduler_desired_count" {
  description = "Desired count for scheduler service (0 = inactive)"
  type        = number
  default     = 1
}

variable "image_tag" {
  description = "Docker image tag to deploy"
  type        = string
  default     = "scheduler-stage-13-56cfddd"

  validation {
    condition     = can(regex("^scheduler-stage-[0-9]+-[a-zA-Z0-9]+$", var.image_tag))
    error_message = "Image tag must follow format: scheduler-stage-{build-number}-{git-sha}"
  }
}

# ========================================
# Shared Resource References (SSM)
# ========================================
data "aws_ssm_parameter" "vpc_id" {
  name = "/shared/network/vpc-id"
}

data "aws_ssm_parameter" "private_subnets" {
  name = "/shared/network/private-subnets"
}

# ========================================
# Staging RDS Configuration
# ========================================
data "aws_secretsmanager_secret" "rds" {
  name = "setof-commerce/rds/staging-credentials"
}

data "aws_secretsmanager_secret_version" "rds" {
  secret_id = data.aws_secretsmanager_secret.rds.id
}

# Legacy-specific secrets
data "aws_secretsmanager_secret" "legacy" {
  name = "setof-commerce/legacy/credentials"
}

data "aws_secretsmanager_secret_version" "legacy" {
  secret_id = data.aws_secretsmanager_secret.legacy.id
}

# ========================================
# Monitoring Configuration (AMP)
# ========================================
data "aws_ssm_parameter" "amp_workspace_arn" {
  name = "/shared/monitoring/amp-workspace-arn"
}

data "aws_ssm_parameter" "amp_remote_write_url" {
  name = "/shared/monitoring/amp-remote-write-url"
}

# ========================================
# Redis Configuration (Stage Shared Redis)
# ========================================
data "aws_ssm_parameter" "redis_endpoint" {
  name = "/${var.project_name}/elasticache-stage/redis-endpoint"
}

data "aws_ssm_parameter" "redis_port" {
  name = "/${var.project_name}/elasticache-stage/redis-port"
}

# ========================================
# Locals
# ========================================
locals {
  vpc_id          = data.aws_ssm_parameter.vpc_id.value
  private_subnets = split(",", data.aws_ssm_parameter.private_subnets.value)

  # Stage RDS Configuration
  rds_credentials = jsondecode(data.aws_secretsmanager_secret_version.rds.secret_string)
  rds_host        = local.rds_credentials.host
  rds_port        = local.rds_credentials.port
  rds_dbname      = "luxurydb"
  rds_username    = local.rds_credentials.username

  # Stage Redis Configuration (Shared Redis - No Auth, No TLS)
  redis_host        = data.aws_ssm_parameter.redis_endpoint.value
  redis_port        = tonumber(data.aws_ssm_parameter.redis_port.value)
  redis_password    = ""
  redis_ssl_enabled = "false"

  # AMP Configuration
  amp_workspace_arn    = data.aws_ssm_parameter.amp_workspace_arn.value
  amp_remote_write_url = data.aws_ssm_parameter.amp_remote_write_url.value

  # Sentry DSN (Stage: disabled)
  sentry_dsn = ""
}
