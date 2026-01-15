# ========================================
# Terraform Provider Configuration
# ========================================
# Legacy Admin API - Strangler Fig Pattern
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
    key            = "setof-commerce/ecs-web-api-legacy-admin/terraform.tfstate"
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

variable "legacy_admin_cpu" {
  description = "CPU units for legacy-admin task"
  type        = number
  default     = 512
}

variable "legacy_admin_memory" {
  description = "Memory for legacy-admin task"
  type        = number
  default     = 1024
}

variable "legacy_admin_desired_count" {
  description = "Desired count for legacy-admin service"
  type        = number
  default     = 1
}

variable "image_tag" {
  description = "Docker image tag to deploy. Auto-set by GitHub Actions. Format: {component}-{build-number}-{git-sha}"
  type        = string
  default     = "legacy-api-admin-77-d58dcd3" # Stage 코드 배포 (2026-01-16)

  validation {
    condition     = can(regex("^legacy-api-admin-[0-9]+-[a-zA-Z0-9]+$", var.image_tag))
    error_message = "Image tag must follow format: legacy-api-admin-{build-number}-{git-sha} (e.g., legacy-api-admin-1-abc1234)"
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

data "aws_ssm_parameter" "public_subnets" {
  name = "/shared/network/public-subnets"
}

data "aws_ssm_parameter" "certificate_arn" {
  name = "/shared/network/certificate-arn"
}

data "aws_ssm_parameter" "route53_zone_id" {
  name = "/shared/network/route53-zone-id"
}

# ========================================
# RDS Configuration (MySQL - luxurydb)
# ========================================

# RDS Proxy endpoint from SSM Parameter Store
data "aws_ssm_parameter" "rds_proxy_endpoint" {
  name = "/shared/rds/proxy-endpoint"
}

# SetOf Commerce Secrets Manager secret (same DB as new architecture)
data "aws_secretsmanager_secret" "rds" {
  name = "setof-commerce/rds/credentials"
}

data "aws_secretsmanager_secret_version" "rds" {
  secret_id = data.aws_secretsmanager_secret.rds.id
}

# Legacy-specific secrets (JWT, PortOne, Slack, AWS)
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
  public_subnets  = split(",", data.aws_ssm_parameter.public_subnets.value)
  certificate_arn = data.aws_ssm_parameter.certificate_arn.value
  route53_zone_id = data.aws_ssm_parameter.route53_zone_id.value
  fqdn            = "commerce-admin.set-of.com" # Legacy admin server domain

  # RDS Configuration (MySQL - luxurydb schema)
  # Using RDS Proxy for connection pooling and failover resilience
  rds_credentials = jsondecode(data.aws_secretsmanager_secret_version.rds.secret_string)
  rds_host        = data.aws_ssm_parameter.rds_proxy_endpoint.value
  rds_port        = "3306"
  rds_dbname      = "luxurydb" # Legacy uses luxurydb schema
  rds_username    = local.rds_credentials.username

  # Redis Configuration
  redis_host = data.aws_ssm_parameter.redis_endpoint.value
  redis_port = tonumber(data.aws_ssm_parameter.redis_port.value)

  # AMP Configuration
  amp_workspace_arn    = data.aws_ssm_parameter.amp_workspace_arn.value
  amp_remote_write_url = data.aws_ssm_parameter.amp_remote_write_url.value
}
