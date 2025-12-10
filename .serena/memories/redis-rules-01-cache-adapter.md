# Persistence Redis Layer - Cache Adapter Rules

## CACHE_ADAPTER_STRUCTURE (4 rules)

Cache Adapter 클래스 구조 규칙

```json
{
  "category": "CACHE_ADAPTER_STRUCTURE",
  "rules": [
    {
      "ruleId": "REDIS-CA-001",
      "name": "CacheAdapter는 클래스여야 함",
      "severity": "ERROR",
      "description": "Cache Adapter는 클래스로 정의되어야 합니다 (인터페이스 금지)",
      "pattern": "class\\s+\\w+CacheAdapter",
      "antiPattern": "interface\\s+\\w+CacheAdapter",
      "archUnitTest": "CacheAdapterArchTest.cacheAdapter_MustBeClass"
    },
    {
      "ruleId": "REDIS-CA-002",
      "name": "@Component 어노테이션 필수",
      "severity": "ERROR",
      "description": "Cache Adapter는 @Component 어노테이션으로 Spring Bean 등록",
      "pattern": "@Component.*class\\s+\\w+CacheAdapter",
      "antiPattern": "class\\s+\\w+CacheAdapter(?!.*@Component)",
      "archUnitTest": "CacheAdapterArchTest.cacheAdapter_MustHaveComponentAnnotation"
    },
    {
      "ruleId": "REDIS-CA-003",
      "name": "CachePort 인터페이스 구현 필수",
      "severity": "ERROR",
      "description": "Cache Adapter는 CachePort 인터페이스를 구현해야 함",
      "pattern": "class\\s+\\w+CacheAdapter\\s+implements\\s+.*CachePort",
      "antiPattern": "class\\s+\\w+CacheAdapter(?!.*implements.*CachePort)",
      "archUnitTest": "CacheAdapterArchTest.cacheAdapter_MustImplementCachePort"
    },
    {
      "ruleId": "REDIS-CA-004",
      "name": "모든 필드 final 필수",
      "severity": "ERROR",
      "description": "Cache Adapter의 모든 인스턴스 필드는 final로 불변성 보장",
      "pattern": "private\\s+final\\s+\\w+",
      "antiPattern": "private\\s+(?!final)\\w+\\s+\\w+;",
      "archUnitTest": "CacheAdapterArchTest.cacheAdapter_AllFieldsMustBeFinal"
    }
  ]
}
```

---

## CACHE_ADAPTER_DEPENDENCY (3 rules)

Cache Adapter 의존성 규칙

```json
{
  "category": "CACHE_ADAPTER_DEPENDENCY",
  "rules": [
    {
      "ruleId": "REDIS-CA-005",
      "name": "RedisTemplate 의존성 필수",
      "severity": "ERROR",
      "description": "Cache Adapter는 RedisTemplate 의존성이 필수 (Lettuce)",
      "pattern": "RedisTemplate.*\\w+;",
      "rationale": "Lettuce 기반 캐싱을 위해 RedisTemplate 사용",
      "archUnitTest": "CacheAdapterArchTest.cacheAdapter_MustDependOnRedisTemplate"
    },
    {
      "ruleId": "REDIS-CA-006",
      "name": "ObjectMapper 의존성 필수",
      "severity": "ERROR",
      "description": "Cache Adapter는 ObjectMapper 의존성이 필수 (JSON 직렬화)",
      "pattern": "ObjectMapper.*\\w+;",
      "rationale": "캐시 데이터 JSON 직렬화/역직렬화",
      "archUnitTest": "CacheAdapterArchTest.cacheAdapter_MustDependOnObjectMapper"
    },
    {
      "ruleId": "REDIS-CA-007",
      "name": "RedissonClient 의존성 금지",
      "severity": "ERROR",
      "description": "Cache Adapter에서 Redisson 의존 금지 (Lock 전용)",
      "antiPattern": "RedissonClient.*\\w+;",
      "rationale": "역할 분리: Lettuce=캐시, Redisson=분산락",
      "archUnitTest": "CacheAdapterArchTest.cacheAdapter_MustNotDependOnRedisson"
    }
  ]
}
```

---

## CACHE_ADAPTER_METHOD (2 rules)

Cache Adapter 메서드 규칙

