# setof-commerce Terraform 환경별 구성

## 디렉토리 구조

```
environments/
├── prod/                           # 프로덕션 환경
│   ├── ecr/                        # ECR Repository
│   ├── ecs-cluster/                # ECS Cluster
│   ├── ecs-batch/                  # ECS Service (batch)
│   ├── ecs-web-api/                # ECS Service (web-api)
│   ├── ecs-web-api-admin/          # ECS Service (web-api-admin)
│   ├── ecs-web-api-legacy/         # ECS Service (legacy-web-api)
│   ├── ecs-web-api-legacy-admin/   # ECS Service (legacy-web-api-admin)
│   └── elasticache/                # Redis
│
└── stage/                          # 스테이징 환경
    ├── ecr/                        # ECR Repository
    ├── ecs-cluster/                # ECS Cluster
    ├── ecs-batch/                  # ECS Service (batch)
    ├── ecs-web-api/                # ECS Service (web-api)
    ├── ecs-web-api-admin/          # ECS Service (web-api-admin)
    ├── ecs-web-api-legacy/         # ECS Service (legacy-web-api)
    └── ecs-web-api-legacy-admin/   # ECS Service (legacy-web-api-admin)
```

## 환경별 차이점

| 항목 | Prod | Stage |
|------|------|-------|
| ECS CPU | 512 | 256 |
| ECS Memory | 1024 | 512 |
| Desired Count | 2 | 1 |
| AutoScaling Max | 10 | 3 |
| Log Retention | 30일 | 14일 |
| Snapshot Retention | 1일 | 0 (비활성화) |
| Capacity Provider | FARGATE 100% | FARGATE_SPOT 70% |

## 배포 순서

Stage 환경 최초 배포 시 다음 순서로 진행:

```bash
# 1. ECR Repository 생성
cd environments/stage/ecr
terraform init
terraform apply

# 2. ECS Cluster 생성
cd ../ecs-cluster
terraform init
terraform apply

# 3. ElastiCache (Redis) 생성 (prod only)
cd ../elasticache
terraform init
terraform apply

# 4. ECS Services 생성 (순서 무관)
cd ../ecs-web-api
terraform init
terraform apply

cd ../ecs-web-api-admin
terraform init
terraform apply

# Legacy services
cd ../ecs-web-api-legacy
terraform init
terraform apply

cd ../ecs-web-api-legacy-admin
terraform init
terraform apply

# Batch service
cd ../ecs-batch
terraform init
terraform apply
```

## 사전 요구사항 (Stage)

Stage 환경 배포 전 다음 AWS 리소스가 필요합니다:

### SSM Parameters
```bash
# VPC
/shared/network/vpc-id
/shared/network/private-subnets

# RDS Endpoint
/shared/stage/rds/endpoint

# AMP (Monitoring)
/shared/monitoring/amp-workspace-arn
/shared/monitoring/amp-remote-write-url

# Service Discovery
/shared/service-discovery/namespace-id
```

### Secrets Manager
```bash
# Stage RDS 인증 정보
stage-shared-mysql-auth
  {
    "username": "...",
    "password": "..."
  }
```

## Backend State 경로

| 환경 | 모듈 | State Key |
|------|------|-----------|
| prod | ecr | `setof/ecr/terraform.tfstate` |
| prod | ecs-cluster | `setof/ecs-cluster/terraform.tfstate` |
| prod | ecs-web-api | `setof/ecs-web-api/terraform.tfstate` |
| prod | ecs-web-api-admin | `setof/ecs-web-api-admin/terraform.tfstate` |
| prod | ecs-web-api-legacy | `setof/ecs-web-api-legacy/terraform.tfstate` |
| prod | ecs-web-api-legacy-admin | `setof/ecs-web-api-legacy-admin/terraform.tfstate` |
| prod | ecs-batch | `setof/ecs-batch/terraform.tfstate` |
| prod | elasticache | `setof/elasticache/terraform.tfstate` |
| stage | ecr | `setof/stage/ecr/terraform.tfstate` |
| stage | ecs-cluster | `setof/stage/ecs-cluster/terraform.tfstate` |
| stage | ecs-web-api | `setof/stage/ecs-web-api/terraform.tfstate` |
| stage | ecs-web-api-admin | `setof/stage/ecs-web-api-admin/terraform.tfstate` |
| stage | ecs-web-api-legacy | `setof/stage/ecs-web-api-legacy/terraform.tfstate` |
| stage | ecs-web-api-legacy-admin | `setof/stage/ecs-web-api-legacy-admin/terraform.tfstate` |
| stage | ecs-batch | `setof/stage/ecs-batch/terraform.tfstate` |

## Sentry 환경변수 설정

각 ECS Task Definition에서 `SENTRY_DSN` 환경변수를 SSM Parameter 또는 직접 값으로 주입:

```hcl
# provider.tf의 locals 블록에서 정의
locals {
  # Sentry Configuration (Prod only)
  sentry_dsn = "https://ac11e32d9607b289b2bd20925f65cf2e@o4509783165173760.ingest.us.sentry.io/4510790972866560"
}

# main.tf의 container_definitions에서 사용
environment = [
  {
    name  = "SENTRY_DSN"
    value = local.sentry_dsn
  },
  ...
]
```

## 주의사항

1. **Prod Backend Key**: Prod 환경은 기존 state 호환성을 위해 레거시 경로 유지
2. **Shared Resources**: VPC, RDS, Service Discovery Namespace는 `/shared/` 경로의 SSM 파라미터 공유
3. **Stage RDS**: 기존 Stage RDS 엔드포인트를 SSM에 등록 필요
4. **Atlantis**: PR 기반 terraform plan/apply는 atlantis.yaml 참조
