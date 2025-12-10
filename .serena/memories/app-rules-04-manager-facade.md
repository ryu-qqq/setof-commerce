# Application Layer - Manager & Facade Rules

## TRANSACTION_MANAGER (7 rules)

단일 PersistencePort 트랜잭션

```json
{
  "category": "TRANSACTION_MANAGER",
  "rules": [
    {
      "ruleId": "APP-TM-001",
      "name": "@Component + @Transactional 클래스 레벨",
      "severity": "ERROR",
      "pattern": "@Component\\s+@Transactional\\s+.*class\\s+\\w+TransactionManager",
      "antiPattern": "@Service\\s+.*class\\s+\\w+TransactionManager"
    },
    {
      "ruleId": "APP-TM-002",
      "name": "네이밍 컨벤션",
      "severity": "ERROR",
      "description": "{Bc}TransactionManager 형식",
      "pattern": "class\\s+\\w+TransactionManager",
      "antiPattern": "class\\s+\\w+TxManager|class\\s+\\w+PersistManager"
    },
    {
      "ruleId": "APP-TM-003",
      "name": "단일 PersistencePort 의존",
      "severity": "ERROR",
      "pattern": "private\\s+final\\s+\\w+PersistencePort\\s+\\w+;",
      "antiPattern": "(private\\s+final\\s+\\w+PersistencePort\\s+\\w+;.*){2,}"
    },
    {
      "ruleId": "APP-TM-004",
      "name": "persist() 메서드만",
      "severity": "ERROR",
      "description": "순수 위임만",
      "pattern": "public\\s+\\w+\\s+persist\\(",
      "antiPattern": "public\\s+\\w+\\s+(save|update|delete|create)\\w*\\("
    },
    {
      "ruleId": "APP-TM-005",
      "name": "비즈니스 로직 금지",
      "severity": "ERROR",
      "antiPattern": "if\\s*\\(|for\\s*\\(|while\\s*\\("
    },
    {
      "ruleId": "APP-TM-006",
      "name": "Lombok 사용 금지",
      "severity": "ERROR",
      "antiPattern": "@(Getter|Setter|Data|Builder|RequiredArgsConstructor).*class\\s+\\w+TransactionManager"
    },
    {
      "ruleId": "APP-TM-007",
      "name": "패키지 위치",
      "severity": "ERROR",
      "pattern": "package\\s+.*\\.application\\.service\\.manager\\.command;"
    }
  ]
}
```

---

## READ_MANAGER (7 rules)

단일 QueryPort 읽기

```json
{
  "category": "READ_MANAGER",
  "rules": [
    {
      "ruleId": "APP-RM-001",
      "name": "@Component 어노테이션",
      "severity": "ERROR",
      "pattern": "@Component\\s+.*class\\s+\\w+ReadManager",
      "antiPattern": "@Service\\s+.*class\\s+\\w+ReadManager"
    },
    {
      "ruleId": "APP-RM-002",
      "name": "@Transactional(readOnly=true) 메서드 레벨",
      "severity": "ERROR",
      "pattern": "@Transactional\\(readOnly\\s*=\\s*true\\)\\s+(public|protected)",
      "antiPattern": "@Transactional\\s+.*class\\s+\\w+ReadManager"
    },
    {
      "ruleId": "APP-RM-003",
      "name": "네이밍 컨벤션",
      "severity": "ERROR",
      "description": "{Bc}ReadManager 형식",
      "pattern": "class\\s+\\w+ReadManager",
      "antiPattern": "class\\s+\\w+QueryManager|class\\s+\\w+Reader"
    },
    {
      "ruleId": "APP-RM-004",
      "name": "단일 QueryPort 의존",
      "severity": "ERROR",
      "pattern": "private\\s+final\\s+\\w+QueryPort\\s+\\w+;",
      "antiPattern": "(private\\s+final\\s+\\w+QueryPort\\s+\\w+;.*){2,}"
    },
    {
      "ruleId": "APP-RM-005",
      "name": "findBy/get 메서드 네이밍",
      "severity": "ERROR",
      "pattern": "public\\s+\\w+\\s+(findBy|get)\\w*\\(",
      "antiPattern": "public\\s+\\w+\\s+(search|query|load)\\w*\\("
    },
    {
      "ruleId": "APP-RM-006",
      "name": "Lombok 사용 금지",
      "severity": "ERROR",
      "antiPattern": "@(Getter|Setter|Data|Builder|RequiredArgsConstructor).*class\\s+\\w+ReadManager"
    },
    {
      "ruleId": "APP-RM-007",
      "name": "패키지 위치",
      "severity": "ERROR",
      "pattern": "package\\s+.*\\.application\\.service\\.manager\\.query;"
    }
  ]
}
```

