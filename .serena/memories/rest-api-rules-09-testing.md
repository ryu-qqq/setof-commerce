# REST API Layer - TESTING 규칙 (12개)
# Version: 1.0.0
# Created: 2025-12-08
# Category: TESTING

## 📋 개요
테스트 관련 규칙 12개

---

## 규칙 목록

```json
{
  "category": "TESTING",
  "rules": [
    {
      "id": "TEST-001",
      "name": "TestRestTemplate 사용 필수",
      "severity": "ERROR",
      "description": "REST API 테스트는 TestRestTemplate 사용 필수. MockMvc 금지",
      "pattern": {
        "type": "type_usage",
        "required": ["TestRestTemplate"],
        "forbidden": ["MockMvc", "MockMvcBuilders"]
      },
      "autofix": false
    },
    {
      "id": "TEST-002",
      "name": "@WebMvcTest 금지",
      "severity": "ERROR",
      "description": "@WebMvcTest 금지. @SpringBootTest + TestRestTemplate 사용",
      "pattern": {
        "type": "annotation",
        "forbidden": ["@WebMvcTest"],
        "required": ["@SpringBootTest"]
      },
      "autofix": false
    },
    {
      "id": "TEST-003",
      "name": "@SpringBootTest(webEnvironment) 필수",
      "severity": "ERROR",
      "description": "@SpringBootTest에 webEnvironment = RANDOM_PORT 설정 필수",
      "pattern": {
        "type": "annotation_attribute",
        "required": ["webEnvironment = WebEnvironment.RANDOM_PORT"]
      },
      "autofix": true
    },
    {
      "id": "TEST-004",
      "name": "@Sql 사용 권장",
      "severity": "WARNING",
      "description": "테스트 데이터 설정에 @Sql 어노테이션 사용 권장",
      "pattern": {
        "type": "annotation",
        "recommended": ["@Sql"]
      },
      "autofix": false
    },
    {
      "id": "TEST-005",
      "name": "@Transactional 금지",
      "severity": "ERROR",
      "description": "통합 테스트에서 @Transactional 금지. 실제 트랜잭션 경계 테스트",
      "pattern": {
        "type": "annotation",
        "forbidden": ["@Transactional"]
      },
      "autofix": false
    },
    {
      "id": "TEST-006",
      "name": "Test Fixture 사용",
      "severity": "WARNING",
      "description": "test-fixtures 모듈의 Builder 사용 권장",
      "pattern": {
        "type": "import",
        "recommended_pattern": "import.*\\.testfixtures\\."
      },
      "autofix": false
    },
    {
      "id": "TEST-007",
      "name": "HTTP 상태 코드 검증",
      "severity": "ERROR",
      "description": "모든 API 테스트에 HTTP 상태 코드 검증 필수",
      "pattern": {
        "type": "method_call",
        "required": ["getStatusCode()", "assertThat.*HttpStatus"]
      },
      "autofix": false
    },
    {
      "id": "TEST-008",
      "name": "응답 바디 검증",
      "severity": "WARNING",
      "description": "성공 케이스에서 응답 바디 내용 검증 권장",
      "pattern": {
        "type": "method_call",
        "recommended": ["getBody()", "assertThat.*body"]
      },
      "autofix": false
    },
    {
      "id": "TEST-009",
      "name": "인증 테스트 분리",
      "severity": "WARNING",
      "description": "인증 성공/실패 케이스 별도 테스트 권장",
      "pattern": {
        "type": "test_method_naming",
        "recommended": ["WithAuth", "WithoutAuth", "Unauthorized"]
      },
      "autofix": false
    },
    {
      "id": "TEST-010",
      "name": "REST Docs 문서화",
      "severity": "INFO",
      "description": "API 테스트에 REST Docs 스니펫 생성 권장",
      "pattern": {
        "type": "annotation",
        "recommended": ["@AutoConfigureRestDocs"]
      },
      "autofix": false
    },
    {
      "id": "TEST-011",
      "name": "@DisplayName 필수",
      "severity": "WARNING",
      "description": "테스트 메서드에 한국어 @DisplayName 권장",
      "pattern": {
        "type": "annotation",
        "recommended": ["@DisplayName"]
      },
      "autofix": false
    },
    {
      "id": "TEST-012",
      "name": "테스트 데이터 정리",
      "severity": "WARNING",
      "description": "@AfterEach 또는 @Sql(executionPhase=AFTER_TEST_METHOD)로 데이터 정리",
      "pattern": {
        "type": "annotation",
        "recommended": ["@AfterEach", "@Sql.*AFTER_TEST_METHOD"]
      },
      "autofix": false
    }
  ]
}
```

---

## 📊 통계

| Severity | 개수 |
|----------|------|
| ERROR | 4 |
| WARNING | 7 |
| INFO | 1 |
| **총계** | **12** |

---

## 🔗 관련 문서

- Integration Test Guide: `docs/coding_convention/01-adapter-in-layer/rest-api/testing/01_rest-api-testing-guide.md`
- Unit Test Guide: `docs/coding_convention/01-adapter-in-layer/rest-api/testing/02_controller-unit-test-guide.md`
- REST Docs Guide: `docs/coding_convention/01-adapter-in-layer/rest-api/testing/03_rest-docs-guide.md`
- ArchUnit Guide: `docs/coding_convention/01-adapter-in-layer/rest-api/testing/04_rest-api-archunit-guide.md`
