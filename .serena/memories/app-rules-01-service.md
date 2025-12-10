# Application Layer - Service Rules

## COMMAND_SERVICE (9 rules)

Command UseCase 구현체 (CUD 연산)

```json
{
  "category": "COMMAND_SERVICE",
  "rules": [
    {
      "ruleId": "APP-CS-001",
      "name": "CommandService 클래스 어노테이션",
      "severity": "ERROR",
      "description": "CommandService는 반드시 @Service 어노테이션을 사용해야 함",
      "pattern": "@Service\\s+.*class\\s+\\w+CommandService",
      "antiPattern": "class\\s+\\w+CommandService(?!.*@Service)"
    },
    {
      "ruleId": "APP-CS-002",
      "name": "UseCase 인터페이스 구현",
      "severity": "ERROR",
      "description": "CommandService는 반드시 UseCase 인터페이스를 구현해야 함",
      "pattern": "class\\s+\\w+CommandService\\s+implements\\s+\\w+UseCase",
      "antiPattern": "class\\s+\\w+CommandService(?!.*implements.*UseCase)"
    },
    {
      "ruleId": "APP-CS-003",
      "name": "@Transactional 사용 금지",
      "severity": "ERROR",
      "description": "CommandService에서 @Transactional 직접 사용 금지 (Facade/Manager 책임)",
      "antiPattern": "@Transactional.*class\\s+\\w+CommandService"
    },
    {
      "ruleId": "APP-CS-004",
      "name": "Port 직접 호출 금지",
      "severity": "ERROR",
      "description": "CommandService에서 Port 직접 호출 금지 (Facade/Manager 통해서만)",
      "antiPattern": "(PersistencePort|QueryPort)\\s+\\w+;.*class\\s+\\w+CommandService"
    },
    {
      "ruleId": "APP-CS-005",
      "name": "execute() 메서드 필수",
      "severity": "ERROR",
      "description": "CommandService는 UseCase의 execute() 메서드를 구현해야 함",
      "pattern": "@Override.*public\\s+\\w+\\s+execute\\("
    },
    {
      "ruleId": "APP-CS-006",
      "name": "생성자 주입 필수",
      "severity": "ERROR",
      "description": "의존성은 생성자 주입만 허용 (@Autowired 필드 주입 금지)",
      "pattern": "private\\s+final\\s+\\w+\\s+\\w+;",
      "antiPattern": "@Autowired\\s+private"
    },
    {
      "ruleId": "APP-CS-007",
      "name": "Factory 사용 필수",
      "severity": "WARNING",
      "description": "도메인 객체 생성 시 Factory 사용 권장",
      "pattern": "\\w+Factory\\s+\\w+Factory",
      "antiPattern": "new\\s+\\w+(Aggregate|Entity)\\("
    },
    {
      "ruleId": "APP-CS-008",
      "name": "Lombok 사용 금지",
      "severity": "ERROR",
      "description": "CommandService에서 Lombok 어노테이션 사용 금지",
      "antiPattern": "@(Getter|Setter|Data|Builder|RequiredArgsConstructor).*class\\s+\\w+CommandService"
    },
    {
      "ruleId": "APP-CS-009",
      "name": "비즈니스 로직 금지",
      "severity": "WARNING",
      "description": "Application Layer에서 비즈니스 로직 금지 (Domain 책임)",
      "antiPattern": "if\\s*\\(.*\\.get\\w+\\(\\).*[<>=]"
    }
  ]
}
```

---

## QUERY_SERVICE (8 rules)

Query UseCase 구현체 (Read 연산)

```json
{
  "category": "QUERY_SERVICE",
  "rules": [
    {
      "ruleId": "APP-QS-001",
      "name": "QueryService 클래스 어노테이션",
      "severity": "ERROR",
      "description": "QueryService는 반드시 @Service 어노테이션을 사용해야 함",
      "pattern": "@Service\\s+.*class\\s+\\w+QueryService",
      "antiPattern": "class\\s+\\w+QueryService(?!.*@Service)"
    },
    {
      "ruleId": "APP-QS-002",
      "name": "UseCase 인터페이스 구현",
      "severity": "ERROR",
      "description": "QueryService는 반드시 UseCase 인터페이스를 구현해야 함",
      "pattern": "class\\s+\\w+QueryService\\s+implements\\s+\\w+UseCase",
      "antiPattern": "class\\s+\\w+QueryService(?!.*implements.*UseCase)"
    },
    {
      "ruleId": "APP-QS-003",
      "name": "@Transactional 사용 금지",
      "severity": "ERROR",
      "description": "QueryService에서 @Transactional 사용 금지 (읽기 전용, 불필요)",
      "antiPattern": "@Transactional.*class\\s+\\w+QueryService"
    },
    {
      "ruleId": "APP-QS-004",
      "name": "Port 직접 호출 금지",
      "severity": "ERROR",
      "description": "QueryService에서 Port 직접 호출 금지 (ReadManager 통해서만)",
      "antiPattern": "QueryPort\\s+\\w+;.*class\\s+\\w+QueryService"
    },
    {
      "ruleId": "APP-QS-005",
      "name": "execute() 메서드 필수",
      "severity": "ERROR",
      "description": "QueryService는 UseCase의 execute() 메서드를 구현해야 함",
      "pattern": "@Override.*public\\s+\\w+\\s+execute\\("
    },
    {
      "ruleId": "APP-QS-006",
      "name": "생성자 주입 필수",
      "severity": "ERROR",
      "description": "의존성은 생성자 주입만 허용",
      "pattern": "private\\s+final\\s+\\w+\\s+\\w+;",
      "antiPattern": "@Autowired\\s+private"
    },
    {
      "ruleId": "APP-QS-007",
      "name": "Assembler 사용 필수",
      "severity": "WARNING",
      "description": "Domain → Response 변환 시 Assembler 사용 권장",
      "pattern": "\\w+Assembler\\s+\\w+Assembler",
      "antiPattern": "new\\s+\\w+Response\\("
    },
    {
      "ruleId": "APP-QS-008",
      "name": "Lombok 사용 금지",
      "severity": "ERROR",
      "description": "QueryService에서 Lombok 어노테이션 사용 금지",
      "antiPattern": "@(Getter|Setter|Data|Builder|RequiredArgsConstructor).*class\\s+\\w+QueryService"
    }
  ]
}
```
