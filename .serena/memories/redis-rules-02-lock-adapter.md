# Persistence Redis Layer - Lock Adapter Rules

## LOCK_ADAPTER_STRUCTURE (4 rules)

Lock Adapter 클래스 구조 규칙

```json
{
  "category": "LOCK_ADAPTER_STRUCTURE",
  "rules": [
    {
      "ruleId": "REDIS-LA-001",
      "name": "LockAdapter는 클래스여야 함",
      "severity": "ERROR",
      "description": "Lock Adapter는 클래스로 정의되어야 합니다 (인터페이스 금지)",
      "pattern": "class\\s+\\w+LockAdapter",
      "antiPattern": "interface\\s+\\w+LockAdapter",
      "archUnitTest": "DistributedLockAdapterArchTest.lockAdapter_MustBeClass"
    },
    {
      "ruleId": "REDIS-LA-002",
      "name": "@Component 어노테이션 필수",
      "severity": "ERROR",
      "description": "Lock Adapter는 @Component 어노테이션으로 Spring Bean 등록",
      "pattern": "@Component.*class\\s+\\w+LockAdapter",
      "antiPattern": "class\\s+\\w+LockAdapter(?!.*@Component)",
      "archUnitTest": "DistributedLockAdapterArchTest.lockAdapter_MustHaveComponentAnnotation"
    },
    {
      "ruleId": "REDIS-LA-003",
      "name": "DistributedLockPort 인터페이스 구현 필수",
      "severity": "ERROR",
      "description": "Lock Adapter는 DistributedLockPort 인터페이스를 구현해야 함",
      "pattern": "class\\s+\\w+LockAdapter\\s+implements\\s+.*LockPort",
      "antiPattern": "class\\s+\\w+LockAdapter(?!.*implements.*LockPort)",
      "archUnitTest": "DistributedLockAdapterArchTest.lockAdapter_MustImplementDistributedLockPort"
    },
    {
      "ruleId": "REDIS-LA-004",
      "name": "모든 필드 final 필수",
      "severity": "ERROR",
      "description": "Lock Adapter의 모든 인스턴스 필드는 final로 불변성 보장",
      "pattern": "private\\s+final\\s+\\w+",
      "antiPattern": "private\\s+(?!final)\\w+\\s+\\w+;",
      "archUnitTest": "DistributedLockAdapterArchTest.lockAdapter_AllFieldsMustBeFinal"
    }
  ]
}
```

---

## LOCK_ADAPTER_DEPENDENCY (2 rules)

Lock Adapter 의존성 규칙

```json
{
  "category": "LOCK_ADAPTER_DEPENDENCY",
  "rules": [
    {
      "ruleId": "REDIS-LA-005",
      "name": "RedissonClient 의존성 필수",
      "severity": "ERROR",
      "description": "Lock Adapter는 RedissonClient 의존성이 필수 (Pub/Sub 기반 Lock)",
      "pattern": "RedissonClient.*\\w+;",
      "rationale": "Redisson의 Pub/Sub 기반 분산락 사용",
      "archUnitTest": "DistributedLockAdapterArchTest.lockAdapter_MustDependOnRedissonClient"
    },
    {
      "ruleId": "REDIS-LA-006",
      "name": "RedisTemplate 의존성 금지",
      "severity": "ERROR",
      "description": "Lock Adapter에서 RedisTemplate 의존 금지 (Cache 전용)",
      "antiPattern": "RedisTemplate.*\\w+;",
      "rationale": "역할 분리: Lettuce=캐시, Redisson=분산락",
      "archUnitTest": "DistributedLockAdapterArchTest.lockAdapter_MustNotDependOnRedisTemplate"
    }
  ]
}
```

---

## LOCK_ADAPTER_METHOD (4 rules)

Lock Adapter 메서드 규칙

```json
{
  "category": "LOCK_ADAPTER_METHOD",
  "rules": [
    {
      "ruleId": "REDIS-LA-007",
      "name": "tryLock 메서드 필수",
      "severity": "ERROR",
      "description": "분산락 획득 메서드 tryLock이 필수",
      "pattern": "(public|boolean)\\s+tryLock\\(",
      "rationale": "타임아웃 기반 Lock 획득 시도",
      "archUnitTest": "DistributedLockAdapterArchTest.lockAdapter_MustHaveTryLockMethod"
    },
    {
      "ruleId": "REDIS-LA-008",
      "name": "unlock 메서드 필수",
      "severity": "ERROR",
      "description": "분산락 해제 메서드 unlock이 필수",
      "pattern": "(public|void)\\s+unlock\\(",
      "rationale": "Lock 해제를 위한 메서드",
      "archUnitTest": "DistributedLockAdapterArchTest.lockAdapter_MustHaveUnlockMethod"
    },
    {
      "ruleId": "REDIS-LA-009",
      "name": "isHeldByCurrentThread 메서드 필수",
      "severity": "ERROR",
      "description": "현재 스레드 Lock 보유 확인 메서드가 필수",
      "pattern": "(public|boolean)\\s+isHeldByCurrentThread\\(",
      "rationale": "unlock 전 현재 스레드 Lock 보유 확인",
      "archUnitTest": "DistributedLockAdapterArchTest.lockAdapter_MustHaveIsHeldByCurrentThreadMethod"
    },
    {
      "ruleId": "REDIS-LA-010",
      "name": "isLocked 메서드 필수",
      "severity": "ERROR",
      "description": "Lock 상태 확인 메서드가 필수",
      "pattern": "(public|boolean)\\s+isLocked\\(",
      "rationale": "Lock 상태 확인을 위한 메서드",
      "archUnitTest": "DistributedLockAdapterArchTest.lockAdapter_MustHaveIsLockedMethod"
    }
  ]
}
```

