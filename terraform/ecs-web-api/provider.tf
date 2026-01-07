# ========================================
# Terraform Provider Configuration
# ========================================
# Service Discovery Only (No ALB)
# Access via: web-api.connectly.local:8080
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
    key            = "setof-commerce/ecs-web-api/terraform.tfstate"
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

variable "web_api_cpu" {
  description = "CPU units for web-api task"
  type        = number
  default     = 512
}

variable "web_api_memory" {
  description = "Memory for web-api task"
  type        = number
  default     = 1024
}

variable "web_api_desired_count" {
  description = "Desired count for web-api service (0 = inactive, dev in progress)"
  type        = number
  default     = 0  # Set to 0: New architecture still in development
}

variable "image_tag" {
  description = "Docker image tag to deploy. Auto-set by GitHub Actions. Format: {component}-{build-number}-{git-sha}"
  type        = string
  default     = "web-api-1-initial" # Fallback only - GitHub Actions will override this

  validation {
    condition     = can(regex("^web-api-[0-9]+-[a-zA-Z0-9]+$", var.image_tag))
    error_message = "Image tag must follow format: web-api-{build-number}-{git-sha} (e.g., web-api-1-abc1234)"
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

# Note: public_subnets, certificate_arn, route53_zone_id removed
# This service uses Service Discovery only (no ALB)

# ========================================
# RDS Configuration (MySQL)
# ========================================

# SetOf Commerce Secrets Manager secret
data "aws_secretsmanager_secret" "rds" {
  name = "setof-commerce/rds/credentials"
}

data "aws_secretsmanager_secret_version" "rds" {
  secret_id = data.aws_secretsmanager_secret.rds.id
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
  # public_subnets, certificate_arn, route53_zone_id removed (no ALB)

  # RDS Configuration (MySQL)
  # Using RDS Proxy for connection pooling and failover resilience
  rds_credentials = jsondecode(data.aws_secretsmanager_secret_version.rds.secret_string)
  rds_host        = "prod-shared-mysql-proxy.proxy-cfacertspqbw.ap-northeast-2.rds.amazonaws.com"
  rds_port        = "3306"
  rds_dbname      = "setof"
  rds_username    = local.rds_credentials.username

  # Redis Configuration
  redis_host = data.aws_ssm_parameter.redis_endpoint.value
  redis_port = tonumber(data.aws_ssm_parameter.redis_port.value)

  # AMP Configuration
  amp_workspace_arn    = data.aws_ssm_parameter.amp_workspace_arn.value
  amp_remote_write_url = data.aws_ssm_parameter.amp_remote_write_url.value
}
