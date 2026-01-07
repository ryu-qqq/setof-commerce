# ========================================
# Terraform Provider Configuration
# ========================================
# Migration Service: Data sync & batch processing
# Access via: migration.connectly.local:8082
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
    key            = "setof-commerce/ecs-migration/terraform.tfstate"
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

variable "migration_cpu" {
  description = "CPU units for migration task"
  type        = number
  default     = 512
}

variable "migration_memory" {
  description = "Memory for migration task"
  type        = number
  default     = 1024
}

variable "migration_desired_count" {
  description = "Desired count for migration service (0 = inactive, dev in progress)"
  type        = number
  default     = 0  # Set to 0: Migration service still in development
}

variable "image_tag" {
  description = "Docker image tag to deploy. Auto-set by GitHub Actions. Format: {component}-{build-number}-{git-sha}"
  type        = string
  default     = "migration-1-initial" # Fallback only - GitHub Actions will override this

  validation {
    condition     = can(regex("^migration-[0-9]+-[a-zA-Z0-9]+$", var.image_tag))
    error_message = "Image tag must follow format: migration-{build-number}-{git-sha} (e.g., migration-1-abc1234)"
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
# RDS Configuration (MySQL) - Primary (New Schema)
# ========================================
data "aws_ssm_parameter" "rds_proxy_endpoint" {
  name = "/shared/rds/proxy-endpoint"
}

data "aws_secretsmanager_secret" "rds" {
  name = "setof-commerce/rds/credentials"
}

data "aws_secretsmanager_secret_version" "rds" {
  secret_id = data.aws_secretsmanager_secret.rds.id
}

# ========================================
# Legacy RDS Configuration (MySQL) - Legacy Schema
# ========================================
data "aws_secretsmanager_secret" "legacy_rds" {
  name = "setof-commerce/legacy-rds/credentials"
}

data "aws_secretsmanager_secret_version" "legacy_rds" {
  secret_id = data.aws_secretsmanager_secret.legacy_rds.id
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
# Redis Configuration (from ElastiCache module)
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

  # Primary RDS Configuration (New Schema)
  rds_credentials = jsondecode(data.aws_secretsmanager_secret_version.rds.secret_string)
  rds_host        = data.aws_ssm_parameter.rds_proxy_endpoint.value
  rds_port        = "3306"
  rds_dbname      = "setof"
  rds_username    = local.rds_credentials.username

  # Legacy RDS Configuration (Legacy Schema)
  legacy_rds_credentials = jsondecode(data.aws_secretsmanager_secret_version.legacy_rds.secret_string)
  legacy_rds_host        = local.legacy_rds_credentials.host
  legacy_rds_port        = "3306"
  legacy_rds_dbname      = local.legacy_rds_credentials.dbname
  legacy_rds_username    = local.legacy_rds_credentials.username

  # Redis Configuration
  redis_host = data.aws_ssm_parameter.redis_endpoint.value
  redis_port = tonumber(data.aws_ssm_parameter.redis_port.value)

  # AMP Configuration
  amp_workspace_arn    = data.aws_ssm_parameter.amp_workspace_arn.value
  amp_remote_write_url = data.aws_ssm_parameter.amp_remote_write_url.value
}
