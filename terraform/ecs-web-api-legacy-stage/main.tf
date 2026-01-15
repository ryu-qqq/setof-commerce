# ========================================
# ECS Service: legacy-web-api (Stage)
# ========================================
# Strangler Fig Pattern: Legacy server on ECS
# REST API server with ALB and Auto Scaling
# Using Infrastructure modules
# Domain: stage.set-of.com
# Port: 8088
# ========================================

# ========================================
# Common Tags (for governance)
# ========================================
locals {
  common_tags = {
    environment  = var.environment
    service_name = "${var.project_name}-legacy-api"
    team         = "platform-team"
    owner        = "platform@ryuqqq.com"
    cost_center  = "engineering"
    project      = var.project_name
    data_class   = "internal"
  }
}

# ========================================
# ECR Repository Reference (Same as prod)
# ========================================
data "aws_ecr_repository" "legacy_api" {
  name = "${var.project_name}-legacy-api-prod"
}

# ========================================
# Staging RDS Security Group Reference
# ========================================
data "aws_db_instance" "staging_rds" {
  db_instance_identifier = "staging-shared-mysql"
}

# Add inbound rule to staging RDS security group for legacy-api ECS access
resource "aws_security_group_rule" "legacy_api_to_rds" {
  type                     = "ingress"
  from_port                = 3306
  to_port                  = 3306
  protocol                 = "tcp"
  security_group_id        = tolist(data.aws_db_instance.staging_rds.vpc_security_groups)[0]
  source_security_group_id = module.ecs_security_group.security_group_id
  description              = "Allow legacy-api-staging ECS to access staging RDS"
}

# ========================================
# ECS Cluster Reference (from ecs-cluster-stage)
# ========================================
data "aws_ssm_parameter" "cluster_name" {
  name = "/${var.project_name}/ecs-stage/cluster-name"
}

data "aws_ecs_cluster" "main" {
  cluster_name = data.aws_ssm_parameter.cluster_name.value
}

data "aws_caller_identity" "current" {}

# ========================================
# Service Discovery Namespace (from shared infrastructure)
# ========================================
data "aws_ssm_parameter" "service_discovery_namespace_id" {
  name = "/shared/service-discovery/namespace-id"
}

# VPC data source for internal communication
data "aws_vpc" "main" {
  id = local.vpc_id
}

# ========================================
# KMS Key for CloudWatch Logs Encryption
# ========================================
resource "aws_kms_key" "logs" {
  description             = "KMS key for SetOf Commerce legacy-api-stage CloudWatch logs encryption"
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
            "kms:EncryptionContext:aws:logs:arn" = "arn:aws:logs:${var.aws_region}:${data.aws_caller_identity.current.account_id}:log-group:/aws/ecs/${var.project_name}-legacy-api-${var.environment}/*"
          }
        }
      }
    ]
  })

  tags = merge(local.common_tags, {
    Name      = "${var.project_name}-legacy-api-logs-kms-${var.environment}"
    Lifecycle = "staging"
    ManagedBy = "terraform"
  })
}

resource "aws_kms_alias" "logs" {
  name          = "alias/${var.project_name}-legacy-api-logs-${var.environment}"
  target_key_id = aws_kms_key.logs.key_id
}

# ========================================
# CloudWatch Log Group (using Infrastructure module)
# ========================================
module "legacy_api_logs" {
  source = "git::https://github.com/ryu-qqq/Infrastructure.git//terraform/modules/cloudwatch-log-group?ref=main"

  name              = "/aws/ecs/${var.project_name}-legacy-api-${var.environment}/application"
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
  name        = "${var.project_name}-legacy-alb-sg-${var.environment}"
  description = "Security group for Legacy ALB (Stage)"
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
    Name      = "${var.project_name}-legacy-alb-sg-${var.environment}"
    Lifecycle = "staging"
    ManagedBy = "terraform"
  })
}

module "ecs_security_group" {
  source = "git::https://github.com/ryu-qqq/Infrastructure.git//terraform/modules/security-group?ref=main"

  name        = "${var.project_name}-legacy-api-sg-${var.environment}"
  description = "Security group for legacy-api ECS tasks (Stage)"
  vpc_id      = local.vpc_id

  type = "custom"

  custom_ingress_rules = [
    {
      from_port                = 8088
      to_port                  = 8088
      protocol                 = "tcp"
      source_security_group_id = aws_security_group.alb.id
      description              = "From ALB only"
    },
    {
      from_port   = 8088
      to_port     = 8088
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
resource "aws_lb" "legacy_api" {
  name               = "${var.project_name}-leg-alb-stg"
  internal           = false
  load_balancer_type = "application"
  security_groups    = [aws_security_group.alb.id]
  subnets            = local.public_subnets

  enable_deletion_protection = false

  tags = merge(local.common_tags, {
    Name      = "${var.project_name}-legacy-alb-stage"
    Lifecycle = "staging"
    ManagedBy = "terraform"
  })
}

# Target Group (Port 8088)
resource "aws_lb_target_group" "legacy_api" {
  name        = "${var.project_name}-legacy-tg-${var.environment}"
  port        = 8088
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
    Name = "${var.project_name}-legacy-tg-${var.environment}"
  })
}

# HTTPS Listener
resource "aws_lb_listener" "https" {
  load_balancer_arn = aws_lb.legacy_api.arn
  port              = "443"
  protocol          = "HTTPS"
  ssl_policy        = "ELBSecurityPolicy-TLS13-1-2-2021-06"
  certificate_arn   = local.certificate_arn

  default_action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.legacy_api.arn
  }
}

