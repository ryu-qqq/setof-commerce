# REST API Layer - Command DTO Rules

## COMMAND_DTO (13 rules)

Command Request DTO 규칙 (CUD 요청)

```json
{
  "category": "COMMAND_DTO",
  "rules": [
    {
      "ruleId": "REST-CMD-001",
      "name": "Record 타입 필수",
      "severity": "ERROR",
      "description": "Command DTO는 public record 키워드로 정의해야 함",
      "pattern": "public record \\w+ApiRequest",
      "antiPattern": "public class \\w+ApiRequest"
    },
    {
      "ruleId": "REST-CMD-002",
      "name": "*ApiRequest 네이밍 규칙",
      "severity": "ERROR",
      "description": "Command DTO는 *ApiRequest 접미사 필수",
      "pattern": "^[A-Z][a-zA-Z]*ApiRequest$"
    },
    {
      "ruleId": "REST-CMD-003",
      "name": "Lombok 금지",
      "severity": "ERROR",
      "description": "Lombok 어노테이션 전면 금지",
      "antiPattern": "@(Data|Builder|Getter|Setter|Value)"
    },
    {
      "ruleId": "REST-CMD-004",
      "name": "Jackson 어노테이션 금지",
      "severity": "ERROR",
      "description": "Jackson 어노테이션 금지 (@JsonFormat, @JsonProperty 등)",
      "antiPattern": "@Json(Format|Property|Ignore|Include)"
    },
    {
      "ruleId": "REST-CMD-005",
      "name": "Domain 변환 메서드 금지",
      "severity": "ERROR",
      "description": "DTO에 toDomain(), toEntity() 등 Domain 변환 메서드 금지",
      "antiPattern": "\\s+(toDomain|toEntity|toAggregate)\\s*\\("
    },
    {
      "ruleId": "REST-CMD-006",
      "name": "Bean Validation 어노테이션 권장",
      "severity": "WARNING",
      "description": "필드에 @NotNull, @NotBlank 등 Bean Validation 어노테이션 권장",
      "pattern": "@(NotNull|NotBlank|NotEmpty|Valid|Min|Max|Pattern)"
    },
    {
      "ruleId": "REST-CMD-007",
      "name": "Spring 어노테이션 금지",
      "severity": "ERROR",
      "description": "Spring 어노테이션 금지 (@Component, @Service 등)",
      "antiPattern": "@(Component|Service|Repository|Controller)"
    },
    {
      "ruleId": "REST-CMD-008",
      "name": "Setter 메서드 금지",
      "severity": "ERROR",
      "description": "Record는 불변이므로 Setter 메서드 금지",
      "antiPattern": "void set[A-Z]"
    },
    {
      "ruleId": "REST-CMD-009",
      "name": "비즈니스 로직 메서드 금지",
      "severity": "WARNING",
      "description": "DTO에 비즈니스 로직 메서드 금지 (isValid, calculate 등)",
      "antiPattern": "(boolean is[A-Z]|calculate|compute|validate)\\s*\\("
    },
    {
      "ruleId": "REST-CMD-010",
      "name": "패키지 위치",
      "severity": "WARNING",
      "description": "올바른 패키지 위치: adapter.in.rest.{bc}.dto.command",
      "pattern": "package.*\\.adapter\\.in\\.rest\\.[a-z]+\\.dto\\.command"
    },
    {
      "ruleId": "REST-CMD-011",
      "name": "Compact Constructor 활용",
      "severity": "WARNING",
      "description": "불변 리스트 변환(List.copyOf()), 추가 검증 로직은 Compact Constructor에서 처리",
      "pattern": "List.copyOf\\("
    },
    {
      "ruleId": "REST-CMD-012",
      "name": "Nested Record 사용",
      "severity": "INFO",
      "description": "복잡한 구조는 Nested Record로 표현 (예: OrderItemRequest)"
    },
    {
      "ruleId": "REST-CMD-013",
      "name": "액션 명확",
      "severity": "WARNING",
      "description": "Command 이름에 액션 포함 (Create, Update, Cancel, Approve 등)",
      "pattern": "^(Create|Update|Modify|Change|Cancel|Confirm|Approve|Reject|Complete|Register|Place|Enroll)[A-Z][a-zA-Z]*ApiRequest$"
    }
  ]
}
```

## 관련 문서

- `docs/coding_convention/01-adapter-in-layer/rest-api/dto/command/command-dto-guide.md`
