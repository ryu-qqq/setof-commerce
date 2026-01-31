# ========================================
# ECS web-api-admin Outputs
# ========================================
# Service Discovery only - No ALB (Gateway Pattern)
# Access via: http://web-api-admin.connectly.local:8081
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

output "service_discovery_endpoint" {
  description = "Service Discovery endpoint (internal VPC only)"
  value       = "web-api-admin.connectly.local:8081"
}

output "log_group_name" {
  description = "CloudWatch log group name"
  value       = module.web_api_admin_logs.log_group_name
}

output "kms_key_arn" {
  description = "KMS key ARN for logs encryption"
  value       = aws_kms_key.logs.arn
}
