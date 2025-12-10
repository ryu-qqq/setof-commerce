# Application Layer - DTO Rules

## COMMAND_DTO (8 rules)

Command DTO (CUD 요청 데이터)

```json
{
  "category": "COMMAND_DTO",
  "rules": [
    {
      "ruleId": "APP-CD-001",
      "name": "public record 필수",
      "severity": "ERROR",
      "description": "Command DTO는 반드시 public record로 선언",
      "pattern": "public\\s+record\\s+\\w+Command\\s*\\(",
      "antiPattern": "(public\\s+class|private\\s+record)\\s+\\w+Command"
    },
    {
      "ruleId": "APP-CD-002",
      "name": "네이밍 컨벤션",
      "severity": "ERROR",
      "description": "Command DTO는 {Action}{Bc}Command 형식",
      "pattern": "record\\s+(Create|Update|Delete|Register|Cancel|Approve|Reject)\\w+Command",
      "antiPattern": "record\\s+\\w+Dto\\s*\\(|record\\s+\\w+Request\\s*\\("
    },
    {
      "ruleId": "APP-CD-003",
      "name": "Lombok 사용 금지",
      "severity": "ERROR",
      "antiPattern": "@(Getter|Setter|Data|Builder).*record\\s+\\w+Command"
    },
    {
      "ruleId": "APP-CD-004",
      "name": "Validation 어노테이션 금지",
      "severity": "ERROR",
      "description": "Command DTO에서 jakarta.validation 어노테이션 금지 (REST API Layer 책임)",
      "antiPattern": "@(NotNull|NotBlank|NotEmpty|Size|Min|Max|Pattern|Email|Valid).*record\\s+\\w+Command"
    },
    {
      "ruleId": "APP-CD-005",
      "name": "비즈니스 로직 금지",
      "severity": "ERROR",
      "antiPattern": "public\\s+\\w+\\s+calculate|public\\s+\\w+\\s+validate|public\\s+boolean\\s+is[A-Z]"
    },
    {
      "ruleId": "APP-CD-006",
      "name": "Compact Constructor 허용 패턴",
      "severity": "WARNING",
      "description": "Compact Constructor에서 List.copyOf()만 허용",
      "pattern": "public\\s+\\w+Command\\s*\\{[^}]*List\\.copyOf\\(",
      "antiPattern": "public\\s+\\w+Command\\s*\\{[^}]*(if|for|while|throw)"
    },
    {
      "ruleId": "APP-CD-007",
      "name": "불변 컬렉션 사용",
      "severity": "WARNING",
      "pattern": "List\\.copyOf\\(\\w+\\)",
      "antiPattern": "new\\s+ArrayList\\<\\>\\(\\w+\\)"
    },
    {
      "ruleId": "APP-CD-008",
      "name": "패키지 위치",
      "severity": "ERROR",
      "pattern": "package\\s+.*\\.application\\.port\\.in\\.dto\\.command;"
    }
  ]
}
```

---

## QUERY_DTO (6 rules)

Query DTO (조회 요청 데이터)

```json
{
  "category": "QUERY_DTO",
  "rules": [
    {
      "ruleId": "APP-QD-001",
      "name": "public record 필수",
      "severity": "ERROR",
      "pattern": "public\\s+record\\s+\\w+Query\\s*\\(",
      "antiPattern": "(public\\s+class|private\\s+record)\\s+\\w+Query"
    },
    {
      "ruleId": "APP-QD-002",
      "name": "네이밍 컨벤션",
      "severity": "ERROR",
      "description": "Get{Bc}Query 또는 Search{Bc}Query 형식",
      "pattern": "record\\s+(Get|Search|Find|List)\\w+Query",
      "antiPattern": "record\\s+\\w+QueryDto\\s*\\("
    },
    {
      "ruleId": "APP-QD-003",
      "name": "Lombok 사용 금지",
      "severity": "ERROR",
      "antiPattern": "@(Getter|Setter|Data|Builder).*record\\s+\\w+Query"
    },
    {
      "ruleId": "APP-QD-004",
      "name": "기본값 처리 금지",
      "severity": "ERROR",
      "description": "Query DTO에서 기본값 처리 금지 (REST API Layer 책임)",
      "antiPattern": "\\w+\\s*==\\s*null\\s*\\?|Objects\\.requireNonNullElse"
    },
    {
      "ruleId": "APP-QD-005",
      "name": "Validation 어노테이션 금지",
      "severity": "ERROR",
      "antiPattern": "@(NotNull|NotBlank|Size|Min|Max).*record\\s+\\w+Query"
    },
    {
      "ruleId": "APP-QD-006",
      "name": "패키지 위치",
      "severity": "ERROR",
      "pattern": "package\\s+.*\\.application\\.port\\.in\\.dto\\.query;"
    }
  ]
}
```

---

## RESPONSE_DTO (6 rules)

Response DTO (응답 데이터)