# HTTP to HTTPS Redirect
resource "aws_lb_listener" "http" {
  load_balancer_arn = aws_lb.legacy_api.arn
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
resource "aws_route53_record" "legacy_api" {
  zone_id = local.route53_zone_id
  name    = local.fqdn
  type    = "A"

  alias {
    name                   = aws_lb.legacy_api.dns_name
    zone_id                = aws_lb.legacy_api.zone_id
    evaluate_target_health = true
  }
}

# ========================================
# IAM Roles (using Infrastructure module)
# ========================================

# ECS Task Execution Role
module "legacy_api_task_execution_role" {
  source = "git::https://github.com/ryu-qqq/Infrastructure.git//terraform/modules/iam-role-policy?ref=main"

  role_name = "${var.project_name}-legacy-api-execution-role-${var.environment}"

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
    data.aws_secretsmanager_secret.rds.arn,
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

# ECS Task Role
module "legacy_api_task_role" {
  source = "git::https://github.com/ryu-qqq/Infrastructure.git//terraform/modules/iam-role-policy?ref=main"

  role_name = "${var.project_name}-legacy-api-task-role-${var.environment}"

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
    legacy-api-access = {
      policy = jsonencode({
        Version = "2012-10-17"
        Statement = [
          {
            Sid    = "AMPRemoteWrite"
            Effect = "Allow"
            Action = [
              "aps:RemoteWrite"
            ]
            Resource = "arn:aws:aps:${var.aws_region}:*:workspace/*"
          },
          {
            Sid    = "XRayTracing"
            Effect = "Allow"
            Action = [
              "xray:PutTraceSegments",
              "xray:PutTelemetryRecords",
              "xray:GetSamplingRules",
              "xray:GetSamplingTargets",
              "xray:GetSamplingStatisticSummaries"
            ]
            Resource = "*"
          },
          {
            Sid    = "CloudWatchLogsAccess"
            Effect = "Allow"
            Action = [
              "logs:CreateLogGroup",
              "logs:CreateLogStream",
              "logs:PutLogEvents",
              "logs:DescribeLogStreams"
            ]
            Resource = [
              "arn:aws:logs:${var.aws_region}:${data.aws_caller_identity.current.account_id}:log-group:/ecs/${var.project_name}/otel:*",
              "arn:aws:logs:${var.aws_region}:${data.aws_caller_identity.current.account_id}:log-group:/ecs/${var.project_name}/otel-collector:*"
            ]
          },
          {
            Sid    = "CloudWatchMetricsAccess"
            Effect = "Allow"
            Action = [
              "cloudwatch:PutMetricData"
            ]
            Resource = "*"
            Condition = {
              StringEquals = {
                "cloudwatch:namespace" = "SetOfCommerce"
              }
            }
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
              "arn:aws:s3:::temp-set-of.net/*",
              "arn:aws:s3:::prod-connectly/otel-config/*"
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
# ADOT Sidecar (using Infrastructure module)
# ========================================
module "adot_sidecar" {
  source = "git::https://github.com/ryu-qqq/Infrastructure.git//terraform/modules/adot-sidecar?ref=main"

  project_name              = var.project_name
  service_name              = "legacy-api"
  aws_region                = var.aws_region
  amp_workspace_arn         = local.amp_workspace_arn
  amp_remote_write_endpoint = local.amp_remote_write_url
  log_group_name            = module.legacy_api_logs.log_group_name
  app_port                  = 8088
  cluster_name              = data.aws_ecs_cluster.main.cluster_name
  environment               = var.environment
  config_bucket             = "prod-connectly"
  config_version            = "20251215"
}

# ========================================
# ECS Service (using Infrastructure module)
# ========================================
module "ecs_service" {
  source = "git::https://github.com/ryu-qqq/Infrastructure.git//terraform/modules/ecs-service?ref=main"

  # Service Configuration
  name            = "${var.project_name}-legacy-api-${var.environment}"
  cluster_id      = data.aws_ecs_cluster.main.arn
  container_name  = "legacy-api"
  container_image = "${data.aws_ecr_repository.legacy_api.repository_url}:${var.image_tag}"
  container_port  = 8088
  cpu             = var.legacy_api_cpu
  memory          = var.legacy_api_memory
  desired_count   = var.legacy_api_desired_count

  # IAM Roles
  execution_role_arn = module.legacy_api_task_execution_role.role_arn
  task_role_arn      = module.legacy_api_task_role.role_arn

  # Network Configuration
  subnet_ids         = local.private_subnets
  security_group_ids = [module.ecs_security_group.security_group_id]
  assign_public_ip   = false

  # Load Balancer Configuration
  load_balancer_config = {
    target_group_arn = aws_lb_target_group.legacy_api.arn
    container_name   = "legacy-api"
    container_port   = 8088
  }

  # Health Check Grace Period (Legacy Spring Boot startup)
  health_check_grace_period_seconds = 180

  # Container Environment Variables
  container_environment = [
    { name = "SPRING_PROFILES_ACTIVE", value = var.environment },
    # Database
    { name = "DB_HOST", value = local.rds_host },
    { name = "DB_PORT", value = local.rds_port },
    { name = "DB_NAME", value = local.rds_dbname },
    { name = "DB_USERNAME", value = local.rds_username },
    # Redis
    { name = "REDIS_HOST", value = local.redis_host },
    { name = "REDIS_PORT", value = tostring(local.redis_port) },
    { name = "REDIS_DATABASE", value = "0" },
    # Kakao OAuth
    { name = "KAKAO_CLIENT_ID", value = "a12a765b27dfd19e5c5fc5a5a88963aa" },
    # Security Service Token
    { name = "SECURITY_SERVICE_TOKEN_ENABLED", value = "true" },
    # Stage Environment: Disable external payment API
    { name = "PORTONE_ENABLED", value = "false" }
  ]

  # Container Secrets (from Secrets Manager and SSM)
  container_secrets = [
    { name = "DB_PASSWORD", valueFrom = "${data.aws_secretsmanager_secret.rds.arn}:password::" },
    { name = "KAKAO_CLIENT_SECRET", valueFrom = "${data.aws_secretsmanager_secret.legacy.arn}:kakao_client_secret::" },
    { name = "JWT_SECRET", valueFrom = "${data.aws_secretsmanager_secret.legacy.arn}:jwt_secret::" },
    { name = "PORTONE_API_KEY", valueFrom = "${data.aws_secretsmanager_secret.legacy.arn}:portone_api_key::" },
    { name = "PORTONE_API_SECRET", valueFrom = "${data.aws_secretsmanager_secret.legacy.arn}:portone_api_secret::" },
    { name = "SLACK_TOKEN", valueFrom = "${data.aws_secretsmanager_secret.legacy.arn}:slack_token::" },
    { name = "AWS_ACCESS_KEY", valueFrom = "${data.aws_secretsmanager_secret.legacy.arn}:aws_access_key::" },
    { name = "AWS_SECRET_KEY", valueFrom = "${data.aws_secretsmanager_secret.legacy.arn}:aws_secret_key::" },
    { name = "SECURITY_SERVICE_TOKEN_SECRET", valueFrom = "arn:aws:ssm:${var.aws_region}:${data.aws_caller_identity.current.account_id}:parameter/shared/security/service-token-secret" }
  ]

  # Health Check
  health_check_command      = ["CMD-SHELL", "wget --no-verbose --tries=1 --spider http://localhost:8088/actuator/health || exit 1"]
  health_check_interval     = 30
  health_check_timeout      = 5
  health_check_retries      = 3
  health_check_start_period = 120

  # Logging
  log_configuration = {
    log_driver = "awslogs"
    options = {
      "awslogs-group"         = module.legacy_api_logs.log_group_name
      "awslogs-region"        = var.aws_region
      "awslogs-stream-prefix" = "legacy-api"
    }
  }

  # ADOT Sidecar
  sidecars = [module.adot_sidecar.container_definition]

  # Auto Scaling (Stage는 최소 리소스)
  enable_autoscaling        = true
  autoscaling_min_capacity  = 1
  autoscaling_max_capacity  = 3
  autoscaling_target_cpu    = 70
  autoscaling_target_memory = 80

  # Enable ECS Exec for debugging
  enable_execute_command = true

  # Deployment Configuration - Zero-Downtime Rolling Update
  deployment_maximum_percent         = 200  # 새 태스크 먼저 시작
  deployment_minimum_healthy_percent = 100  # 최소 100% 유지 (Zero-Downtime: 새 태스크 healthy 후 기존 종료)
  deployment_circuit_breaker_enable   = true
  deployment_circuit_breaker_rollback = true

  # Service Discovery Configuration
  enable_service_discovery         = true
  service_discovery_namespace_id   = data.aws_ssm_parameter.service_discovery_namespace_id.value
  service_discovery_namespace_name = "connectly.local"

  # Tagging
  environment  = local.common_tags.environment
  service_name = local.common_tags.service_name
  team         = local.common_tags.team
  owner        = local.common_tags.owner
  cost_center  = local.common_tags.cost_center
  project      = local.common_tags.project
  data_class   = local.common_tags.data_class
}
