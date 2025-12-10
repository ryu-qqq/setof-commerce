# Persistence Redis Layer Validation Rules Index

> 필요한 카테고리만 선택적으로 읽어서 토큰을 절약하세요.

## 개요

- **Layer**: Persistence (Redis)
- **총 카테고리**: 9개
- **총 룰**: 52개
- **버전**: 1.0.0
- **전략**: Lettuce (캐싱) + Redisson (분산락) 이원화

---

## 카테고리 인덱스

### 🗄️ Cache Adapter (Lettuce 기반)
| 파일 | 카테고리 | 룰 수 | 용도 |
|-----|---------|------|------|
| `redis-rules-01-cache-adapter.md` | CACHE_ADAPTER_STRUCTURE | 4 | 클래스 구조 (@Component, CachePort 구현) |
| `redis-rules-01-cache-adapter.md` | CACHE_ADAPTER_DEPENDENCY | 3 | 의존성 (RedisTemplate 필수, Redisson 금지) |
| `redis-rules-01-cache-adapter.md` | CACHE_ADAPTER_METHOD | 2 | 메서드 (evictByPattern, scanKeys) |
| `redis-rules-01-cache-adapter.md` | CACHE_ADAPTER_PROHIBITION | 4 | 금지 (@Transactional, 비즈니스 로직, DB, Logger) |

### 🔐 Lock Adapter (Redisson 기반)
| 파일 | 카테고리 | 룰 수 | 용도 |
|-----|---------|------|------|
| `redis-rules-02-lock-adapter.md` | LOCK_ADAPTER_STRUCTURE | 4 | 클래스 구조 (@Component, DistributedLockPort 구현) |
| `redis-rules-02-lock-adapter.md` | LOCK_ADAPTER_DEPENDENCY | 2 | 의존성 (RedissonClient 필수, RedisTemplate 금지) |
| `redis-rules-02-lock-adapter.md` | LOCK_ADAPTER_METHOD | 4 | 메서드 (tryLock, unlock, isHeldByCurrentThread, isLocked) |
| `redis-rules-02-lock-adapter.md` | LOCK_ADAPTER_PROHIBITION | 5 | 금지 (@Transactional, 비즈니스 로직, DB, Logger, 스핀락) |
| `redis-rules-02-lock-adapter.md` | LOCK_ADAPTER_SAFETY | 1 | 안전성 (ConcurrentHashMap Lock 캐시) |

### ⚙️ Config (설정)
| 파일 | 카테고리 | 룰 수 | 용도 |
|-----|---------|------|------|
| `redis-rules-03-config.md` | CONFIG_STRUCTURE | 3 | 클래스 구조 (@Configuration, LettuceConfig, RedissonConfig) |
| `redis-rules-03-config.md` | CONFIG_BEAN | 3 | Bean 정의 (@Bean 필수, RedisTemplate, RedissonClient) |
| `redis-rules-03-config.md` | CONFIG_ROLE_SEPARATION | 2 | 역할 분리 (Lettuce-Redisson 상호 의존 금지) |
| `redis-rules-03-config.md` | CONFIG_PROHIBITION | 3 | 금지 (Domain, Adapter, Application 의존) |
| `redis-rules-03-config.md` | CONFIG_SAFETY | 1 | 설정 안전성 (@Value 외부화) |

---

## Zero-Tolerance 규칙 요약

### 캐싱 (Lettuce)
1. **@Transactional 금지** - Redis는 Spring TX와 무관
2. **Redisson 의존 금지** - Lettuce(RedisTemplate) 전용
3. **KEYS 명령어 금지** - SCAN 사용 (scanKeys 메서드)
4. **비즈니스 로직 금지** - 캐시 저장/조회/무효화만
5. **DB 접근 금지** - Cache와 DB 역할 분리

### 분산락 (Redisson)
1. **@Transactional 금지** - Redis는 Spring TX와 무관
2. **RedisTemplate 의존 금지** - Redisson 전용
3. **스핀락 패턴 금지** - Pub/Sub 기반 Lock 사용
4. **비즈니스 로직 금지** - Lock 획득/해제만
5. **Lock 캐시 필수** - ConcurrentHashMap 사용

### 설정 (Config)
1. **역할 분리** - LettuceConfig와 RedissonConfig 상호 의존 금지
2. **@Value 외부화** - 하드코딩 금지
3. **Domain/Application 의존 금지** - 인프라 설정만

---

## Lettuce vs Redisson 전략

```
┌─────────────────────────────────────────────────────┐
│                    Redis Server                      │
├─────────────────────────────────────────────────────┤
│                                                     │
│   [Lettuce]                    [Redisson]           │
│   Spring Boot 기본              별도 추가            │
│                                                     │
│   • @Cacheable                 • RLock (분산락)     │
│   • RedisTemplate              • RSemaphore         │
│   • Spring Session             • RCountDownLatch    │
│   • 단순 K-V 저장              • RAtomicLong        │
│                                                     │
└─────────────────────────────────────────────────────┘
```

| 라이브러리 | 용도 | 이유 |
|-----------|------|------|
| **Lettuce** | 캐싱, 세션, 단순 K-V | Spring Boot 기본, 가벼움, 빠름 |
| **Redisson** | 분산락, 분산 자료구조 | Pub/Sub 기반, Watchdog, 검증됨 |

---

## 사용법

```
# 인덱스 확인
read_memory("convention-persistence-redis-validation-rules.md")

# 필요한 룰만 선택적으로 읽기
read_memory("redis-rules-01-cache-adapter.md")  # Cache Adapter 룰 (13개)
read_memory("redis-rules-02-lock-adapter.md")   # Lock Adapter 룰 (16개)
read_memory("redis-rules-03-config.md")         # Config 룰 (12개)
read_memory("redis-rules-04-testing")         # Testing Guide
```

---

## ArchUnit 테스트 현황

| 테스트 파일 | 검증 대상 | 규칙 수 |
|------------|----------|--------|
| `CacheAdapterArchTest.java` | CacheAdapter | 11 |
| `DistributedLockAdapterArchTest.java` | LockAdapter | 14 |
| `RedisConfigArchTest.java` | Config | 10 |

**총 35개 ArchUnit 규칙이 빌드 시 자동 검증됩니다.**
