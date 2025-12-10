# REST API Layer - Mapper Rules

## MAPPER (16 rules)

API Mapper 규칙

```json
{
  "category": "MAPPER",
  "rules": [
    {
      "ruleId": "REST-MAP-001",
      "name": "@Component 어노테이션 필수",
      "severity": "ERROR",
      "description": "Mapper는 @Component Bean으로 등록해야 함",
      "pattern": "@Component.*class.*ApiMapper"
    },
    {
      "ruleId": "REST-MAP-002",
      "name": "*ApiMapper 네이밍 규칙",
      "severity": "ERROR",
      "description": "Mapper는 *ApiMapper 접미사 필수",
      "pattern": "^[A-Z][a-zA-Z]*ApiMapper$"
    },
    {
      "ruleId": "REST-MAP-003",
      "name": "Static 메서드 금지",
      "severity": "ERROR",
      "description": "Static 메서드 금지. 인스턴스 메서드로 DI 가능하게",
      "antiPattern": "public static \\w+ (to|map)"
    },
    {
      "ruleId": "REST-MAP-004",
      "name": "비즈니스 로직 금지",
      "severity": "ERROR",
      "description": "Mapper에 비즈니스 로직 금지. 필드 매핑만",
      "antiPattern": ["if.*else", "switch.*case", "throw new"]
    },
    {
      "ruleId": "REST-MAP-005",
      "name": "Domain 객체 직접 사용 금지",
      "severity": "ERROR",
      "description": "Domain Entity/Aggregate 직접 사용 금지. Application DTO만 사용",
      "antiPattern": "import.*\\.domain\\.[a-z]+\\.(?!exception)"
    },
    {
      "ruleId": "REST-MAP-006",
      "name": "Lombok 금지",
      "severity": "ERROR",
      "description": "Lombok 어노테이션 전면 금지",
      "antiPattern": "@(Data|Builder|RequiredArgsConstructor)"
    },
    {
      "ruleId": "REST-MAP-007",
      "name": "Repository/UseCase 주입 금지",
      "severity": "ERROR",
      "description": "Mapper에 Repository, UseCase 주입 금지. Controller가 UseCase 주입",
      "antiPattern": "(Repository|UseCase|Service)\\s+\\w+;"
    },
    {
      "ruleId": "REST-MAP-008",
      "name": "패키지 위치",
      "severity": "WARNING",
      "description": "올바른 패키지 위치: adapter.in.rest.{bc}.mapper",
      "pattern": "package.*\\.adapter\\.in\\.rest\\.[a-z]+\\.mapper"
    },
    {
      "ruleId": "REST-MAP-009",
      "name": "기본값 설정 금지",
      "severity": "ERROR",
      "description": "Mapper에서 기본값 설정 금지. Controller 또는 DTO Compact Constructor 책임",
      "antiPattern": "!= null \\? .* : .*default"
    },
    {
      "ruleId": "REST-MAP-010",
      "name": "검증 로직 금지",
      "severity": "ERROR",
      "description": "Mapper에서 검증 로직 금지. Bean Validation (@Valid, @NotNull 등) 사용",
      "antiPattern": "if.*[<>]=?.*throw"
    },
    {
      "ruleId": "REST-MAP-011",
      "name": "비즈니스 로직 금지 (계산)",
      "severity": "ERROR",
      "description": "Mapper에서 비즈니스 로직 금지. UseCase 책임",
      "antiPattern": ["\\.multiply\\(", "\\.add\\(", "\\.subtract\\(", "calculate", "compute"]
    },
    {
      "ruleId": "REST-MAP-012",
      "name": "상태 변환 로직 금지",
      "severity": "ERROR",
      "description": "Mapper에서 상태 변환 로직 금지 (switch status). Application Layer 책임",
      "antiPattern": "switch.*status"
    },
    {
      "ruleId": "REST-MAP-013",
      "name": "계산 로직 금지",
      "severity": "ERROR",
      "description": "Mapper에서 계산 로직 금지. Domain/UseCase 책임",
      "antiPattern": ["\\* 0\\.", "/ 100", "Math\\."]
    },
    {
      "ruleId": "REST-MAP-014",
      "name": "조건부 필드 설정 금지",
      "severity": "ERROR",
      "description": "Mapper에서 조건부 필드 설정 금지. Application Layer 책임",
      "antiPattern": "\\.equals\\(.*\\) \\?"
    },
    {
      "ruleId": "REST-MAP-015",
      "name": "Cursor/Offset 자동 판단",
      "severity": "INFO",
      "description": "Cursor 유무에 따라 페이징 타입 자동 설정 (ofCursor/ofOffset)",
      "pattern": "request.cursor\\(\\) != null|ofCursor|ofOffset"
    },
    {
      "ruleId": "REST-MAP-016",
      "name": "Private Helper 메서드 허용",
      "severity": "INFO",
      "description": "Nested 변환용 Private Helper 메서드 허용 (toCustomerInfo, toLineItems 등)",
      "pattern": "private \\w+ to[A-Z]"
    }
  ]
}
```

## 관련 문서

- `docs/coding_convention/01-adapter-in-layer/rest-api/mapper/mapper-guide.md`
