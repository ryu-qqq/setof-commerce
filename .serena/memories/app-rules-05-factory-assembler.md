# Application Layer - Factory & Assembler Rules

## COMMAND_FACTORY (8 rules)

Command → Domain 변환

```json
{
  "category": "COMMAND_FACTORY",
  "rules": [
    {
      "ruleId": "APP-CMF-001",
      "name": "@Component 어노테이션",
      "severity": "ERROR",
      "pattern": "@Component\\s+.*class\\s+\\w+CommandFactory",
      "antiPattern": "@Service\\s+.*class\\s+\\w+CommandFactory"
    },
    {
      "ruleId": "APP-CMF-002",
      "name": "네이밍 컨벤션",
      "severity": "ERROR",
      "description": "{Bc}CommandFactory 형식",
      "pattern": "class\\s+\\w+CommandFactory",
      "antiPattern": "class\\s+\\w+Builder"
    },
    {
      "ruleId": "APP-CMF-003",
      "name": "create 메서드 네이밍",
      "severity": "ERROR",
      "pattern": "public\\s+\\w+\\s+create\\w*\\(",
      "antiPattern": "public\\s+\\w+\\s+(build|make|new)\\w*\\("
    },
    {
      "ruleId": "APP-CMF-004",
      "name": "createBundle 메서드",
      "severity": "WARNING",
      "description": "다중 객체 생성 시 권장",
      "pattern": "public\\s+\\w+Bundle\\s+createBundle\\("
    },
    {
      "ruleId": "APP-CMF-005",
      "name": "Port 호출 금지",
      "severity": "ERROR",
      "antiPattern": "\\w+Port\\.\\w+\\("
    },
    {
      "ruleId": "APP-CMF-006",
      "name": "@Transactional 금지",
      "severity": "ERROR",
      "antiPattern": "@Transactional.*class\\s+\\w+CommandFactory"
    },
    {
      "ruleId": "APP-CMF-007",
      "name": "Lombok 사용 금지",
      "severity": "ERROR",
      "antiPattern": "@(Getter|Setter|Data|Builder|RequiredArgsConstructor).*class\\s+\\w+CommandFactory"
    },
    {
      "ruleId": "APP-CMF-008",
      "name": "패키지 위치",
      "severity": "ERROR",
      "pattern": "package\\s+.*\\.application\\.service\\.factory\\.command;"
    }
  ]
}
```

---

## QUERY_FACTORY (5 rules)

Query → Criteria 변환

```json
{
  "category": "QUERY_FACTORY",
  "rules": [
    {
      "ruleId": "APP-QYF-001",
      "name": "@Component 어노테이션",
      "severity": "ERROR",
      "pattern": "@Component\\s+.*class\\s+\\w+QueryFactory",
      "antiPattern": "@Service\\s+.*class\\s+\\w+QueryFactory"
    },
    {
      "ruleId": "APP-QYF-002",
      "name": "네이밍 컨벤션",
      "severity": "ERROR",
      "description": "{Bc}QueryFactory 형식",
      "pattern": "class\\s+\\w+QueryFactory",
      "antiPattern": "class\\s+\\w+CriteriaBuilder"
    },
    {
      "ruleId": "APP-QYF-003",
      "name": "createCriteria 메서드 네이밍",
      "severity": "ERROR",
      "pattern": "public\\s+\\w+Criteria\\s+createCriteria\\w*\\(",
      "antiPattern": "public\\s+\\w+\\s+(build|make)Criteria\\w*\\("
    },
    {
      "ruleId": "APP-QYF-004",
      "name": "Lombok 사용 금지",
      "severity": "ERROR",
      "antiPattern": "@(Getter|Setter|Data|Builder|RequiredArgsConstructor).*class\\s+\\w+QueryFactory"
    },
    {
      "ruleId": "APP-QYF-005",
      "name": "패키지 위치",
      "severity": "ERROR",
      "pattern": "package\\s+.*\\.application\\.service\\.factory\\.query;"
    }
  ]
}
```

---

## ASSEMBLER (7 rules)

Domain → Response 변환

```json
{
  "category": "ASSEMBLER",
  "rules": [
    {
      "ruleId": "APP-AS-001",
      "name": "@Component 어노테이션",
      "severity": "ERROR",
      "pattern": "@Component\\s+.*class\\s+\\w+Assembler",
      "antiPattern": "@Service\\s+.*class\\s+\\w+Assembler"
    },
    {
      "ruleId": "APP-AS-002",
      "name": "네이밍 컨벤션",
      "severity": "ERROR",
      "description": "{Bc}Assembler 형식",
      "pattern": "class\\s+\\w+Assembler",
      "antiPattern": "class\\s+\\w+Mapper|class\\s+\\w+Converter"
    },
    {
      "ruleId": "APP-AS-003",
      "name": "Domain → Response 변환만",
      "severity": "ERROR",
      "description": "toResponse()만 허용, toDomain 금지!",
      "pattern": "public\\s+\\w+Response\\s+toResponse\\(",
      "antiPattern": "public\\s+\\w+\\s+toDomain\\(|public\\s+\\w+\\s+toEntity\\("
    },
    {
      "ruleId": "APP-AS-004",
      "name": "Port 의존 금지",
      "severity": "ERROR",
      "antiPattern": "\\w+Port\\s+\\w+;"
    },
    {
      "ruleId": "APP-AS-005",
      "name": "비즈니스 로직 금지",
      "severity": "ERROR",
      "description": "순수 변환만",
      "antiPattern": "if\\s*\\(.*\\.get\\w+\\(\\).*[<>=]"
    },
    {
      "ruleId": "APP-AS-006",
      "name": "Lombok 사용 금지",
      "severity": "ERROR",
      "antiPattern": "@(Getter|Setter|Data|Builder|RequiredArgsConstructor).*class\\s+\\w+Assembler"
    },
    {
      "ruleId": "APP-AS-007",
      "name": "패키지 위치",
      "severity": "ERROR",
      "pattern": "package\\s+.*\\.application\\.service\\.assembler;"
    }
  ]
}
```
