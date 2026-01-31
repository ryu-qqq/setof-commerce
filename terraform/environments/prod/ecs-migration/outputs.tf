# ========================================
# ECS migration Outputs
# ========================================
# Service Discovery Only (Internal VPC)
# Access via: migration.connectly.local:8082
# ========================================

output "service_name" {
  description = "ECS service name"
  value       = module.ecs_service.service_name
}

output "service_arn" {
  description = "ECS service ARN"
  value       = module.ecs_service.service_id
}

output "task_definition_arn" {
  description = "Task definition ARN"
  value       = module.ecs_service.task_definition_arn
}

output "fqdn" {
  description = "Service Discovery FQDN (Cloud Map)"
  value       = "migration.connectly.local"
}

output "url" {
  description = "Internal Service URL (via Service Discovery)"
  value       = "http://migration.connectly.local:8082"
}

output "log_group_name" {
  description = "CloudWatch log group name"
  value       = module.migration_logs.log_group_name
}

output "kms_key_arn" {
  description = "KMS key ARN for logs encryption"
  value       = aws_kms_key.logs.arn
}
