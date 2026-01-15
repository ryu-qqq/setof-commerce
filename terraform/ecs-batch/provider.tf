# ========================================
# Terraform Provider Configuration
# ========================================
# Legacy Batch - Spring Batch Jobs
# Python Airflow 대체
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
    key            = "setof-commerce/ecs-batch/terraform.tfstate"
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
  default     = "prod"
}

variable "aws_region" {
  description = "AWS region"
  type        = string
  default     = "ap-northeast-2"
}

variable "batch_cpu" {
  description = "CPU units for batch task"
  type        = number
  default     = 512
}

variable "batch_memory" {
  description = "Memory for batch task"
  type        = number
  default     = 1024
}

variable "batch_desired_count" {
  description = "Desired count for batch service"
  type        = number
  default     = 1
}

variable "image_tag" {
  description = "Docker image tag to deploy. Auto-set by GitHub Actions."
  type        = string
  default     = "legacy-batch-79-d58dcd3" # Stage 코드 배포 (2026-01-16)

  validation {
    condition     = can(regex("^legacy-batch-[0-9]+-[a-zA-Z0-9]+$", var.image_tag))
    error_message = "Image tag must follow format: legacy-batch-{build-number}-{git-sha}"
  }
}

variable "enable_eventbridge_schedules" {
  description = "Enable EventBridge Scheduler resources. Requires scheduler:* IAM permissions."
  type        = bool
  default     = true
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
# RDS Configuration (MySQL)
# ========================================
data "aws_secretsmanager_secret" "rds" {
  name = "setof-commerce/rds/credentials"
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
# Redis Configuration
# ========================================
data "aws_ssm_parameter" "redis_endpoint" {
  name = "/${var.project_name}/elasticache/redis-endpoint"
}

data "aws_ssm_parameter" "redis_port" {
  name = "/${var.project_name}/elasticache/redis-port"
}

# ========================================
# Locals
# ========================================
locals {
  vpc_id          = data.aws_ssm_parameter.vpc_id.value
  private_subnets = split(",", data.aws_ssm_parameter.private_subnets.value)

  # RDS Configuration (MySQL - luxurydb schema)
  rds_credentials = jsondecode(data.aws_secretsmanager_secret_version.rds.secret_string)
  rds_host        = "prod-shared-mysql-proxy.proxy-cfacertspqbw.ap-northeast-2.rds.amazonaws.com"
  rds_port        = "3306"
  rds_dbname      = "luxurydb"
  rds_username    = local.rds_credentials.username

  # Redis Configuration
  redis_host = data.aws_ssm_parameter.redis_endpoint.value
  redis_port = tonumber(data.aws_ssm_parameter.redis_port.value)

  # AMP Configuration
  amp_workspace_arn    = data.aws_ssm_parameter.amp_workspace_arn.value
  amp_remote_write_url = data.aws_ssm_parameter.amp_remote_write_url.value
}
