# REST API Layer - Response DTO Rules

## RESPONSE_DTO (12 rules)

Response DTO 규칙

```json
{
  "category": "RESPONSE_DTO",
  "rules": [
    {
      "ruleId": "REST-RSP-001",
      "name": "Record 타입 필수",
      "severity": "ERROR",
      "description": "Response DTO는 public record 키워드로 정의해야 함",
      "pattern": "public record \\w+ApiResponse",
      "antiPattern": "public class \\w+ApiResponse"
    },
    {
      "ruleId": "REST-RSP-002",
      "name": "*ApiResponse 네이밍 규칙",
      "severity": "ERROR",
      "description": "Response DTO는 *ApiResponse 접미사 필수",
      "pattern": "^[A-Z][a-zA-Z]*ApiResponse$"
    },
    {
      "ruleId": "REST-RSP-003",
      "name": "Lombok 금지",
      "severity": "ERROR",
      "description": "Lombok 어노테이션 전면 금지",
      "antiPattern": "@(Data|Builder|Getter|Setter)"
    },
    {
      "ruleId": "REST-RSP-004",
      "name": "Jackson 어노테이션 금지",
      "severity": "ERROR",
      "description": "Jackson 어노테이션 금지",
      "antiPattern": "@Json(Format|Property|Ignore)"
    },
    {
      "ruleId": "REST-RSP-005",
      "name": "from() 정적 팩토리 메서드 권장",
      "severity": "INFO",
      "description": "Application Response → API Response 변환을 위한 from() 메서드 권장",
      "pattern": "public static \\w+ApiResponse from\\("
    },
    {
      "ruleId": "REST-RSP-006",
      "name": "비즈니스 로직 메서드 금지",
      "severity": "WARNING",
      "description": "DTO에 비즈니스 로직 메서드 금지",
      "antiPattern": "(boolean is[A-Z]|calculate|compute|validate)\\s*\\("
    },
    {
      "ruleId": "REST-RSP-007",
      "name": "Spring Page 직접 사용 금지",
      "severity": "ERROR",
      "description": "Page<T> 직접 반환 금지. PageApiResponse/SliceApiResponse 사용",
      "antiPattern": "ResponseEntity<.*Page<|ResponseEntity<.*Slice<"
    },
    {
      "ruleId": "REST-RSP-008",
      "name": "패키지 위치",
      "severity": "WARNING",
      "description": "올바른 패키지 위치: adapter.in.rest.{bc}.dto.response",
      "pattern": "package.*\\.adapter\\.in\\.rest\\.[a-z]+\\.dto\\.response"
    },
    {
      "ruleId": "REST-RSP-009",
      "name": "Compact Constructor",
      "severity": "WARNING",
      "description": "불변 컬렉션 적용 (List.copyOf()) - Compact Constructor에서 처리",
      "pattern": "items = List.copyOf\\(items\\)"
    },
    {
      "ruleId": "REST-RSP-010",
      "name": "SliceApiResponse 사용",
      "severity": "ERROR",
      "description": "Cursor 기반 페이징 응답에 SliceApiResponse 사용 (무한 스크롤)",
      "pattern": "SliceApiResponse<"
    },
    {
      "ruleId": "REST-RSP-011",
      "name": "PageApiResponse 사용",
      "severity": "ERROR",
      "description": "Offset 기반 페이징 응답에 PageApiResponse 사용 (관리자 테이블)",
      "pattern": "PageApiResponse<"
    },
    {
      "ruleId": "REST-RSP-012",
      "name": "Domain 직접 노출 금지",
      "severity": "ERROR",
      "description": "Domain Entity를 Response DTO로 직접 반환 금지",
      "antiPattern": "ResponseEntity<.*(?!ApiResponse).*>"
    }
  ]
}
```

## 관련 문서

- `docs/coding_convention/01-adapter-in-layer/rest-api/dto/response/response-dto-guide.md`
