# ========================================
# ECR Repository Outputs (Stage)
# ========================================

output "ecr_web_api_url" {
  description = "ECR repository URL for web-api (Stage)"
  value       = module.ecr_web_api.repository_url
}

output "ecr_web_api_admin_url" {
  description = "ECR repository URL for web-api-admin (Stage)"
  value       = module.ecr_web_api_admin.repository_url
}

output "ecr_legacy_api_url" {
  description = "ECR repository URL for legacy-api (Stage)"
  value       = module.ecr_legacy_api.repository_url
}

output "ecr_legacy_api_admin_url" {
  description = "ECR repository URL for legacy-api-admin (Stage)"
  value       = module.ecr_legacy_api_admin.repository_url
}

output "ecr_legacy_batch_url" {
  description = "ECR repository URL for legacy-batch (Stage)"
  value       = module.ecr_legacy_batch.repository_url
}

output "ecr_migration_url" {
  description = "ECR repository URL for migration (Stage)"
  value       = module.ecr_migration.repository_url
}

# ========================================
# Repository Names
# ========================================
output "ecr_repository_names" {
  description = "All ECR repository names for Stage environment"
  value = {
    web_api           = "${var.project_name}-web-api-${var.environment}"
    web_api_admin     = "${var.project_name}-web-api-admin-${var.environment}"
    legacy_api        = "${var.project_name}-legacy-api-${var.environment}"
    legacy_api_admin  = "${var.project_name}-legacy-api-admin-${var.environment}"
    legacy_batch      = "${var.project_name}-legacy-batch-${var.environment}"
    migration         = "${var.project_name}-migration-${var.environment}"
  }
}
