# ========================================
# ECS Service: legacy-admin-api (Staging)
# ========================================
# Staging environment for Legacy Partner Admin
# Domain: stage-commerce-admin.set-of.com
# Port: 8089
# ========================================

# ========================================
# Common Tags
# ========================================
locals {
  common_tags = {
    environment  = var.environment
    service_name = "${var.project_name}-legacy-admin"
    team         = "platform-team"
    owner        = "platform@ryuqqq.com"
    cost_center  = "engineering"
    project      = var.project_name
    data_class   = "internal"
  }
}

# ========================================
# ECR Repository Reference
# ========================================
data "aws_ecr_repository" "legacy_admin" {
  name = "${var.project_name}-legacy-api-admin-prod"
}

# ========================================
# Staging RDS Security Group Reference
# ========================================
data "aws_db_instance" "staging_rds" {
  db_instance_identifier = "staging-shared-mysql"
}

# Add inbound rule to staging RDS security group for legacy-admin ECS access
resource "aws_security_group_rule" "legacy_admin_to_rds" {
  type                     = "ingress"
  from_port                = 3306
  to_port                  = 3306
  protocol                 = "tcp"
  security_group_id        = tolist(data.aws_db_instance.staging_rds.vpc_security_groups)[0]
  source_security_group_id = module.ecs_security_group.security_group_id
  description              = "Allow legacy-admin-staging ECS to access staging RDS"
}

# ========================================
# ECS Cluster Reference (Staging Cluster)
# ========================================
data "aws_ecs_cluster" "staging" {
  cluster_name = "${var.project_name}-cluster-stage"  # 실제 클러스터명은 stage
}

data "aws_caller_identity" "current" {}

data "aws_vpc" "main" {
  id = local.vpc_id
}

# ========================================
# KMS Key for CloudWatch Logs
# ========================================
resource "aws_kms_key" "logs" {
  description             = "KMS key for SetOf Commerce legacy-admin staging CloudWatch logs"
  deletion_window_in_days = 30
  enable_key_rotation     = true

  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Sid    = "Enable IAM User Permissions"
        Effect = "Allow"
        Principal = {
          AWS = "arn:aws:iam::${data.aws_caller_identity.current.account_id}:root"
        }
        Action   = "kms:*"
        Resource = "*"
      },
      {
        Sid    = "Allow CloudWatch Logs"
        Effect = "Allow"
        Principal = {
          Service = "logs.${var.aws_region}.amazonaws.com"
        }
        Action = [
          "kms:Encrypt*",
          "kms:Decrypt*",
          "kms:ReEncrypt*",
          "kms:GenerateDataKey*",
          "kms:Describe*"
        ]
        Resource = "*"
        Condition = {
          ArnLike = {
            "kms:EncryptionContext:aws:logs:arn" = "arn:aws:logs:${var.aws_region}:${data.aws_caller_identity.current.account_id}:log-group:/aws/ecs/${var.project_name}-legacy-admin-${var.environment}/*"
          }
        }
      }
    ]
  })

  tags = merge(local.common_tags, {
    Name = "${var.project_name}-legacy-admin-logs-kms-${var.environment}"
  })
}

resource "aws_kms_alias" "logs" {
  name          = "alias/${var.project_name}-legacy-admin-logs-${var.environment}"
  target_key_id = aws_kms_key.logs.key_id
}

# ========================================
# CloudWatch Log Group
# ========================================
module "legacy_admin_logs" {
  source = "git::https://github.com/ryu-qqq/Infrastructure.git//terraform/modules/cloudwatch-log-group?ref=main"

  name              = "/aws/ecs/${var.project_name}-legacy-admin-${var.environment}/application"
  retention_in_days = 14
  kms_key_id        = aws_kms_key.logs.arn

  environment  = local.common_tags.environment
  service_name = local.common_tags.service_name
  team         = local.common_tags.team
  owner        = local.common_tags.owner
  cost_center  = local.common_tags.cost_center
  project      = local.common_tags.project
  data_class   = local.common_tags.data_class
}

