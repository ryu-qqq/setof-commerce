# ========================================
# ECR Outputs
# ========================================

output "web_api_repository_url" {
  description = "ECR repository URL for web-api"
  value       = module.ecr_web_api.repository_url
}

output "web_api_repository_arn" {
  description = "ECR repository ARN for web-api"
  value       = module.ecr_web_api.repository_arn
}

output "web_api_admin_repository_url" {
  description = "ECR repository URL for web-api-admin"
  value       = module.ecr_web_api_admin.repository_url
}

output "web_api_admin_repository_arn" {
  description = "ECR repository ARN for web-api-admin"
  value       = module.ecr_web_api_admin.repository_arn
}

output "legacy_api_repository_url" {
  description = "ECR repository URL for legacy-api"
  value       = module.ecr_legacy_api.repository_url
}

output "legacy_api_repository_arn" {
  description = "ECR repository ARN for legacy-api"
  value       = module.ecr_legacy_api.repository_arn
}
