# Application Layer - Port Rules

## PORT_IN_COMMAND (5 rules)

Command UseCase 인터페이스

```json
{
  "category": "PORT_IN_COMMAND",
  "rules": [
    {
      "ruleId": "APP-PIC-001",
      "name": "인터페이스 네이밍",
      "severity": "ERROR",
      "description": "{Action}{Bc}UseCase 형식",
      "pattern": "interface\\s+(Create|Update|Delete|Register|Cancel|Approve|Reject)\\w+UseCase",
      "antiPattern": "interface\\s+\\w+CommandService|interface\\s+\\w+Port"
    },
    {
      "ruleId": "APP-PIC-002",
      "name": "단일 메서드 원칙",
      "severity": "ERROR",
      "description": "UseCase 인터페이스는 execute() 메서드만 정의",
      "pattern": "interface\\s+\\w+UseCase\\s*\\{[^}]*execute\\([^}]*\\}"
    },
    {
      "ruleId": "APP-PIC-003",
      "name": "@Transactional 금지",
      "severity": "ERROR",
      "antiPattern": "@Transactional.*interface\\s+\\w+UseCase"
    },
    {
      "ruleId": "APP-PIC-004",
      "name": "패키지 위치",
      "severity": "ERROR",
      "pattern": "package\\s+.*\\.application\\.port\\.in\\.usecase\\.command;"
    },
    {
      "ruleId": "APP-PIC-005",
      "name": "Command DTO 파라미터",
      "severity": "ERROR",
      "pattern": "execute\\(\\w+Command\\s+\\w+\\)",
      "antiPattern": "execute\\(\\w+Request\\s+|execute\\(\\w+Dto\\s+"
    }
  ]
}
```

---

## PORT_IN_QUERY (5 rules)

Query UseCase 인터페이스

```json
{
  "category": "PORT_IN_QUERY",
  "rules": [
    {
      "ruleId": "APP-PIQ-001",
      "name": "인터페이스 네이밍",
      "severity": "ERROR",
      "description": "{Action}{Bc}UseCase 형식",
      "pattern": "interface\\s+(Get|Search|Find|List)\\w+UseCase",
      "antiPattern": "interface\\s+\\w+QueryService"
    },
    {
      "ruleId": "APP-PIQ-002",
      "name": "단일 메서드 원칙",
      "severity": "ERROR",
      "pattern": "interface\\s+\\w+UseCase\\s*\\{[^}]*execute\\([^}]*\\}"
    },
    {
      "ruleId": "APP-PIQ-003",
      "name": "Response DTO 반환",
      "severity": "ERROR",
      "pattern": "\\w+Response\\s+execute\\(|Page<\\w+Response>|Slice<\\w+Response>",
      "antiPattern": "\\w+Entity\\s+execute\\(|\\w+Domain\\s+execute\\("
    },
    {
      "ruleId": "APP-PIQ-004",
      "name": "패키지 위치",
      "severity": "ERROR",
      "pattern": "package\\s+.*\\.application\\.port\\.in\\.usecase\\.query;"
    },
    {
      "ruleId": "APP-PIQ-005",
      "name": "Query DTO 파라미터",
      "severity": "ERROR",
      "pattern": "execute\\(\\w+Query\\s+\\w+\\)",
      "antiPattern": "execute\\(\\w+Request\\s+"
    }
  ]
}
```

---

## PORT_OUT_COMMAND (5 rules)

PersistencePort 인터페이스

```json
{
  "category": "PORT_OUT_COMMAND",
  "rules": [
    {
      "ruleId": "APP-POC-001",
      "name": "인터페이스 네이밍",
      "severity": "ERROR",
      "description": "{Bc}PersistencePort 형식",
      "pattern": "interface\\s+\\w+PersistencePort",
      "antiPattern": "interface\\s+\\w+Repository|interface\\s+\\w+CommandPort"
    },
    {
      "ruleId": "APP-POC-002",
      "name": "persist() 메서드만 허용",
      "severity": "ERROR",
      "pattern": "\\w+\\s+persist\\(\\w+\\s+\\w+\\)",
      "antiPattern": "(save|update|delete|insert)\\s*\\("
    },
    {
      "ruleId": "APP-POC-003",
      "name": "Domain Aggregate 파라미터",
      "severity": "ERROR",
      "antiPattern": "persist\\(\\w+Entity\\s+|persist\\(\\w+Dto\\s+"
    },
    {
      "ruleId": "APP-POC-004",
      "name": "Value Object 반환",
      "severity": "WARNING",
      "description": "persist()는 Value Object (ID) 반환 권장",
      "pattern": "\\w+Id\\s+persist\\(",
      "antiPattern": "void\\s+persist\\("
    },
    {
      "ruleId": "APP-POC-005",
      "name": "패키지 위치",
      "severity": "ERROR",
      "pattern": "package\\s+.*\\.application\\.port\\.out;"
    }
  ]
}
```