# ========================================
# Security Groups
# ========================================
resource "aws_security_group" "alb" {
  name        = "${var.project_name}-legacy-admin-alb-sg-${var.environment}"
  description = "Security group for Legacy Admin Staging ALB"
  vpc_id      = local.vpc_id

  ingress {
    from_port   = 443
    to_port     = 443
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
    description = "HTTPS from anywhere"
  }

  ingress {
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
    description = "HTTP for redirect"
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = merge(local.common_tags, {
    Name = "${var.project_name}-legacy-admin-alb-sg-${var.environment}"
  })
}

module "ecs_security_group" {
  source = "git::https://github.com/ryu-qqq/Infrastructure.git//terraform/modules/security-group?ref=main"

  name        = "${var.project_name}-legacy-admin-sg-${var.environment}"
  description = "Security group for legacy-admin staging ECS tasks"
  vpc_id      = local.vpc_id

  type = "custom"

  custom_ingress_rules = [
    {
      from_port                = 8089
      to_port                  = 8089
      protocol                 = "tcp"
      source_security_group_id = aws_security_group.alb.id
      description              = "From ALB only"
    },
    {
      from_port   = 8089
      to_port     = 8089
      protocol    = "tcp"
      cidr_block  = data.aws_vpc.main.cidr_block
      description = "Service Discovery - internal VPC communication"
    }
  ]

  environment  = local.common_tags.environment
  service_name = local.common_tags.service_name
  team         = local.common_tags.team
  owner        = local.common_tags.owner
  cost_center  = local.common_tags.cost_center
  project      = local.common_tags.project
  data_class   = local.common_tags.data_class
}

# ========================================
# Application Load Balancer
# ========================================
resource "aws_lb" "legacy_admin" {
  name               = "setof-lgc-admin-alb-${var.environment}"
  internal           = false
  load_balancer_type = "application"
  security_groups    = [aws_security_group.alb.id]
  subnets            = local.public_subnets

  enable_deletion_protection = false

  tags = merge(local.common_tags, {
    Name = "${var.project_name}-legacy-admin-alb-${var.environment}"
  })
}

resource "aws_lb_target_group" "legacy_admin" {
  name        = "setof-lgc-admin-tg-${var.environment}"
  port        = 8089
  protocol    = "HTTP"
  vpc_id      = local.vpc_id
  target_type = "ip"

  health_check {
    enabled             = true
    healthy_threshold   = 2
    unhealthy_threshold = 3
    timeout             = 5
    interval            = 30
    path                = "/actuator/health"
    matcher             = "200"
  }

  tags = merge(local.common_tags, {
    Name = "${var.project_name}-legacy-admin-tg-${var.environment}"
  })
}

resource "aws_lb_listener" "https" {
  load_balancer_arn = aws_lb.legacy_admin.arn
  port              = "443"
  protocol          = "HTTPS"
  ssl_policy        = "ELBSecurityPolicy-TLS13-1-2-2021-06"
  certificate_arn   = local.certificate_arn

  default_action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.legacy_admin.arn
  }
}

resource "aws_lb_listener" "http" {
  load_balancer_arn = aws_lb.legacy_admin.arn
  port              = "80"
  protocol          = "HTTP"

  default_action {
    type = "redirect"

    redirect {
      port        = "443"
      protocol    = "HTTPS"
      status_code = "HTTP_301"
    }
  }
}

# ========================================
# Route53 DNS Record
# ========================================
resource "aws_route53_record" "legacy_admin" {
  zone_id = local.route53_zone_id
  name    = local.fqdn
  type    = "A"

  alias {
    name                   = aws_lb.legacy_admin.dns_name
    zone_id                = aws_lb.legacy_admin.zone_id
    evaluate_target_health = true
  }
}

# ========================================
# IAM Roles
# ========================================
module "legacy_admin_task_execution_role" {
  source = "git::https://github.com/ryu-qqq/Infrastructure.git//terraform/modules/iam-role-policy?ref=main"

