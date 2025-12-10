# REST API Layer - Error Handling Rules

## ERROR (9 rules)

에러 처리 규칙

```json
{
  "category": "ERROR",
  "rules": [
    {
      "ruleId": "REST-ERR-001",
      "name": "RFC 7807 ProblemDetail 사용",
      "severity": "ERROR",
      "description": "에러 응답은 ProblemDetail 형식 사용 필수",
      "pattern": "ProblemDetail"
    },
    {
      "ruleId": "REST-ERR-002",
      "name": "Content-Type: application/problem+json",
      "severity": "ERROR",
      "description": "에러 응답에 RFC 7807 Content-Type 필수",
      "pattern": "APPLICATION_PROBLEM_JSON"
    },
    {
      "ruleId": "REST-ERR-003",
      "name": "ErrorMapper 구현",
      "severity": "ERROR",
      "description": "BC별 ErrorMapper 구현 필수",
      "pattern": "implements ErrorMapper"
    },
    {
      "ruleId": "REST-ERR-004",
      "name": "스택트레이스 노출 금지",
      "severity": "ERROR",
      "description": "에러 응답에 스택트레이스 노출 금지",
      "antiPattern": ["getStackTrace", "printStackTrace"]
    },
    {
      "ruleId": "REST-ERR-005",
      "name": "x-error-code 헤더 필수",
      "severity": "WARNING",
      "description": "에러 응답에 x-error-code 헤더 추가 권장",
      "pattern": "x-error-code"
    },
    {
      "ruleId": "REST-ERR-006",
      "name": "Trace ID 포함",
      "severity": "WARNING",
      "description": "에러 응답에 traceId 확장 필드 포함 권장",
      "pattern": "traceId"
    },
    {
      "ruleId": "REST-ERR-007",
      "name": "로깅 레벨 전략",
      "severity": "WARNING",
      "description": "5xx → ERROR (스택트레이스 포함), 404 → DEBUG, 기타 4xx → WARN",
      "pattern": ["log.error.*5xx", "log.debug.*NOT_FOUND", "log.warn.*4xx"]
    },
    {
      "ruleId": "REST-ERR-008",
      "name": "OCP 준수",
      "severity": "ERROR",
      "description": "ErrorMapperRegistry 패턴으로 확장성 보장. GlobalExceptionHandler 직접 분기 금지",
      "pattern": "errorMapperRegistry.map",
      "antiPattern": "if.*ex\\.code\\(\\)\\.equals"
    },
    {
      "ruleId": "REST-ERR-009",
      "name": "ErrorMapper supports()",
      "severity": "ERROR",
      "description": "ErrorMapper.supports()에서 예외 타입 또는 에러 코드 prefix로 매칭",
      "pattern": "supports\\(DomainException ex\\)"
    }
  ]
}
```

## 관련 문서

- `docs/coding_convention/01-adapter-in-layer/rest-api/error/error-guide.md`