---

## LOCK_ADAPTER_PROHIBITION (5 rules)

Lock Adapter 금지 사항 규칙

```json
{
  "category": "LOCK_ADAPTER_PROHIBITION",
  "rules": [
    {
      "ruleId": "REDIS-LA-011",
      "name": "@Transactional 사용 금지",
      "severity": "ERROR",
      "description": "Lock Adapter에서 @Transactional 사용 금지",
      "antiPattern": "@Transactional.*class\\s+\\w+LockAdapter",
      "rationale": "Redis는 Spring Transaction과 무관",
      "archUnitTest": "DistributedLockAdapterArchTest.lockAdapter_MustNotBeTransactional"
    },
    {
      "ruleId": "REDIS-LA-012",
      "name": "비즈니스 로직 포함 금지",
      "severity": "ERROR",
      "description": "Lock Adapter에서 Domain 의존 및 비즈니스 로직 금지",
      "antiPattern": "import.*\\.domain\\.(?!common\\.vo)",
      "rationale": "Lock Adapter는 Lock 획득/해제만 담당",
      "archUnitTest": "DistributedLockAdapterArchTest.lockAdapter_MustNotContainBusinessLogic"
    },
    {
      "ruleId": "REDIS-LA-013",
      "name": "DB 접근 금지",
      "severity": "ERROR",
      "description": "Lock Adapter에서 DB 직접 접근 금지",
      "antiPattern": "(Repository|JpaRepository|EntityManager)",
      "rationale": "Lock과 DB 역할 분리",
      "archUnitTest": "DistributedLockAdapterArchTest.lockAdapter_MustNotAccessDatabase"
    },
    {
      "ruleId": "REDIS-LA-014",
      "name": "Logger 직접 사용 금지",
      "severity": "WARNING",
      "description": "Lock Adapter에서 Logger 직접 사용 금지 (AOP 처리)",
      "antiPattern": "Logger.*\\w+;|log\\.(info|debug|warn|error)",
      "rationale": "로깅은 AOP로 일괄 처리",
      "archUnitTest": "DistributedLockAdapterArchTest.lockAdapter_MustNotContainLogging"
    },
    {
      "ruleId": "REDIS-LA-015",
      "name": "스핀락 패턴 금지",
      "severity": "ERROR",
      "description": "Thread.sleep, wait 등 스핀락 패턴 금지",
      "antiPattern": "(Thread\\.sleep|wait|sleep)\\(",
      "rationale": "Redisson Pub/Sub 기반 Lock 사용 (스핀락 금지)",
      "archUnitTest": "DistributedLockAdapterArchTest.lockAdapter_MustNotUseSpinlock"
    }
  ]
}
```

---

## LOCK_ADAPTER_SAFETY (1 rule)

Lock Adapter 안전성 규칙

```json
{
  "category": "LOCK_ADAPTER_SAFETY",
  "rules": [
    {
      "ruleId": "REDIS-LA-016",
      "name": "Lock 캐시 사용 필수 (Thread-safe)",
      "severity": "ERROR",
      "description": "같은 키에 대해 동일한 Lock 인스턴스 반환을 위해 ConcurrentHashMap 캐시 필수",
      "pattern": "ConcurrentHashMap.*lockCache|locks",
      "rationale": "Thread-safe Lock 인스턴스 캐싱",
      "archUnitTest": "DistributedLockAdapterArchTest.lockAdapter_MustUseLockCache"
    }
  ]
}
```

---

## LockKey VO 사용 규칙

### Domain Layer 인터페이스
```java
// domain/src/main/java/.../common/vo/LockKey.java
public interface LockKey {
    String value();  // "lock:{domain}:{entity}:{id}" 형식
}
```

### 구현 예시
```java
public record OrderLockKey(Long orderId) implements LockKey {
    private static final String PREFIX = "lock:order:";
    
    public OrderLockKey {
        if (orderId == null || orderId <= 0) {
            throw new IllegalArgumentException("orderId must be positive");
        }
    }
    
    @Override
    public String value() {
        return PREFIX + orderId;
    }
}
```

### DistributedLockPort 메서드 시그니처
```java
public interface DistributedLockPort {
    boolean tryLock(LockKey key, long waitTime, long leaseTime, TimeUnit unit);
    void unlock(LockKey key);
    boolean isHeldByCurrentThread(LockKey key);
    boolean isLocked(LockKey key);
}
```

---

## 사용 패턴 (Application Layer)

### try-finally 필수 패턴
```java
public void processWithLock(Long orderId) {
    OrderLockKey lockKey = new OrderLockKey(orderId);
    
    boolean acquired = lockPort.tryLock(lockKey, 10, 30, TimeUnit.SECONDS);
    
    if (!acquired) {
        throw new LockAcquisitionException(lockKey.value(), "Lock 획득 실패");
    }
    
    try {
        // 비즈니스 로직
        processOrder(orderId);
    } finally {
        lockPort.unlock(lockKey);  // 반드시 finally에서 해제
    }
}
```

### waitTime / leaseTime 권장값
| 작업 유형 | waitTime | leaseTime | 설명 |
|----------|----------|-----------|------|
| 빠른 작업 | 5초 | 10초 | 재고 차감, 포인트 차감 |
| 일반 작업 | 10초 | 30초 | 주문 생성, 결제 처리 |
| 무거운 작업 | 30초 | 60초 | 배치 처리, 대량 데이터 |