---

## PORT_OUT_QUERY (5 rules)

QueryPort 인터페이스

```json
{
  "category": "PORT_OUT_QUERY",
  "rules": [
    {
      "ruleId": "APP-POQ-001",
      "name": "인터페이스 네이밍",
      "severity": "ERROR",
      "description": "{Bc}QueryPort 형식",
      "pattern": "interface\\s+\\w+QueryPort",
      "antiPattern": "interface\\s+\\w+Repository"
    },
    {
      "ruleId": "APP-POQ-002",
      "name": "허용 메서드 패턴",
      "severity": "ERROR",
      "description": "findById, existsById, findByCriteria, countByCriteria만 허용",
      "pattern": "(findById|existsById|findByCriteria|countByCriteria)\\s*\\(",
      "antiPattern": "(get|search|query|load)\\w*\\s*\\("
    },
    {
      "ruleId": "APP-POQ-003",
      "name": "Optional 반환 (단건 조회)",
      "severity": "ERROR",
      "pattern": "Optional<\\w+>\\s+findById\\("
    },
    {
      "ruleId": "APP-POQ-004",
      "name": "Criteria 파라미터",
      "severity": "WARNING",
      "pattern": "findByCriteria\\(\\w+Criteria\\s+\\w+\\)",
      "antiPattern": "findByCriteria\\(String\\s+"
    },
    {
      "ruleId": "APP-POQ-005",
      "name": "패키지 위치",
      "severity": "ERROR",
      "pattern": "package\\s+.*\\.application\\.port\\.out;"
    }
  ]
}
```

---

## CACHE_QUERY_PORT (7 rules)

도메인 특화 캐시 포트

```json
{
  "category": "CACHE_QUERY_PORT",
  "rules": [
    {
      "ruleId": "APP-CQP-001",
      "name": "인터페이스 네이밍",
      "severity": "ERROR",
      "description": "{Bc}CacheQueryPort 형식",
      "pattern": "interface\\s+\\w+CacheQueryPort",
      "antiPattern": "interface\\s+CachePort"
    },
    {
      "ruleId": "APP-CQP-002",
      "name": "도메인 ID 파라미터",
      "severity": "ERROR",
      "description": "String 키 금지, 도메인 ID 사용",
      "pattern": "(findById|save|evict)\\(Long\\s+\\w+Id\\)",
      "antiPattern": "(findById|save|evict)\\(String\\s+key\\)"
    },
    {
      "ruleId": "APP-CQP-003",
      "name": "Optional 반환",
      "severity": "ERROR",
      "pattern": "Optional<\\w+>\\s+findById\\("
    },
    {
      "ruleId": "APP-CQP-004",
      "name": "허용 메서드 패턴",
      "severity": "ERROR",
      "description": "findById, save, evict 메서드만",
      "pattern": "(findById|save|evict|evictBy\\w+)\\s*\\(",
      "antiPattern": "(get|set|delete|remove|put)\\s*\\("
    },
    {
      "ruleId": "APP-CQP-005",
      "name": "@Transactional 금지",
      "severity": "ERROR",
      "antiPattern": "@Transactional.*class\\s+\\w+CacheAdapter"
    },
    {
      "ruleId": "APP-CQP-006",
      "name": "패키지 위치",
      "severity": "ERROR",
      "pattern": "package\\s+.*\\.application\\.\\w+\\.port\\.out;"
    },
    {
      "ruleId": "APP-CQP-007",
      "name": "Domain 타입 반환",
      "severity": "ERROR",
      "pattern": "Optional<\\w+>|List<\\w+>",
      "antiPattern": "Optional<String>|Map<String,\\s*Object>"
    }
  ]
}
```