---

## COMMAND_FACADE (8 rules)

2+ TransactionManager 조율

```json
{
  "category": "COMMAND_FACADE",
  "rules": [
    {
      "ruleId": "APP-CF-001",
      "name": "@Component 어노테이션",
      "severity": "ERROR",
      "description": "@Service 아님",
      "pattern": "@Component\\s+.*class\\s+\\w+Facade",
      "antiPattern": "@Service\\s+.*class\\s+\\w+Facade"
    },
    {
      "ruleId": "APP-CF-002",
      "name": "네이밍 컨벤션",
      "severity": "ERROR",
      "description": "{Action}{Bc}Facade 형식",
      "pattern": "class\\s+(Create|Update|Delete|Register|Cancel)\\w+Facade"
    },
    {
      "ruleId": "APP-CF-003",
      "name": "2+ TransactionManager 의존",
      "severity": "ERROR",
      "pattern": "(private\\s+final\\s+\\w+TransactionManager\\s+\\w+;.*){2,}",
      "antiPattern": "private\\s+final\\s+\\w+PersistencePort"
    },
    {
      "ruleId": "APP-CF-004",
      "name": "@Transactional 메서드 레벨",
      "severity": "ERROR",
      "pattern": "@Transactional\\s+(public|protected)\\s+\\w+\\s+persist",
      "antiPattern": "@Transactional\\s+.*class\\s+\\w+Facade"
    },
    {
      "ruleId": "APP-CF-005",
      "name": "persist 메서드 네이밍",
      "severity": "ERROR",
      "pattern": "public\\s+\\w+\\s+persist\\w*\\(",
      "antiPattern": "public\\s+\\w+\\s+(save|create|update|delete)\\w*\\("
    },
    {
      "ruleId": "APP-CF-006",
      "name": "객체 생성 금지",
      "severity": "ERROR",
      "description": "Factory 책임",
      "antiPattern": "new\\s+\\w+(Aggregate|Entity|Domain)\\("
    },
    {
      "ruleId": "APP-CF-007",
      "name": "Lombok 사용 금지",
      "severity": "ERROR",
      "antiPattern": "@(Getter|Setter|Data|Builder|RequiredArgsConstructor).*class\\s+\\w+Facade"
    },
    {
      "ruleId": "APP-CF-008",
      "name": "패키지 위치",
      "severity": "ERROR",
      "pattern": "package\\s+.*\\.application\\.service\\.facade\\.command;"
    }
  ]
}
```

---

## QUERY_FACADE (7 rules)

2+ ReadManager 조율

```json
{
  "category": "QUERY_FACADE",
  "rules": [
    {
      "ruleId": "APP-QF-001",
      "name": "@Component 어노테이션",
      "severity": "ERROR",
      "pattern": "@Component\\s+.*class\\s+\\w+(QueryFacade|ReadFacade)",
      "antiPattern": "@Service\\s+.*class\\s+\\w+(QueryFacade|ReadFacade)"
    },
    {
      "ruleId": "APP-QF-002",
      "name": "네이밍 컨벤션",
      "severity": "ERROR",
      "description": "{Bc}QueryFacade 또는 {Bc}ReadFacade 형식",
      "pattern": "class\\s+\\w+(QueryFacade|ReadFacade)"
    },
    {
      "ruleId": "APP-QF-003",
      "name": "2+ ReadManager 의존",
      "severity": "ERROR",
      "pattern": "(private\\s+final\\s+\\w+ReadManager\\s+\\w+;.*){2,}",
      "antiPattern": "private\\s+final\\s+\\w+QueryPort"
    },
    {
      "ruleId": "APP-QF-004",
      "name": "@Transactional(readOnly=true) 메서드 레벨",
      "severity": "ERROR",
      "pattern": "@Transactional\\(readOnly\\s*=\\s*true\\)\\s+(public|protected)",
      "antiPattern": "@Transactional\\s+.*class\\s+\\w+(QueryFacade|ReadFacade)"
    },
    {
      "ruleId": "APP-QF-005",
      "name": "fetch 메서드 네이밍",
      "severity": "ERROR",
      "pattern": "public\\s+\\w+\\s+fetch\\w*\\(",
      "antiPattern": "public\\s+\\w+\\s+(get|find|search|query)\\w*\\("
    },
    {
      "ruleId": "APP-QF-006",
      "name": "QueryBundle 반환",
      "severity": "WARNING",
      "pattern": "\\w+QueryBundle\\s+fetch\\w*\\("
    },
    {
      "ruleId": "APP-QF-007",
      "name": "패키지 위치",
      "severity": "ERROR",
      "pattern": "package\\s+.*\\.application\\.service\\.facade\\.query;"
    }
  ]
}
```
