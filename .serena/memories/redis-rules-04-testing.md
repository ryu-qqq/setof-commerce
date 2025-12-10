# Persistence Redis Layer - Testing Rules

## REDIS_TEST_STRUCTURE (4 rules)

Redis 테스트 구조 규칙

```json
{
  "category": "REDIS_TEST_STRUCTURE",
  "rules": [
    {
      "ruleId": "REDIS-TEST-001",
      "name": "TestContainers Redis 사용 필수",
      "severity": "ERROR",
      "description": "Redis 테스트는 TestContainers redis:7-alpine 사용 필수",
      "pattern": "@Container.*GenericContainer.*redis:7-alpine",
      "antiPattern": "EmbeddedRedis|embedded-redis",
      "zeroTolerance": true,
      "rationale": "Embedded Redis는 네이티브 라이브러리 의존, TestContainers로 통일",
      "archUnitTest": "RedisTestArchTest.redisTest_MustUseTestContainers"
    },
    {
      "ruleId": "REDIS-TEST-002",
      "name": "RedisTestSupport 상속 필수",
      "severity": "ERROR",
      "description": "Redis 테스트는 RedisTestSupport 상속 필수 (공통 설정)",
      "pattern": "extends\\s+RedisTestSupport",
      "archUnitTest": "RedisTestArchTest.redisTest_MustExtendRedisTestSupport"
    },
    {
      "ruleId": "REDIS-TEST-003",
      "name": "@AfterEach 데이터 정리 필수",
      "severity": "ERROR",
      "description": "각 테스트 후 Redis 데이터 정리 필수 (테스트 격리)",
      "pattern": "@AfterEach.*flushAll|@AfterEach.*clear",
      "archUnitTest": "RedisTestArchTest.redisTest_MustCleanupAfterEach"
    },
    {
      "ruleId": "REDIS-TEST-004",
      "name": "Test Pyramid 준수",
      "severity": "INFO",
      "description": "ArchUnit(하단) → Unit Test → Integration Test(상단) 구조 준수",
      "rationale": "피라미드 하단: 빠른 테스트, 상단: 실제 환경 테스트"
    }
  ]
}
```

---

## CACHE_ADAPTER_TEST (5 rules)

Cache Adapter 테스트 규칙

```json
{
  "category": "CACHE_ADAPTER_TEST",
  "rules": [
    {
      "ruleId": "REDIS-CT-001",
      "name": "CacheTestSupport 상속 필수",
      "severity": "ERROR",
      "description": "Cache Adapter 테스트는 CacheTestSupport 상속 필수",
      "pattern": "extends\\s+CacheTestSupport",
      "archUnitTest": "CacheAdapterTestArchTest.cacheTest_MustExtendCacheTestSupport"
    },
    {
      "ruleId": "REDIS-CT-002",
      "name": "CacheKey VO 사용 필수",
      "severity": "ERROR",
      "description": "테스트에서 String 키 직접 사용 금지, CacheKey VO 사용",
      "pattern": "CacheKey",
      "antiPattern": "set\\(\"[^\"]+\"",
      "zeroTolerance": true,
      "archUnitTest": "CacheAdapterTestArchTest.cacheTest_MustUseCacheKeyVo"
    },
    {
      "ruleId": "REDIS-CT-003",
      "name": "set/get/evict 기본 동작 검증",
      "severity": "ERROR",
      "description": "Cache Adapter 테스트는 set/get/evict 기본 동작 검증 필수",
      "pattern": "(set|get|evict).*assert"
    },
    {
      "ruleId": "REDIS-CT-004",
      "name": "TTL 설정 및 만료 검증",
      "severity": "ERROR",
      "description": "Cache Adapter 테스트는 TTL 설정 및 만료 검증 필수",
      "pattern": "(TTL|ttl|Duration|expire).*assert"
    },
    {
      "ruleId": "REDIS-CT-005",
      "name": "직렬화/역직렬화 검증",
      "severity": "WARNING",
      "description": "Cache Adapter 테스트는 복잡한 객체 직렬화/역직렬화 검증 권장",
      "pattern": "(serialize|deserialize|ObjectMapper)"
    }
  ]
}
```

---

## LOCK_ADAPTER_TEST (5 rules)

Lock Adapter 테스트 규칙