  role_name = "${var.project_name}-legacy-admin-execution-role-${var.environment}"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = "sts:AssumeRole"
        Effect = "Allow"
        Principal = {
          Service = "ecs-tasks.amazonaws.com"
        }
      }
    ]
  })

  attach_aws_managed_policies = [
    "arn:aws:iam::aws:policy/service-role/AmazonECSTaskExecutionRolePolicy"
  ]

  enable_secrets_manager_policy = true
  secrets_manager_secret_arns = [
    data.aws_secretsmanager_secret.rds_staging.arn,
    data.aws_secretsmanager_secret.legacy.arn
  ]

  custom_inline_policies = {
    ssm-access = {
      policy = jsonencode({
        Version = "2012-10-17"
        Statement = [
          {
            Effect = "Allow"
            Action = [
              "ssm:GetParameters",
              "ssm:GetParameter"
            ]
            Resource = [
              "arn:aws:ssm:${var.aws_region}:*:parameter/shared/*",
              "arn:aws:ssm:${var.aws_region}:*:parameter/${var.project_name}/*"
            ]
          },
          {
            Effect = "Allow"
            Action = [
              "kms:Decrypt"
            ]
            Resource = [
              aws_kms_key.logs.arn
            ]
          }
        ]
      })
    }
  }

  environment  = local.common_tags.environment
  service_name = local.common_tags.service_name
  team         = local.common_tags.team
  owner        = local.common_tags.owner
  cost_center  = local.common_tags.cost_center
  project      = local.common_tags.project
  data_class   = local.common_tags.data_class
}

module "legacy_admin_task_role" {
  source = "git::https://github.com/ryu-qqq/Infrastructure.git//terraform/modules/iam-role-policy?ref=main"

  role_name = "${var.project_name}-legacy-admin-task-role-${var.environment}"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = "sts:AssumeRole"
        Effect = "Allow"
        Principal = {
          Service = "ecs-tasks.amazonaws.com"
        }
      }
    ]
  })

  custom_inline_policies = {
    legacy-admin-staging-access = {
      policy = jsonencode({
        Version = "2012-10-17"
        Statement = [
          {
            Sid    = "CloudWatchLogsAccess"
            Effect = "Allow"
            Action = [
              "logs:CreateLogGroup",
              "logs:CreateLogStream",
              "logs:PutLogEvents",
              "logs:DescribeLogStreams"
            ]
            Resource = "*"
          },
          {
            Sid    = "S3Access"
            Effect = "Allow"
            Action = [
              "s3:GetObject",
              "s3:PutObject",
              "s3:DeleteObject"
            ]
            Resource = [
              "arn:aws:s3:::set-of.net/*",
              "arn:aws:s3:::temp-set-of.net/*"
            ]
          },
          {
            Sid    = "S3ListBucket"
            Effect = "Allow"
            Action = [
              "s3:ListBucket"
            ]
            Resource = [
              "arn:aws:s3:::set-of.net",
              "arn:aws:s3:::temp-set-of.net"
            ]
          },
          {
            Sid    = "SQSAccess"
            Effect = "Allow"
            Action = [
              "sqs:SendMessage",
              "sqs:ReceiveMessage",
              "sqs:DeleteMessage",
              "sqs:GetQueueAttributes"
            ]
            Resource = "arn:aws:sqs:${var.aws_region}:*:*"
          }
        ]
      })
    }
  }

  environment  = local.common_tags.environment
  service_name = local.common_tags.service_name
  team         = local.common_tags.team
  owner        = local.common_tags.owner
  cost_center  = local.common_tags.cost_center
  project      = local.common_tags.project
  data_class   = local.common_tags.data_class
}

# ========================================
# ECS Service
# ========================================
module "ecs_service" {
  source = "git::https://github.com/ryu-qqq/Infrastructure.git//terraform/modules/ecs-service?ref=main"

  name            = "${var.project_name}-legacy-admin-${var.environment}"
  cluster_id      = data.aws_ecs_cluster.staging.arn
  container_name  = "legacy-admin"
  container_image = "${data.aws_ecr_repository.legacy_admin.repository_url}:${var.image_tag}"
  container_port  = 8089
  cpu             = var.legacy_admin_cpu
  memory          = var.legacy_admin_memory
  desired_count   = var.legacy_admin_desired_count

  execution_role_arn = module.legacy_admin_task_execution_role.role_arn
  task_role_arn      = module.legacy_admin_task_role.role_arn

  subnet_ids         = local.private_subnets
  security_group_ids = [module.ecs_security_group.security_group_id]
  assign_public_ip   = false

