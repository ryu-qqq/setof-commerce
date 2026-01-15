# ========================================
# ECR Repositories for SetOf Commerce (Stage)
# ========================================
# Container registries using Infrastructure module
# - web-api: REST API server (Spring Boot)
# - web-api-admin: Admin API server (Spring Boot)
# - legacy-api: Legacy API server (Spring Boot)
# - legacy-api-admin: Legacy Admin API server (Spring Boot)
# - legacy-batch: Legacy batch jobs (Spring Batch)
# - migration: Data migration server (Spring Boot)
# ========================================

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
# ECR Repository: web-api (Stage)
# ========================================
module "ecr_web_api" {
  source = "git::https://github.com/ryu-qqq/Infrastructure.git//terraform/modules/ecr?ref=main"

  name                 = "${var.project_name}-web-api-${var.environment}"
  image_tag_mutability = "MUTABLE"  # Stage에서는 MUTABLE 허용 (빠른 반복)
  scan_on_push         = true
  encryption_type      = "AES256"

  # Lifecycle Policy (Stage는 더 짧은 보존)
  enable_lifecycle_policy    = true
  max_image_count            = 15
  lifecycle_tag_prefixes     = ["v", "stage", "latest"]
  untagged_image_expiry_days = 3

  # SSM Parameter 비활성화
  create_ssm_parameter = false

  # Required Tags (governance compliance)
  environment  = local.common_tags.environment
  service_name = "${var.project_name}-web-api"
  team         = local.common_tags.team
  owner        = local.common_tags.owner
  cost_center  = local.common_tags.cost_center
  project      = local.common_tags.project
  data_class   = local.common_tags.data_class
}

# ========================================
# ECR Repository: web-api-admin (Stage)
# ========================================
module "ecr_web_api_admin" {
  source = "git::https://github.com/ryu-qqq/Infrastructure.git//terraform/modules/ecr?ref=main"

  name                 = "${var.project_name}-web-api-admin-${var.environment}"
  image_tag_mutability = "MUTABLE"
  scan_on_push         = true
  encryption_type      = "AES256"

  # Lifecycle Policy
  enable_lifecycle_policy    = true
  max_image_count            = 15
  lifecycle_tag_prefixes     = ["v", "stage", "latest"]
  untagged_image_expiry_days = 3

  # SSM Parameter 비활성화
  create_ssm_parameter = false

  # Required Tags (governance compliance)
  environment  = local.common_tags.environment
  service_name = "${var.project_name}-web-api-admin"
  team         = local.common_tags.team
  owner        = local.common_tags.owner
  cost_center  = local.common_tags.cost_center
  project      = local.common_tags.project
  data_class   = local.common_tags.data_class
}

# ========================================
# ECR Repository: legacy-api (Stage)
# ========================================
module "ecr_legacy_api" {
  source = "git::https://github.com/ryu-qqq/Infrastructure.git//terraform/modules/ecr?ref=main"

  name                 = "${var.project_name}-legacy-api-${var.environment}"
  image_tag_mutability = "MUTABLE"
  scan_on_push         = true
  encryption_type      = "AES256"

  # Lifecycle Policy
  enable_lifecycle_policy    = true
  max_image_count            = 15
  lifecycle_tag_prefixes     = ["v", "stage", "latest"]
  untagged_image_expiry_days = 3

  # SSM Parameter 비활성화
  create_ssm_parameter = false

  # Required Tags (governance compliance)
  environment  = local.common_tags.environment
  service_name = "${var.project_name}-legacy-api"
  team         = local.common_tags.team
  owner        = local.common_tags.owner
  cost_center  = local.common_tags.cost_center
  project      = local.common_tags.project
  data_class   = local.common_tags.data_class
}

# ========================================
# ECR Repository: legacy-api-admin (Stage)
# ========================================
module "ecr_legacy_api_admin" {
  source = "git::https://github.com/ryu-qqq/Infrastructure.git//terraform/modules/ecr?ref=main"

  name                 = "${var.project_name}-legacy-api-admin-${var.environment}"
  image_tag_mutability = "MUTABLE"
  scan_on_push         = true
  encryption_type      = "AES256"

  # Lifecycle Policy
  enable_lifecycle_policy    = true
  max_image_count            = 15
  lifecycle_tag_prefixes     = ["v", "stage", "latest"]
  untagged_image_expiry_days = 3

  # SSM Parameter 비활성화
  create_ssm_parameter = false

  # Required Tags (governance compliance)
  environment  = local.common_tags.environment
  service_name = "${var.project_name}-legacy-api-admin"
  team         = local.common_tags.team
  owner        = local.common_tags.owner
  cost_center  = local.common_tags.cost_center
  project      = local.common_tags.project
  data_class   = local.common_tags.data_class
}

# ========================================
# ECR Repository: legacy-batch (Stage)
# ========================================
module "ecr_legacy_batch" {
  source = "git::https://github.com/ryu-qqq/Infrastructure.git//terraform/modules/ecr?ref=main"

  name                 = "${var.project_name}-legacy-batch-${var.environment}"
  image_tag_mutability = "MUTABLE"
  scan_on_push         = true
  encryption_type      = "AES256"

  # Lifecycle Policy
  enable_lifecycle_policy    = true
  max_image_count            = 15
  lifecycle_tag_prefixes     = ["v", "stage", "latest"]
  untagged_image_expiry_days = 3

  # SSM Parameter 비활성화
  create_ssm_parameter = false

  # Required Tags (governance compliance)
  environment  = local.common_tags.environment
  service_name = "${var.project_name}-legacy-batch"
  team         = local.common_tags.team
  owner        = local.common_tags.owner
  cost_center  = local.common_tags.cost_center
  project      = local.common_tags.project
  data_class   = local.common_tags.data_class
}

# ========================================
# ECR Repository: migration (Stage)
# ========================================
module "ecr_migration" {
  source = "git::https://github.com/ryu-qqq/Infrastructure.git//terraform/modules/ecr?ref=main"

  name                 = "${var.project_name}-migration-${var.environment}"
  image_tag_mutability = "MUTABLE"
  scan_on_push         = true
  encryption_type      = "AES256"

  # Lifecycle Policy
  enable_lifecycle_policy    = true
  max_image_count            = 15
  lifecycle_tag_prefixes     = ["v", "stage", "latest"]
  untagged_image_expiry_days = 3

  # SSM Parameter 비활성화
  create_ssm_parameter = false

  # Required Tags (governance compliance)
  environment  = local.common_tags.environment
  service_name = "${var.project_name}-migration"
  team         = local.common_tags.team
  owner        = local.common_tags.owner
  cost_center  = local.common_tags.cost_center
  project      = local.common_tags.project
  data_class   = local.common_tags.data_class
}
