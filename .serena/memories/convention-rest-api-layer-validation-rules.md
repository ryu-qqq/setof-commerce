# REST API Layer Convention Validation Rules - Index
# Version: 2.0.0
# Created: 2025-12-08
# Updated: 2025-12-08
# Changes: v2.0.0 - 카테고리별 분할 완료. 이 파일은 인덱스/요약본

## 📋 개요

이 문서는 REST API Layer 검증 규칙의 **인덱스(요약본)**입니다.
상세 규칙은 카테고리별 분할 파일을 참조하세요.

---

## 📚 분할 파일 구조

| 파일명 | 카테고리 | 규칙 수 | 설명 |
|--------|----------|---------|------|
| `rest-api-rules-01-controller` | CONTROLLER | 16개 | Controller 계층 규칙 |
| `rest-api-rules-02-command-dto` | COMMAND_DTO | 13개 | Command Request DTO 규칙 |
| `rest-api-rules-03-query-dto` | QUERY_DTO | 12개 | Query Request DTO 규칙 |
| `rest-api-rules-04-response-dto` | RESPONSE_DTO | 12개 | Response DTO 규칙 |
| `rest-api-rules-05-mapper` | MAPPER | 16개 | API Mapper 규칙 |
| `rest-api-rules-06-error` | ERROR | 9개 | 에러 처리 규칙 |
| `rest-api-rules-07-security` | SECURITY | 22개 | 보안 관련 규칙 (Gateway UUID 포함) |
| `rest-api-rules-08-openapi` | OPENAPI | 10개 | OpenAPI 문서화 규칙 |
| `rest-api-rules-09-testing` | TESTING | 12개 | 테스트 관련 규칙 |

---

## 📊 전체 통계

| 카테고리 | 규칙 수 | ERROR | WARNING | INFO |
|----------|---------|-------|---------|------|
| CONTROLLER | 16개 | 11 | 5 | 0 |
| COMMAND_DTO | 13개 | 6 | 4 | 3 |
| QUERY_DTO | 12개 | 4 | 6 | 2 |
| RESPONSE_DTO | 12개 | 6 | 3 | 3 |
| MAPPER | 16개 | 11 | 1 | 4 |
| ERROR | 9개 | 5 | 3 | 1 |
| SECURITY | 22개 | 18 | 4 | 0 |
| OPENAPI | 10개 | 2 | 7 | 1 |
| TESTING | 12개 | 4 | 7 | 1 |
| **총계** | **122개** | **67** | **40** | **15** |

---

## 🔍 규칙 ID 범위

| 카테고리 | ID Prefix | 범위 |
|----------|-----------|------|
| CONTROLLER | CTRL- | CTRL-001 ~ CTRL-016 |
| COMMAND_DTO | CMD- | CMD-001 ~ CMD-013 |
| QUERY_DTO | QRY- | QRY-001 ~ QRY-012 |
| RESPONSE_DTO | RSP- | RSP-001 ~ RSP-012 |
| MAPPER | MAP- | MAP-001 ~ MAP-016 |
| ERROR | ERR- | ERR-001 ~ ERR-009 |
| SECURITY | SEC- | SEC-001 ~ SEC-022 |
| OPENAPI | OAI- | OAI-001 ~ OAI-010 |
| TESTING | TEST- | TEST-001 ~ TEST-012 |

---

## 📤 검증 결과 JSON 출력 스키마

검증 결과는 표준화된 JSON 형식으로 출력됩니다.

```json
{
  "metadata": {
    "projectName": "string",
    "validatedAt": "date-time",
    "conventionVersion": "string"
  },
  "summary": {
    "totalFiles": "integer",
    "totalViolations": "integer",
    "errorCount": "integer",
    "warningCount": "integer",
    "infoCount": "integer"
  },
  "violations": [
    {
      "ruleId": "string (예: CTRL-001)",
      "category": "string",
      "severity": "ERROR|WARNING|INFO",
      "file": "string",
      "line": "integer",
      "message": "string"
    }
  ]
}
```

---

## 🔗 관련 문서

- Controller Guide: `docs/coding_convention/01-adapter-in-layer/rest-api/controller/`
- DTO Guides: `docs/coding_convention/01-adapter-in-layer/rest-api/dto/`
- Mapper Guide: `docs/coding_convention/01-adapter-in-layer/rest-api/mapper/`
- Error Guide: `docs/coding_convention/01-adapter-in-layer/rest-api/error/`
- Security Guide: `docs/coding_convention/01-adapter-in-layer/rest-api/security/`
- OpenAPI Guide: `docs/coding_convention/01-adapter-in-layer/rest-api/openapi/`
- Testing Guide: `docs/coding_convention/01-adapter-in-layer/rest-api/testing/`

---

## 📝 사용법

특정 카테고리 규칙이 필요할 때:
```
read_memory("rest-api-rules-07-security")  # SECURITY 규칙만 로드
read_memory("rest-api-rules-01-controller") # CONTROLLER 규칙만 로드
```

이 인덱스 파일만 로드하면 전체 구조와 통계를 빠르게 파악할 수 있습니다.
