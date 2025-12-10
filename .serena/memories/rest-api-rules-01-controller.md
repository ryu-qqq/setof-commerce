# REST API Layer - Controller Rules

## CONTROLLER (16 rules)

REST API Controller 계층 규칙

```json
{
  "category": "CONTROLLER",
  "rules": [
    {
      "ruleId": "REST-CTRL-001",
      "name": "ResponseEntity<ApiResponse<T>> 래핑 필수",
      "severity": "ERROR",
      "description": "모든 Controller 응답은 ResponseEntity<ApiResponse<T>> 형식으로 반환해야 합니다",
      "pattern": "ResponseEntity<ApiResponse<",
      "antiPattern": ["ApiResponse<(?!.*ResponseEntity)", "ResponseEntity<(?!ApiResponse)"]
    },
    {
      "ruleId": "REST-CTRL-002",
      "name": "@Valid 어노테이션 필수",
      "severity": "ERROR",
      "description": "모든 @RequestBody 파라미터에 @Valid 어노테이션 필수",
      "pattern": "@Valid\\s+@RequestBody|@RequestBody\\s+@Valid",
      "antiPattern": "@RequestBody(?!.*@Valid)\\s+\\w+Request"
    },
    {
      "ruleId": "REST-CTRL-003",
      "name": "DELETE 메서드 금지",
      "severity": "ERROR",
      "description": "DELETE 엔드포인트는 지원하지 않음. 소프트 삭제는 PATCH로 처리",
      "antiPattern": "@DeleteMapping"
    },
    {
      "ruleId": "REST-CTRL-004",
      "name": "@Transactional 금지",
      "severity": "ERROR",
      "description": "Controller에 @Transactional 사용 금지. UseCase 책임",
      "antiPattern": "@Transactional.*class.*Controller"
    },
    {
      "ruleId": "REST-CTRL-005",
      "name": "비즈니스 로직 금지",
      "severity": "ERROR",
      "description": "Controller에 비즈니스 로직 구현 금지",
      "antiPattern": ["if.*throw", "switch.*case", "domain\\.", "new.*Entity"]
    },
    {
      "ruleId": "REST-CTRL-006",
      "name": "try-catch 금지",
      "severity": "ERROR",
      "description": "Controller에서 try-catch 예외 처리 금지. GlobalExceptionHandler 위임",
      "antiPattern": ["try\\s*\\{", "catch\\s*\\("]
    },
    {
      "ruleId": "REST-CTRL-007",
      "name": "Lombok 금지",
      "severity": "ERROR",
      "description": "Lombok 어노테이션 전면 금지",
      "antiPattern": "@(Data|Builder|Getter|Setter|AllArgsConstructor|NoArgsConstructor|RequiredArgsConstructor)"
    },
    {
      "ruleId": "REST-CTRL-008",
      "name": "@RestController 필수",
      "severity": "ERROR",
      "description": "Controller 클래스에 @RestController 어노테이션 필수",
      "pattern": "@RestController.*class.*Controller"
    },
    {
      "ruleId": "REST-CTRL-009",
      "name": "@Validated 필수 (PathVariable/RequestParam 검증시)",
      "severity": "WARNING",
      "description": "PathVariable/RequestParam 검증 시 클래스 레벨에 @Validated 필수",
      "pattern": "@Validated.*class.*Controller"
    },
    {
      "ruleId": "REST-CTRL-010",
      "name": "HTTP 상태 코드 매핑",
      "severity": "WARNING",
      "description": "POST → 201 Created, GET → 200 OK, PATCH → 200 OK",
      "pattern": {
        "POST": "HttpStatus.CREATED",
        "GET": "HttpStatus.OK",
        "PATCH": "HttpStatus.OK"
      }
    },
    {
      "ruleId": "REST-CTRL-011",
      "name": "Javadoc 필수",
      "severity": "WARNING",
      "description": "Controller 클래스 및 public 메서드에 Javadoc 필수 (@author, @since)",
      "pattern": "/\\*\\*.*@author.*@since.*\\*/"
    },
    {
      "ruleId": "REST-CTRL-012",
      "name": "Mapper DI 필수",
      "severity": "ERROR",
      "description": "Mapper를 생성자 주입으로 DI해야 합니다",
      "pattern": "private final \\w+ApiMapper"
    },
    {
      "ruleId": "REST-CTRL-013",
      "name": "UseCase 직접 의존",
      "severity": "ERROR",
      "description": "UseCase를 생성자 주입으로 직접 의존해야 합니다",
      "pattern": "private final \\w+(UseCase|QueryService)"
    },
    {
      "ruleId": "REST-CTRL-014",
      "name": "RESTful URI 설계",
      "severity": "WARNING",
      "description": "URI는 명사 복수형 사용, 동사 금지 (/createOrder ❌ → POST /orders ✅)",
      "antiPattern": "@.*Mapping.*/(create|update|delete|get|find|search)[A-Z]"
    },
    {
      "ruleId": "REST-CTRL-015",
      "name": "Domain 직접 호출 금지",
      "severity": "ERROR",
      "description": "Controller에서 Domain 객체 직접 생성/조작 금지",
      "antiPattern": "import.*\\.domain\\.[a-z]+\\.aggregate\\."
    },
    {
      "ruleId": "REST-CTRL-016",
      "name": "Properties로 경로 관리",
      "severity": "WARNING",
      "description": "엔드포인트는 Properties로 관리, 하드코딩 금지",
      "pattern": "@RequestMapping.*\\$\\{api\\.endpoints\\."
    }
  ]
}
```

## 관련 문서

- `docs/coding_convention/01-adapter-in-layer/rest-api/controller/controller-guide.md`