```json
{
  "category": "RESPONSE_DTO",
  "rules": [
    {
      "ruleId": "APP-RD-001",
      "name": "public record 필수",
      "severity": "ERROR",
      "pattern": "public\\s+record\\s+\\w+Response\\s*\\(",
      "antiPattern": "(public\\s+class|private\\s+record)\\s+\\w+Response"
    },
    {
      "ruleId": "APP-RD-002",
      "name": "네이밍 컨벤션",
      "severity": "ERROR",
      "description": "{Bc}Response 형식",
      "pattern": "record\\s+\\w+Response\\s*\\(",
      "antiPattern": "record\\s+\\w+Dto\\s*\\(|record\\s+\\w+Result\\s*\\("
    },
    {
      "ruleId": "APP-RD-003",
      "name": "Lombok 사용 금지",
      "severity": "ERROR",
      "antiPattern": "@(Getter|Setter|Data|Builder).*record\\s+\\w+Response"
    },
    {
      "ruleId": "APP-RD-004",
      "name": "Jackson 어노테이션 금지",
      "severity": "ERROR",
      "description": "Response DTO에서 Jackson 어노테이션 금지 (REST API Layer 책임)",
      "antiPattern": "@Json(Property|Include|Format|Ignore).*record\\s+\\w+Response"
    },
    {
      "ruleId": "APP-RD-005",
      "name": "비즈니스 로직 금지",
      "severity": "ERROR",
      "antiPattern": "public\\s+\\w+\\s+calculate|public\\s+\\w+\\s+get[A-Z]\\w+\\(\\)"
    },
    {
      "ruleId": "APP-RD-006",
      "name": "패키지 위치",
      "severity": "ERROR",
      "pattern": "package\\s+.*\\.application\\.port\\.in\\.dto\\.response;"
    }
  ]
}
```

---

## PERSIST_BUNDLE (8 rules)

PersistBundle (영속화 객체 묶음)

```json
{
  "category": "PERSIST_BUNDLE",
  "rules": [
    {
      "ruleId": "APP-PB-001",
      "name": "public record 필수",
      "severity": "ERROR",
      "pattern": "public\\s+record\\s+\\w+PersistBundle\\s*\\(",
      "antiPattern": "(public\\s+class|private\\s+record)\\s+\\w+PersistBundle"
    },
    {
      "ruleId": "APP-PB-002",
      "name": "네이밍 컨벤션",
      "severity": "ERROR",
      "description": "{Bc}PersistBundle 형식",
      "pattern": "record\\s+\\w+PersistBundle\\s*\\("
    },
    {
      "ruleId": "APP-PB-003",
      "name": "enrichWithId() 메서드 필수",
      "severity": "ERROR",
      "description": "ID 할당 메서드 필수",
      "pattern": "public\\s+\\w+PersistBundle\\s+enrichWithId\\(\\w+Id\\s+\\w+\\)"
    },
    {
      "ruleId": "APP-PB-004",
      "name": "Domain 객체만 포함",
      "severity": "ERROR",
      "antiPattern": "\\w+Response\\s+\\w+|\\w+Dto\\s+\\w+"
    },
    {
      "ruleId": "APP-PB-005",
      "name": "비즈니스 로직 금지",
      "severity": "ERROR",
      "antiPattern": "public\\s+\\w+\\s+calculate|public\\s+boolean\\s+is[A-Z](?!.*enrichWithId)"
    },
    {
      "ruleId": "APP-PB-006",
      "name": "Lombok 사용 금지",
      "severity": "ERROR",
      "antiPattern": "@(Getter|Setter|Data|Builder).*record\\s+\\w+PersistBundle"
    },
    {
      "ruleId": "APP-PB-007",
      "name": "패키지 위치",
      "severity": "ERROR",
      "pattern": "package\\s+.*\\.application\\.\\w+\\.dto\\.bundle;"
    },
    {
      "ruleId": "APP-PB-008",
      "name": "불변 컬렉션 사용",
      "severity": "WARNING",
      "pattern": "List<\\w+>|Set<\\w+>",
      "antiPattern": "ArrayList<|HashSet<"
    }
  ]
}
```

---

## QUERY_BUNDLE (8 rules)

QueryBundle (조회 결과 묶음)

```json
{
  "category": "QUERY_BUNDLE",
  "rules": [
    {
      "ruleId": "APP-QB-001",
      "name": "public record 필수",
      "severity": "ERROR",
      "pattern": "public\\s+record\\s+\\w+QueryBundle\\s*\\(",
      "antiPattern": "(public\\s+class|private\\s+record)\\s+\\w+QueryBundle"
    },
    {
      "ruleId": "APP-QB-002",
      "name": "네이밍 컨벤션",
      "severity": "ERROR",
      "description": "{Bc}QueryBundle 형식",
      "pattern": "record\\s+\\w+QueryBundle\\s*\\("
    },
    {
      "ruleId": "APP-QB-003",
      "name": "Domain 객체만 포함",
      "severity": "ERROR",
      "antiPattern": "\\w+Response\\s+\\w+|\\w+Dto\\s+\\w+"
    },
    {
      "ruleId": "APP-QB-004",
      "name": "hasXxx() 편의 메서드만 허용",
      "severity": "WARNING",
      "pattern": "public\\s+boolean\\s+has\\w+\\(\\)",
      "antiPattern": "public\\s+\\w+\\s+calculate"
    },
    {
      "ruleId": "APP-QB-005",
      "name": "비즈니스 로직 금지",
      "severity": "ERROR",
      "antiPattern": "public\\s+\\w+\\s+calculate|if\\s*\\(.*[<>=]"
    },
    {
      "ruleId": "APP-QB-006",
      "name": "Lombok 사용 금지",
      "severity": "ERROR",
      "antiPattern": "@(Getter|Setter|Data|Builder).*record\\s+\\w+QueryBundle"
    },
    {
      "ruleId": "APP-QB-007",
      "name": "패키지 위치",
      "severity": "ERROR",
      "pattern": "package\\s+.*\\.application\\.\\w+\\.dto\\.bundle;"
    },
    {
      "ruleId": "APP-QB-008",
      "name": "불변 컬렉션 사용",
      "severity": "WARNING",
      "pattern": "List<\\w+>|Set<\\w+>",
      "antiPattern": "ArrayList<|HashSet<"
    }
  ]
}
```