```json
{
  "category": "LOCK_ADAPTER_TEST",
  "rules": [
    {
      "ruleId": "REDIS-LT-001",
      "name": "LockTestSupport 상속 필수",
      "severity": "ERROR",
      "description": "Lock Adapter 테스트는 LockTestSupport 상속 필수",
      "pattern": "extends\\s+LockTestSupport",
      "archUnitTest": "LockAdapterTestArchTest.lockTest_MustExtendLockTestSupport"
    },
    {
      "ruleId": "REDIS-LT-002",
      "name": "LockKey VO 사용 필수",
      "severity": "ERROR",
      "description": "테스트에서 String 키 직접 사용 금지, LockKey VO 사용",
      "pattern": "LockKey",
      "antiPattern": "tryLock\\(\"[^\"]+\"",
      "zeroTolerance": true,
      "archUnitTest": "LockAdapterTestArchTest.lockTest_MustUseLockKeyVo"
    },
    {
      "ruleId": "REDIS-LT-003",
      "name": "tryLock/unlock 기본 동작 검증",
      "severity": "ERROR",
      "description": "Lock Adapter 테스트는 tryLock/unlock 기본 동작 검증 필수",
      "pattern": "(tryLock|unlock).*assert"
    },
    {
      "ruleId": "REDIS-LT-004",
      "name": "동시성 테스트 필수",
      "severity": "ERROR",
      "description": "Lock Adapter 테스트는 동시성 테스트 필수 (runConcurrently)",
      "pattern": "(runConcurrently|ExecutorService|CompletableFuture\\.allOf)",
      "archUnitTest": "LockAdapterTestArchTest.lockTest_MustHaveConcurrencyTest"
    },
    {
      "ruleId": "REDIS-LT-005",
      "name": "finally 블록 unlock 검증",
      "severity": "WARNING",
      "description": "Lock 사용 코드에서 finally 블록 unlock 패턴 검증 권장",
      "pattern": "finally.*unlock"
    }
  ]
}
```

---

## 테스트 지원 클래스

### RedisTestSupport (기반 클래스)

```json
{
  "class": "RedisTestSupport",
  "location": "adapter-out/persistence-redis/src/test/java/.../common/RedisTestSupport.java",
  "role": "TestContainers Redis 설정, @AfterEach 데이터 정리",
  "methods": [
    {
      "name": "assertKeyExists",
      "description": "Redis 키 존재 확인"
    },
    {
      "name": "assertKeyNotExists",
      "description": "Redis 키 없음 확인"
    },
    {
      "name": "getTtl",
      "description": "키의 TTL 조회"
    }
  ]
}
```

### CacheTestSupport (Cache 전용)

```json
{
  "class": "CacheTestSupport",
  "location": "adapter-out/persistence-redis/src/test/java/.../common/CacheTestSupport.java",
  "role": "Cache Adapter 테스트 유틸리티",
  "methods": [
    {
      "name": "assertTtlSet",
      "description": "TTL이 설정되었는지 확인"
    },
    {
      "name": "assertCacheHit",
      "description": "캐시 히트 확인"
    },
    {
      "name": "assertCacheMiss",
      "description": "캐시 미스 확인"
    }
  ]
}
```

### LockTestSupport (Lock 전용)

```json
{
  "class": "LockTestSupport",
  "location": "adapter-out/persistence-redis/src/test/java/.../common/LockTestSupport.java",
  "role": "Lock Adapter 동시성 테스트 유틸리티",
  "methods": [
    {
      "name": "assertLocked",
      "description": "락이 획득되었는지 확인"
    },
    {
      "name": "assertUnlocked",
      "description": "락이 해제되었는지 확인"
    },
    {
      "name": "runConcurrently",
      "description": "여러 스레드에서 동시 실행"
    },
    {
      "name": "runConcurrentlyWithTracking",
      "description": "동시 실행 + 결과 추적"
    }
  ]
}
```

---

## 코드 예시

### Cache Adapter 테스트

```java
class ProductCacheAdapterTest extends CacheTestSupport {
    
    @Autowired
    private ProductCacheAdapter cacheAdapter;
    
    @Test
    void set_get_evict_기본_동작() {
        // given
        ProductCacheKey key = ProductCacheKey.of(1L);
        ProductCacheData data = new ProductCacheData("상품명", 10000);
        
        // when: set
        cacheAdapter.set(key, data, Duration.ofMinutes(10));
        
        // then: get
        Optional<ProductCacheData> cached = cacheAdapter.get(key);
        assertThat(cached).isPresent();
        assertThat(cached.get().name()).isEqualTo("상품명");
        
        // when: evict
        cacheAdapter.evict(key);
        
        // then: 없음
        assertThat(cacheAdapter.get(key)).isEmpty();
    }
    
    @Test
    void TTL_설정_및_만료() {
        // given
        ProductCacheKey key = ProductCacheKey.of(1L);
        ProductCacheData data = new ProductCacheData("상품명", 10000);
        
        // when
        cacheAdapter.set(key, data, Duration.ofSeconds(1));
        
        // then: TTL 설정됨
        assertTtlSet(key);
        
        // when: 만료 대기
        Thread.sleep(1500);
        
        // then: 만료됨
        assertThat(cacheAdapter.get(key)).isEmpty();
    }
}
```

