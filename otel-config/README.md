# OTEL Config - ADOT Collector 설정 파일

이 디렉토리에는 AWS Distro for OpenTelemetry (ADOT) Collector 설정 파일이 포함되어 있습니다.

## 디렉토리 구조

```
otel-config/
├── README.md
├── upload-to-s3.sh                    # S3 업로드 스크립트 (KMS 암호화)
├── setof-commerce-legacy-api/
│   └── otel-config.yaml               # Legacy API용 ADOT 설정 (port 8088)
├── setof-commerce-legacy-admin/
│   └── otel-config.yaml               # Legacy Admin용 ADOT 설정 (port 8089)
├── setof-commerce-web-api/
│   └── otel-config.yaml               # Web API용 ADOT 설정 (port 8080)
├── setof-commerce-web-api-admin/
│   └── otel-config.yaml               # Web API Admin용 ADOT 설정 (port 8081)
└── setof-commerce-legacy-batch/
    └── otel-config.yaml               # Legacy Batch용 ADOT 설정 (port 8090)
```

## 서비스별 포트 매핑

| 서비스 | 포트 | Terraform 디렉토리 |
|--------|------|-------------------|
| legacy-api | 8088 | ecs-web-api-legacy |
| legacy-admin | 8089 | ecs-web-api-legacy-admin |
| web-api | 8080 | ecs-web-api |
| web-api-admin | 8081 | ecs-web-api-admin |
| legacy-batch | 8090 | ecs-batch |

## S3 업로드

ADOT Sidecar는 S3에서 설정 파일을 읽습니다. 다음 명령으로 업로드합니다:

```bash
# 전체 업로드 (KMS 암호화 자동 적용)
./otel-config/upload-to-s3.sh
```

**참고**: prod-connectly 버킷은 KMS 암호화가 활성화되어 있어 `--sse aws:kms` 옵션이 필수입니다.

## S3 경로 규칙

Terraform의 `adot-sidecar` 모듈은 다음 패턴으로 S3 경로를 구성합니다:

```
s3://{bucket}/otel-config/{project_name}-{service_name}/otel-config.yaml
```

예시:
- `s3://prod-connectly/otel-config/setof-commerce-legacy-api/otel-config.yaml`
- `s3://prod-connectly/otel-config/setof-commerce-web-api/otel-config.yaml`

## 설정 변경 시

1. 설정 파일 수정
2. S3에 업로드: `./otel-config/upload-to-s3.sh`
3. ECS 서비스 재배포 (또는 Terraform의 `config_version` 업데이트)

```bash
# ECS 서비스 재배포 예시
aws ecs update-service --cluster setof-commerce-cluster-prod \
    --service setof-commerce-legacy-api-prod \
    --force-new-deployment
```

## 수집 메트릭

### Prometheus 메트릭 (Spring Boot Actuator)
- `http_server_requests_*` - HTTP 요청 메트릭
- `jvm_*` - JVM 메트릭 (메모리, GC, 스레드)
- `process_*` - 프로세스 메트릭
- `hikaricp_*` - HikariCP 커넥션 풀 메트릭
- `jdbc_*` - JDBC 메트릭
- `spring_batch_*` - Spring Batch 메트릭 (batch 서비스만)

### ECS Container 메트릭
- CPU 사용률
- 메모리 사용률
- 네트워크 I/O
- 디스크 I/O

### 트레이스
- AWS X-Ray로 분산 트레이싱 전송

## 로그 스트리밍 (OpenSearch)

모든 ECS 서비스는 CloudWatch Logs → Kinesis → Lambda → OpenSearch 파이프라인을 통해 로그가 스트리밍됩니다.

Terraform에서 `log-subscription-filter-v2` 모듈로 구성됩니다.

## 관련 문서

- [ADOT 통합 가이드](../docs/coding_convention/06-observability/adot-integration.md)
- [AWS ADOT 공식 문서](https://aws-otel.github.io/docs/introduction)