---

## DISTRIBUTED_LOCK_PORT (6 rules)

Cross-cutting 분산락 포트

```json
{
  "category": "DISTRIBUTED_LOCK_PORT",
  "rules": [
    {
      "ruleId": "APP-DLP-001",
      "name": "인터페이스 네이밍",
      "severity": "ERROR",
      "description": "정확히 DistributedLockPort 이름 사용",
      "pattern": "interface\\s+DistributedLockPort",
      "antiPattern": "interface\\s+\\w+LockPort|interface\\s+LockService"
    },
    {
      "ruleId": "APP-DLP-002",
      "name": "LockKey VO 사용",
      "severity": "ERROR",
      "description": "String 금지, LockKey VO 필수",
      "pattern": "(tryLock|unlock)\\(LockKey\\s+\\w+",
      "antiPattern": "(tryLock|unlock)\\(String\\s+"
    },
    {
      "ruleId": "APP-DLP-003",
      "name": "tryLock 시그니처",
      "severity": "ERROR",
      "description": "(LockKey, waitTime, leaseTime, TimeUnit) 시그니처",
      "pattern": "boolean\\s+tryLock\\(LockKey\\s+\\w+,\\s*long\\s+\\w+,\\s*long\\s+\\w+,\\s*TimeUnit"
    },
    {
      "ruleId": "APP-DLP-004",
      "name": "패키지 위치 (Cross-cutting)",
      "severity": "ERROR",
      "description": "common.port.out 패키지",
      "pattern": "package\\s+.*\\.common\\.port\\.out;|package\\s+.*\\.application\\.common\\.port\\.out;"
    },
    {
      "ruleId": "APP-DLP-005",
      "name": "@Transactional 금지",
      "severity": "ERROR",
      "antiPattern": "@Transactional.*class\\s+\\w+LockAdapter"
    },
    {
      "ruleId": "APP-DLP-006",
      "name": "isHeldByCurrentThread 메서드",
      "severity": "WARNING",
      "pattern": "boolean\\s+isHeldByCurrentThread\\(LockKey"
    }
  ]
}
```

---

## LOCK_KEY (6 rules)

분산락 키 Value Object

```json
{
  "category": "LOCK_KEY",
  "rules": [
    {
      "ruleId": "APP-LK-001",
      "name": "LockKey 인터페이스 구현",
      "severity": "ERROR",
      "pattern": "record\\s+\\w+LockKey.*implements\\s+LockKey",
      "antiPattern": "record\\s+\\w+LockKey(?!.*implements\\s+LockKey)"
    },
    {
      "ruleId": "APP-LK-002",
      "name": "record 타입 필수",
      "severity": "ERROR",
      "pattern": "public\\s+record\\s+\\w+LockKey",
      "antiPattern": "public\\s+class\\s+\\w+LockKey"
    },
    {
      "ruleId": "APP-LK-003",
      "name": "value() 메서드 구현",
      "severity": "ERROR",
      "pattern": "@Override\\s+public\\s+String\\s+value\\(\\)"
    },
    {
      "ruleId": "APP-LK-004",
      "name": "키 형식 규칙",
      "severity": "ERROR",
      "description": "lock:{domain}:{id} 패턴",
      "pattern": "\"lock:\" \\+ \\w+|\"lock:\\w+:\"",
      "antiPattern": "\"cache:\".*LockKey"
    },
    {
      "ruleId": "APP-LK-005",
      "name": "Compact Constructor 검증",
      "severity": "ERROR",
      "pattern": "public\\s+\\w+LockKey\\s*\\{[^}]*(if|Objects\\.requireNonNull)"
    },
    {
      "ruleId": "APP-LK-006",
      "name": "패키지 위치",
      "severity": "ERROR",
      "description": "domain.{bc}.vo 패키지",
      "pattern": "package\\s+.*\\.domain\\.\\w+\\.vo;",
      "antiPattern": "package\\s+.*\\.application\\."
    }
  ]
}
```