### Lock Adapter 테스트

```java
class OrderLockAdapterTest extends LockTestSupport {
    
    @Autowired
    private DistributedLockAdapter lockAdapter;
    
    @Test
    void tryLock_unlock_기본_동작() {
        // given
        OrderLockKey key = OrderLockKey.of(1L);
        
        // when: 락 획득
        boolean acquired = lockAdapter.tryLock(key, Duration.ofSeconds(10));
        
        // then
        assertThat(acquired).isTrue();
        assertLocked(key);
        
        // when: 락 해제
        lockAdapter.unlock(key);
        
        // then
        assertUnlocked(key);
    }
    
    @Test
    void 동시_락_획득_시_하나만_성공() {
        // given
        OrderLockKey key = OrderLockKey.of(1L);
        AtomicInteger successCount = new AtomicInteger(0);
        
        // when: 10개 스레드 동시 실행
        runConcurrently(10, () -> {
            if (lockAdapter.tryLock(key, Duration.ofSeconds(1))) {
                try {
                    successCount.incrementAndGet();
                    Thread.sleep(100);
                } finally {
                    lockAdapter.unlock(key);
                }
            }
        });
        
        // then: 1개만 성공 (동시에 실행되므로)
        assertThat(successCount.get()).isEqualTo(1);
    }
    
    @Test
    void finally_블록_unlock_패턴() {
        // given
        OrderLockKey key = OrderLockKey.of(1L);
        
        // when: 예외 발생해도 unlock 보장
        try {
            lockAdapter.tryLock(key, Duration.ofSeconds(10));
            throw new RuntimeException("의도적 예외");
        } catch (Exception e) {
            // 예외 발생
        } finally {
            lockAdapter.unlock(key);
        }
        
        // then: 락 해제됨
        assertUnlocked(key);
    }
}
```

---

## 검증 체크리스트

### Cache Adapter 테스트
- [ ] CacheTestSupport 상속
- [ ] CacheKey VO 사용 (String 키 금지)
- [ ] set/get/evict 기본 동작 검증
- [ ] TTL 설정 및 만료 검증
- [ ] 직렬화/역직렬화 검증

### Lock Adapter 테스트
- [ ] LockTestSupport 상속
- [ ] LockKey VO 사용 (String 키 금지)
- [ ] tryLock/unlock 기본 동작 검증
- [ ] 동시성 테스트 (runConcurrently)
- [ ] finally 블록 unlock 검증

### 금지 사항
- [ ] Embedded Redis 사용 ❌
- [ ] String 키 직접 사용 ❌
- [ ] TTL 없는 캐시 저장 ❌
- [ ] unlock 없는 Lock 사용 ❌

---

## Zero-Tolerance 규칙 요약

| 카테고리 | 규칙 ID | 규칙 내용 |
|---------|---------|----------|
| REDIS_TEST | REDIS-TEST-001 | TestContainers 사용 필수 (Embedded Redis 금지) |
| CACHE_ADAPTER_TEST | REDIS-CT-002 | CacheKey VO 사용 필수 (String 키 금지) |
| LOCK_ADAPTER_TEST | REDIS-LT-002 | LockKey VO 사용 필수 (String 키 금지) |

---

## 관련 문서

- [Redis Testing Guide](docs/coding_convention/04-persistence-layer/redis/testing/01_redis-testing-guide.md)
- [Cache Adapter Test](docs/coding_convention/04-persistence-layer/redis/testing/02_cache-adapter-test.md)
- [Lock Adapter Test](docs/coding_convention/04-persistence-layer/redis/testing/03_lock-adapter-test.md)
- `redis-rules-01-cache-adapter`: Cache Adapter 상세 규칙
- `redis-rules-02-lock-adapter`: Lock Adapter 상세 규칙

---

**버전**: 2.0.0 (JSON 구조로 변환)
**작성일**: 2025-12-09
