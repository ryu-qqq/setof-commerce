# REST API Layer - Query DTO Rules

## QUERY_DTO (12 rules)

Query Request DTO 규칙 (Read 요청)

```json
{
  "category": "QUERY_DTO",
  "rules": [
    {
      "ruleId": "REST-QRY-001",
      "name": "Record 타입 필수",
      "severity": "ERROR",
      "description": "Query DTO는 public record 키워드로 정의해야 함",
      "pattern": "public record \\w+ApiRequest",
      "antiPattern": "public class \\w+ApiRequest"
    },
    {
      "ruleId": "REST-QRY-002",
      "name": "*ApiRequest 네이밍 규칙",
      "severity": "ERROR",
      "description": "Query DTO는 *ApiRequest 접미사 필수 (Search, List, Detail 등)",
      "pattern": "^[A-Z][a-zA-Z]*(Search|List|Detail|Stats)?ApiRequest$"
    },
    {
      "ruleId": "REST-QRY-003",
      "name": "Lombok 금지",
      "severity": "ERROR",
      "description": "Lombok 어노테이션 전면 금지",
      "antiPattern": "@(Data|Builder|Getter|Setter|Value)"
    },
    {
      "ruleId": "REST-QRY-004",
      "name": "Jackson 어노테이션 금지",
      "severity": "ERROR",
      "description": "Jackson 어노테이션 금지",
      "antiPattern": "@Json(Format|Property|Ignore)"
    },
    {
      "ruleId": "REST-QRY-005",
      "name": "Domain 변환 메서드 금지",
      "severity": "ERROR",
      "description": "toCriteria, toFilter 등 Domain 변환 메서드 금지",
      "antiPattern": "\\s+(toCriteria|toFilter|toDomain|toQuery)\\s*\\("
    },
    {
      "ruleId": "REST-QRY-006",
      "name": "페이징 필드 권장",
      "severity": "INFO",
      "description": "조회 DTO에 페이징 필드 (page, size) 권장",
      "pattern": "(Integer|int)\\s+(page|size|cursor)"
    },
    {
      "ruleId": "REST-QRY-007",
      "name": "Setter 메서드 금지",
      "severity": "ERROR",
      "description": "Record는 불변이므로 Setter 메서드 금지",
      "antiPattern": "void set[A-Z]"
    },
    {
      "ruleId": "REST-QRY-008",
      "name": "패키지 위치",
      "severity": "WARNING",
      "description": "올바른 패키지 위치: adapter.in.rest.{bc}.dto.query",
      "pattern": "package.*\\.adapter\\.in\\.rest\\.[a-z]+\\.dto\\.query"
    },
    {
      "ruleId": "REST-QRY-009",
      "name": "Optional 필드",
      "severity": "WARNING",
      "description": "조회 조건은 대부분 Optional (null 허용), 필수 필드에만 @NotNull 사용"
    },
    {
      "ruleId": "REST-QRY-010",
      "name": "기본값 설정",
      "severity": "WARNING",
      "description": "Compact Constructor에서 기본값 설정 (page=0, size=20 등)",
      "pattern": "page = page == null|size = size == null"
    },
    {
      "ruleId": "REST-QRY-011",
      "name": "날짜 범위 검증",
      "severity": "WARNING",
      "description": "startDate/endDate 범위 검증 (startDate.isAfter(endDate) 체크)",
      "pattern": "startDate.*isAfter.*endDate"
    },
    {
      "ruleId": "REST-QRY-012",
      "name": "Cursor 페이징 지원",
      "severity": "INFO",
      "description": "Cursor 기반 페이징 필드 (cursor, size) 지원 권장",
      "pattern": "String\\s+cursor"
    }
  ]
}
```

## 관련 문서

- `docs/coding_convention/01-adapter-in-layer/rest-api/dto/query/query-dto-guide.md`
