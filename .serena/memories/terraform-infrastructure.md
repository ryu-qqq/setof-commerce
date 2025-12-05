# Terraform Infrastructure 가이드

## Wrapper Module 패턴

### 개념
- Infrastructure 레포 모듈을 래핑하여 컨벤션 강제
- GitHub: https://github.com/ryu-qqq/Infrastructure
- 모듈 위치: `terraform/modules/`

### 구조
```
terraform/
├── ecr/              # ECR Wrapper
├── ecs-cluster/      # ECS Cluster Wrapper
├── ecs-web-api/      # ECS Service Wrapper
├── elasticache/      # Redis Wrapper
├── s3/               # S3 Wrapper
└── environments/     # 환경별 tfvars
```

## 컨벤션 강제 항목

### 네이밍
```hcl
locals {
  naming = {
    resource = "${var.project_name}-{type}-${var.environment}"
  }
}
```
- 패턴: `{project}-{resource}-{env}`
- 예: `my-app-web-api-dev`

### 필수 태그 (8개)
```hcl
variable "project_name" { }   # 프로젝트명
variable "environment" { }    # dev/staging/prod
variable "team" { }           # 담당 팀
variable "owner" { }          # 소유자 이메일
variable "cost_center" { }    # 비용 센터
# 자동 추가: ManagedBy, ServiceName, DataClass
```

### 보안 설정 (하드코딩)
| 리소스 | 강제 설정 |
|--------|----------|
| ECR | `IMMUTABLE` 태그, `scan_on_push` |
| ECS | `assign_public_ip = false` |
| ElastiCache | 암호화 필수 |
| S3 | Public Access 완전 차단 |

### 환경별 제약
| 설정 | dev | staging/prod |
|------|-----|--------------|
| force_destroy | 허용 | 금지 |
| enable_execute_command | 허용 | 금지 |
| snapshot_retention | 7일 | 14일 |

## SSM Parameter (Cross-Stack)
```hcl
resource "aws_ssm_parameter" "bucket_name" {
  name  = "/${var.project_name}/s3/uploads-bucket-name"
  value = module.s3_uploads.bucket_id
}
```

### 참조 방법
```hcl
data "aws_ssm_parameter" "ecr_url" {
  name = "/${var.project_name}/ecr/web-api-repository-url"
}
```

## 배포 순서
1. ecr → 2. ecs-cluster → 3. elasticache (선택) → 4. s3 (선택) → 5. ecs-web-api

## 사용 가능한 Infrastructure 모듈
- ecr, ecs-service, elasticache, s3-bucket
- alb, rds, security-group, cloudfront
- route53-record, sns, sqs, lambda
- eventbridge, waf, cloudwatch-log-group
- iam-role-policy, bastion-ssm, adot-sidecar
