# ========================================
# Outputs: legacy-batch ECS Task Definition
# ========================================
# Note: Service 없음 (RunTask 전용, 비용 효율적)
# ========================================

output "task_definition_arn" {
  description = "Task definition ARN"
  value       = aws_ecs_task_definition.legacy_batch.arn
}

output "task_definition_family" {
  description = "Task definition family name"
  value       = aws_ecs_task_definition.legacy_batch.family
}

output "security_group_id" {
  description = "Security group ID for batch tasks"
  value       = module.ecs_security_group.security_group_id
}

output "log_group_name" {
  description = "CloudWatch log group name"
  value       = module.legacy_batch_logs.log_group_name
}

output "eventbridge_schedules" {
  description = "EventBridge schedule names"
  value = {
    shipment_tracking = aws_scheduler_schedule.shipment_tracking.name
    sellic_sync       = aws_scheduler_schedule.sellic_sync.name
    alimtalk_notify   = aws_scheduler_schedule.alimtalk_notify.name
    order_completed   = aws_scheduler_schedule.order_completed.name
    order_settlement  = aws_scheduler_schedule.order_settlement.name
  }
}

output "ecr_repository_url" {
  description = "ECR repository URL"
  value       = data.aws_ecr_repository.legacy_batch.repository_url
}

output "eventbridge_role_arn" {
  description = "EventBridge IAM role ARN for ECS RunTask"
  value       = aws_iam_role.eventbridge_batch.arn
}