```json
{
  "category": "CACHE_ADAPTER_METHOD",
  "rules": [
    {
      "ruleId": "REDIS-CA-008",
      "name": "evictByPattern 메서드 필수",
      "severity": "ERROR",
      "description": "패턴 기반 캐시 무효화 메서드가 필수",
      "pattern": "(public|void)\\s+evictByPattern\\(",
      "rationale": "패턴 기반 캐시 무효화 지원",
      "archUnitTest": "CacheAdapterArchTest.cacheAdapter_MustHaveEvictByPatternMethod"
    },
    {
      "ruleId": "REDIS-CA-009",
      "name": "scanKeys 메서드 필수 (SCAN 사용)",
      "severity": "ERROR",
      "description": "KEYS 명령어 대신 SCAN을 사용하는 scanKeys 메서드 필수",
      "pattern": "private\\s+.*\\s+scanKeys\\(",
      "antiPattern": "redisTemplate\\.keys\\(",
      "rationale": "KEYS 명령어는 Redis 블로킹 발생, SCAN 사용",
      "archUnitTest": "CacheAdapterArchTest.cacheAdapter_MustHaveScanKeysMethod"
    }
  ]
}
```

---

## CACHE_ADAPTER_PROHIBITION (4 rules)

Cache Adapter 금지 사항 규칙

```json
{
  "category": "CACHE_ADAPTER_PROHIBITION",
  "rules": [
    {
      "ruleId": "REDIS-CA-010",
      "name": "@Transactional 사용 금지",
      "severity": "ERROR",
      "description": "Cache Adapter에서 @Transactional 사용 금지",
      "antiPattern": "@Transactional.*class\\s+\\w+CacheAdapter",
      "rationale": "Redis는 Spring Transaction과 무관",
      "archUnitTest": "CacheAdapterArchTest.cacheAdapter_MustNotBeTransactional"
    },
    {
      "ruleId": "REDIS-CA-011",
      "name": "비즈니스 로직 포함 금지",
      "severity": "ERROR",
      "description": "Cache Adapter에서 Domain 의존 및 비즈니스 로직 금지",
      "antiPattern": "import.*\\.domain\\.(?!common\\.vo)",
      "rationale": "Cache Adapter는 캐시 저장/조회/무효화만 담당",
      "archUnitTest": "CacheAdapterArchTest.cacheAdapter_MustNotContainBusinessLogic"
    },
    {
      "ruleId": "REDIS-CA-012",
      "name": "DB 접근 금지",
      "severity": "ERROR",
      "description": "Cache Adapter에서 DB 직접 접근 금지",
      "antiPattern": "(Repository|JpaRepository|EntityManager)",
      "rationale": "Cache와 DB 역할 분리",
      "archUnitTest": "CacheAdapterArchTest.cacheAdapter_MustNotAccessDatabase"
    },
    {
      "ruleId": "REDIS-CA-013",
      "name": "Logger 직접 사용 금지",
      "severity": "WARNING",
      "description": "Cache Adapter에서 Logger 직접 사용 금지 (AOP 처리)",
      "antiPattern": "Logger.*\\w+;|log\\.(info|debug|warn|error)",
      "rationale": "로깅은 AOP로 일괄 처리",
      "archUnitTest": "CacheAdapterArchTest.cacheAdapter_MustNotContainLogging"
    }
  ]
}
```

---

## CacheKey VO 사용 규칙

### Domain Layer 인터페이스
```java
// domain/src/main/java/.../common/vo/CacheKey.java
public interface CacheKey {
    String value();  // "cache:{domain}:{entity}:{id}" 형식
}
```

### 구현 예시
```java
public record ProductCacheKey(Long productId) implements CacheKey {
    private static final String PREFIX = "cache:product:";
    
    public ProductCacheKey {
        if (productId == null || productId <= 0) {
            throw new IllegalArgumentException("productId must be positive");
        }
    }
    
    @Override
    public String value() {
        return PREFIX + productId;
    }
}
```

### CachePort 메서드 시그니처
```java
public interface CachePort<T> {
    void set(CacheKey key, T value);              // CacheKey VO 사용
    void set(CacheKey key, T value, Duration ttl);
    Optional<T> get(CacheKey key);
    void evict(CacheKey key);
    void evictByPattern(String pattern);          // 패턴만 String 허용
    boolean exists(CacheKey key);
    Duration getTtl(CacheKey key);
}
```
