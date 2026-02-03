# ========================================
# Terraform Provider Configuration
# ========================================
# Web API Admin Stage - Service Discovery Only (No ALB)
# Access via: web-api-admin-stage.connectly.local:8081
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
    key            = "setof-commerce/ecs-web-api-admin-stage/terraform.tfstate"
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

variable "web_api_admin_cpu" {
  description = "CPU units for web-api-admin task"
  type        = number
  default     = 512
}

variable "web_api_admin_memory" {
  description = "Memory for web-api-admin task"
  type        = number
  default     = 1024
}

variable "web_api_admin_desired_count" {
  description = "Desired count for web-api-admin service (1 = Stage 고정)"
  type        = number
  default     = 1
}

variable "image_tag" {
  description = "Docker image tag to deploy"
  type        = string
  default     = "web-api-admin-stage-1-initial"

  validation {
    condition     = can(regex("^web-api-admin-stage-[0-9]+-[a-zA-Z0-9]+$", var.image_tag))
    error_message = "Image tag must follow format: web-api-admin-stage-{build-number}-{git-sha}"
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
# Staging RDS Configuration (MySQL)
# ========================================
data "aws_secretsmanager_secret" "rds" {
  name = "setof-commerce/rds/staging-credentials"
}

data "aws_secretsmanager_secret_version" "rds" {
  secret_id = data.aws_secretsmanager_secret.rds.id
}

# ========================================
# Service Token Secret (for internal service communication)
# ========================================
data "aws_ssm_parameter" "service_token_secret" {
  name = "/shared/security/service-token-secret"
}

# ========================================
# AuthHub Service Token (for AuthHub SDK authentication)
# ========================================
data "aws_ssm_parameter" "authhub_service_token" {
  name = "/authhub/stage/security/service-token-secret"
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
# Redis Configuration (Stage - Shared Redis)
# ========================================
# Stage Redis: stage-shared-redis.j9czrc.0001.apn2.cache.amazonaws.com:6379
# - auth_token: 없음 (비밀번호 없음)
# - transit_encryption_enabled: false (TLS 비활성화)

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
  redis_host         = "stage-shared-redis.j9czrc.0001.apn2.cache.amazonaws.com"
  redis_port         = 6379
  redis_password     = ""  # 비밀번호 없음
  redis_ssl_enabled  = "false"  # TLS 비활성화

  # AMP Configuration
  amp_workspace_arn    = data.aws_ssm_parameter.amp_workspace_arn.value
  amp_remote_write_url = data.aws_ssm_parameter.amp_remote_write_url.value

  # Sentry DSN (Stage: 비활성화 - 빈 값)
  sentry_dsn = ""
}