  load_balancer_config = {
    target_group_arn = aws_lb_target_group.legacy_admin.arn
    container_name   = "legacy-admin"
    container_port   = 8089
  }

  health_check_grace_period_seconds = 180

  container_environment = [
    { name = "SPRING_PROFILES_ACTIVE", value = "prod" },
    { name = "DB_HOST", value = local.rds_host },
    { name = "DB_PORT", value = local.rds_port },
    { name = "DB_NAME", value = local.rds_dbname },
    { name = "REDIS_HOST", value = local.redis_host },
    { name = "REDIS_PORT", value = tostring(local.redis_port) },
    { name = "REDIS_DATABASE", value = "0" },
    { name = "CORE_SERVER_URL", value = "https://stage-core-server.set-of.net" },
    { name = "SECURITY_SERVICE_TOKEN_ENABLED", value = "true" },
    { name = "SEWON_URL", value = "http://api.sellic.co.kr" },
    { name = "SEWON_CUSTOMER_ID", value = "1012" },
    { name = "OCO_URL", value = "http://localhost:9998" }
  ]

  container_secrets = [
    { name = "DB_USERNAME", valueFrom = "${data.aws_secretsmanager_secret.rds_staging.arn}:username::" },
    { name = "DB_PASSWORD", valueFrom = "${data.aws_secretsmanager_secret.rds_staging.arn}:password::" },
    { name = "JWT_SECRET", valueFrom = "${data.aws_secretsmanager_secret.legacy.arn}:jwt_secret::" },
    { name = "PORTONE_API_KEY", valueFrom = "${data.aws_secretsmanager_secret.legacy.arn}:portone_api_key::" },
    { name = "PORTONE_API_SECRET", valueFrom = "${data.aws_secretsmanager_secret.legacy.arn}:portone_api_secret::" },
    { name = "SLACK_TOKEN", valueFrom = "${data.aws_secretsmanager_secret.legacy.arn}:slack_token::" },
    { name = "AWS_ACCESS_KEY", valueFrom = "${data.aws_secretsmanager_secret.legacy.arn}:aws_access_key::" },
    { name = "AWS_SECRET_KEY", valueFrom = "${data.aws_secretsmanager_secret.legacy.arn}:aws_secret_key::" },
    { name = "SECURITY_SERVICE_TOKEN_SECRET", valueFrom = data.aws_ssm_parameter.service_token_secret.arn },
    { name = "SEWON_API_KEY", valueFrom = "${data.aws_secretsmanager_secret.legacy.arn}:sellic_api_key::" }
  ]

  health_check_command      = ["CMD-SHELL", "wget --no-verbose --tries=1 --spider http://localhost:8089/actuator/health || exit 1"]
  health_check_interval     = 30
  health_check_timeout      = 5
  health_check_retries      = 3
  health_check_start_period = 120

  log_configuration = {
    log_driver = "awslogs"
    options = {
      "awslogs-group"         = module.legacy_admin_logs.log_group_name
      "awslogs-region"        = var.aws_region
      "awslogs-stream-prefix" = "legacy-admin"
    }
  }

  enable_autoscaling        = false
  enable_execute_command    = true

  deployment_maximum_percent         = 200
  deployment_minimum_healthy_percent = 100
  deployment_circuit_breaker_enable   = true
  deployment_circuit_breaker_rollback = true

  enable_service_discovery         = true
  service_discovery_namespace_id   = data.aws_ssm_parameter.service_discovery_namespace_id.value
  service_discovery_namespace_name = "connectly.local"

  environment  = local.common_tags.environment
  service_name = local.common_tags.service_name
  team         = local.common_tags.team
  owner        = local.common_tags.owner
  cost_center  = local.common_tags.cost_center
  project      = local.common_tags.project
  data_class   = local.common_tags.data_class
}

# ========================================
# Outputs
# ========================================
output "alb_dns_name" {
  description = "ALB DNS name"
  value       = aws_lb.legacy_admin.dns_name
}

output "service_url" {
  description = "Service URL"
  value       = "https://${local.fqdn}"
}

output "ecs_service_name" {
  description = "ECS Service name"
  value       = module.ecs_service.service_name
}
