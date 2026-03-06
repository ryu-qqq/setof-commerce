# ========================================
# Terraform Provider Configuration
# ========================================
# Shadow Traffic - Legacy vs New API comparison (Stage)
# EventBridge Scheduled Task (Python)
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
    key            = "setof-commerce/ecs-shadow-traffic-stage/terraform.tfstate"
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
# Variables
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

variable "task_cpu" {
  description = "CPU units for shadow-traffic task"
  type        = number
  default     = 256
}

variable "task_memory" {
  description = "Memory for shadow-traffic task"
  type        = number
  default     = 512
}

variable "image_tag" {
  description = "Docker image tag to deploy"
  type        = string
  default     = "latest"
}

variable "schedule_expression" {
  description = "EventBridge schedule expression"
  type        = string
  default     = "rate(1 minute)"
}

variable "enable_schedule" {
  description = "Enable EventBridge schedule"
  type        = bool
  default     = true
}

variable "slack_webhook_url" {
  description = "Slack webhook URL for diff alerts"
  type        = string
  default     = ""
}

variable "dashboard_url" {
  description = "Grafana dashboard URL for alert links"
  type        = string
  default     = ""
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

data "aws_caller_identity" "current" {}

# ========================================
# Locals
# ========================================
locals {
  vpc_id          = data.aws_ssm_parameter.vpc_id.value
  private_subnets = split(",", data.aws_ssm_parameter.private_subnets.value)

  # Legacy API (Stage) - ECS Service Discovery DNS
  legacy_api_url = "http://setof-commerce-legacy-api-stage.connectly.local:8088"
  # New API (Stage) - ECS Service Discovery DNS
  new_api_url = "http://setof-commerce-web-api-stage.connectly.local:8080"
}
