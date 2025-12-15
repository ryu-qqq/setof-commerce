# ========================================
# ECS web-api Outputs
# ========================================
# Service Discovery Only (No ALB)
# Access via: web-api.connectly.local:8080
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
  value       = "web-api.connectly.local"
}

output "url" {
  description = "Internal Service URL (via Gateway/Service Discovery)"
  value       = "http://web-api.connectly.local:8080"
}

output "log_group_name" {
  description = "CloudWatch log group name"
  value       = module.web_api_logs.log_group_name
}

output "kms_key_arn" {
  description = "KMS key ARN for logs encryption"
  value       = aws_kms_key.logs.arn
}
