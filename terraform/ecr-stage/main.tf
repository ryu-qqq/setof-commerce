# ============================================================================
# SetOf Commerce - Stage ECR Repositories
# ============================================================================
# Stage 환경용 ECR 레포지토리
#
# 레포지토리 목록:
# - setof-commerce-legacy-api-stage: Legacy API (Stage)
# - setof-commerce-legacy-api-admin-stage: Legacy Admin API (Stage)
# - setof-commerce-legacy-batch-stage: Legacy Batch Jobs (Stage)
# ============================================================================

# ========================================
# Common Tags (for governance)
# ========================================
locals {
  common_tags = {
    environment  = var.environment
    service_name = "${var.project_name}-ecr"
    team         = "platform-team"
    owner        = "platform@ryuqqq.com"
    cost_center  = "engineering"
    project      = var.project_name
    data_class   = "internal"
  }
}

# ========================================
# ECR Module - Legacy API (Stage)
# ========================================
module "ecr_legacy_api_stage" {
  source = "git::https://github.com/ryu-qqq/Infrastructure.git//modules/ecr?ref=main"

  repository_name = "${var.project_name}-legacy-api-${var.environment}"

  governance_tags = {
    environment  = local.common_tags.environment
    service_name = "legacy-api"
    team         = local.common_tags.team
    owner        = local.common_tags.owner
    cost_center  = local.common_tags.cost_center
    project      = local.common_tags.project
    data_class   = local.common_tags.data_class
  }

  lifecycle_policy_rules = [
    {
      rulePriority = 1
      description  = "Keep last 10 images for stage environment"
      selection = {
        tagStatus   = "any"
        countType   = "imageCountMoreThan"
        countNumber = 10
      }
      action = {
        type = "expire"
      }
    }
  ]
}

# ========================================
# ECR Module - Legacy Admin API (Stage)
# ========================================
module "ecr_legacy_api_admin_stage" {
  source = "git::https://github.com/ryu-qqq/Infrastructure.git//modules/ecr?ref=main"

  repository_name = "${var.project_name}-legacy-api-admin-${var.environment}"

  governance_tags = {
    environment  = local.common_tags.environment
    service_name = "legacy-api-admin"
    team         = local.common_tags.team
    owner        = local.common_tags.owner
    cost_center  = local.common_tags.cost_center
    project      = local.common_tags.project
    data_class   = local.common_tags.data_class
  }

  lifecycle_policy_rules = [
    {
      rulePriority = 1
      description  = "Keep last 10 images for stage environment"
      selection = {
        tagStatus   = "any"
        countType   = "imageCountMoreThan"
        countNumber = 10
      }
      action = {
        type = "expire"
      }
    }
  ]
}

# ========================================
# ECR Module - Legacy Batch (Stage)
# ========================================
module "ecr_legacy_batch_stage" {
  source = "git::https://github.com/ryu-qqq/Infrastructure.git//modules/ecr?ref=main"

  repository_name = "${var.project_name}-legacy-batch-${var.environment}"

  governance_tags = {
    environment  = local.common_tags.environment
    service_name = "legacy-batch"
    team         = local.common_tags.team
    owner        = local.common_tags.owner
    cost_center  = local.common_tags.cost_center
    project      = local.common_tags.project
    data_class   = local.common_tags.data_class
  }

  lifecycle_policy_rules = [
    {
      rulePriority = 1
      description  = "Keep last 10 images for stage environment"
      selection = {
        tagStatus   = "any"
        countType   = "imageCountMoreThan"
        countNumber = 10
      }
      action = {
        type = "expire"
      }
    }
  ]
}

# ========================================
# Outputs
# ========================================
output "legacy_api_repository_url" {
  description = "Legacy API ECR repository URL (Stage)"
  value       = module.ecr_legacy_api_stage.repository_url
}

output "legacy_api_admin_repository_url" {
  description = "Legacy Admin API ECR repository URL (Stage)"
  value       = module.ecr_legacy_api_admin_stage.repository_url
}

output "legacy_batch_repository_url" {
  description = "Legacy Batch ECR repository URL (Stage)"
  value       = module.ecr_legacy_batch_stage.repository_url
}

output "legacy_api_repository_arn" {
  description = "Legacy API ECR repository ARN (Stage)"
  value       = module.ecr_legacy_api_stage.repository_arn
}

output "legacy_api_admin_repository_arn" {
  description = "Legacy Admin API ECR repository ARN (Stage)"
  value       = module.ecr_legacy_api_admin_stage.repository_arn
}

output "legacy_batch_repository_arn" {
  description = "Legacy Batch ECR repository ARN (Stage)"
  value       = module.ecr_legacy_batch_stage.repository_arn
}
