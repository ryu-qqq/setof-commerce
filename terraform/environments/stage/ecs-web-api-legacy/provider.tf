# ========================================
# Terraform Provider Configuration
# ========================================
# Legacy Web API Stage - Strangler Fig Pattern
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
    key            = "setof-commerce/ecs-web-api-legacy-stage/terraform.tfstate"
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

variable "legacy_api_cpu" {
  description = "CPU units for legacy-api task"
  type        = number
  default     = 1024
}

variable "legacy_api_memory" {
  description = "Memory for legacy-api task"
  type        = number
  default     = 2048
}

variable "legacy_api_desired_count" {
  description = "Desired count for legacy-api service (1 = Stage 고정)"
  type        = number
  default     = 1
}

variable "image_tag" {
  description = "Docker image tag to deploy"
  type        = string
  default     = "legacy-api-stage-13-56cfddd"

  validation {
    condition     = can(regex("^legacy-api-stage-[0-9]+-[a-zA-Z0-9]+$", var.image_tag))
    error_message = "Image tag must follow format: legacy-api-stage-{build-number}-{git-sha}"
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
# Staging RDS Configuration (MySQL - luxurydb)
# ========================================

# Staging RDS Credentials
data "aws_secretsmanager_secret" "rds" {
  name = "setof-commerce/rds/staging-credentials"
}

data "aws_secretsmanager_secret_version" "rds" {
  secret_id = data.aws_secretsmanager_secret.rds.id
}

# Legacy-specific secrets (Kakao, JWT, PortOne, Slack, AWS)
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
  public_subnets  = split(",", data.aws_ssm_parameter.public_subnets.value)
  certificate_arn = data.aws_ssm_parameter.certificate_arn.value
  route53_zone_id = data.aws_ssm_parameter.route53_zone_id.value
  # fqdn removed: stage.set-of.com DNS is managed by Infrastructure repo's cloudfront-routing module

  # Stage RDS Configuration
  rds_credentials = jsondecode(data.aws_secretsmanager_secret_version.rds.secret_string)
  rds_host        = "staging-shared-mysql-proxy.proxy-cfacertspqbw.ap-northeast-2.rds.amazonaws.com"
  rds_port        = "3306"
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
