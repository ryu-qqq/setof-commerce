# Persistence Redis Layer - Config Rules

## CONFIG_STRUCTURE (3 rules)

Redis Config 클래스 구조 규칙

```json
{
  "category": "CONFIG_STRUCTURE",
  "rules": [
    {
      "ruleId": "REDIS-CFG-001",
      "name": "@Configuration 어노테이션 필수",
      "severity": "ERROR",
      "description": "Config 클래스는 @Configuration 어노테이션이 필수",
      "pattern": "@Configuration.*class\\s+\\w+Config",
      "antiPattern": "class\\s+\\w+Config(?!.*@Configuration)",
      "archUnitTest": "RedisConfigArchTest.config_MustHaveConfigurationAnnotation"
    },
    {
      "ruleId": "REDIS-CFG-002",
      "name": "LettuceConfig 클래스 필수",
      "severity": "ERROR",
      "description": "캐싱용 Lettuce 설정 클래스가 필수",
      "pattern": "@Configuration.*class\\s+LettuceConfig",
      "rationale": "캐싱을 위한 RedisTemplate 설정",
      "archUnitTest": "RedisConfigArchTest.lettuceConfig_MustExist"
    },
    {
      "ruleId": "REDIS-CFG-003",
      "name": "RedissonConfig 클래스 필수",
      "severity": "ERROR",
      "description": "분산락용 Redisson 설정 클래스가 필수",
      "pattern": "@Configuration.*class\\s+RedissonConfig",
      "rationale": "분산락을 위한 RedissonClient 설정",
      "archUnitTest": "RedisConfigArchTest.redissonConfig_MustExist"
    }
  ]
}
```

---

## CONFIG_BEAN (3 rules)

Redis Config Bean 정의 규칙

```json
{
  "category": "CONFIG_BEAN",
  "rules": [
    {
      "ruleId": "REDIS-CFG-004",
      "name": "@Bean 메서드 필수",
      "severity": "ERROR",
      "description": "Config 클래스는 최소 하나의 @Bean 메서드를 가져야 함",
      "pattern": "@Bean.*public\\s+\\w+",
      "rationale": "설정 클래스의 목적은 Bean 정의",
      "archUnitTest": "RedisConfigArchTest.config_MustHaveBeanMethods"
    },
    {
      "ruleId": "REDIS-CFG-005",
      "name": "LettuceConfig의 RedisTemplate Bean 필수",
      "severity": "ERROR",
      "description": "LettuceConfig는 RedisTemplate Bean을 정의해야 함",
      "pattern": "@Bean.*RedisTemplate|redisTemplate",
      "rationale": "캐싱용 RedisTemplate 제공",
      "archUnitTest": "RedisConfigArchTest.lettuceConfig_MustDefineRedisTemplateBean"
    },
    {
      "ruleId": "REDIS-CFG-006",
      "name": "RedissonConfig의 RedissonClient Bean 필수",
      "severity": "ERROR",
      "description": "RedissonConfig는 RedissonClient Bean을 정의해야 함",
      "pattern": "@Bean.*RedissonClient|redissonClient",
      "rationale": "분산락용 RedissonClient 제공",
      "archUnitTest": "RedisConfigArchTest.redissonConfig_MustDefineRedissonClientBean"
    }
  ]
}
```

---

## CONFIG_ROLE_SEPARATION (2 rules)

Lettuce / Redisson 역할 분리 규칙

```json
{
  "category": "CONFIG_ROLE_SEPARATION",
  "rules": [
    {
      "ruleId": "REDIS-CFG-007",
      "name": "LettuceConfig의 Redisson 의존 금지",
      "severity": "ERROR",
      "description": "LettuceConfig에서 Redisson 의존 금지",
      "antiPattern": "import.*redisson|Redisson.*\\w+",
      "rationale": "LettuceConfig는 캐시 전용, Redisson 의존 금지",
      "archUnitTest": "RedisConfigArchTest.lettuceConfig_MustNotDependOnRedisson"
    },
    {
      "ruleId": "REDIS-CFG-008",
      "name": "RedissonConfig의 RedisTemplate 의존 금지",
      "severity": "ERROR",
      "description": "RedissonConfig에서 RedisTemplate 의존 금지",
      "antiPattern": "import.*RedisTemplate|RedisTemplate.*\\w+",
      "rationale": "RedissonConfig는 분산락 전용, RedisTemplate 의존 금지",
      "archUnitTest": "RedisConfigArchTest.redissonConfig_MustNotDependOnRedisTemplate"
    }
  ]
}
```

---

## CONFIG_PROHIBITION (3 rules)

Config 금지 사항 규칙

```json
{
  "category": "CONFIG_PROHIBITION",
  "rules": [
    {
      "ruleId": "REDIS-CFG-009",
      "name": "Domain 의존 금지",
      "severity": "ERROR",
      "description": "Config 클래스에서 Domain 의존 금지",
      "antiPattern": "import.*\\.domain\\.",
      "rationale": "Config 클래스는 인프라 설정만 담당",
      "archUnitTest": "RedisConfigArchTest.config_MustNotContainBusinessLogic"
    },
    {
      "ruleId": "REDIS-CFG-010",
      "name": "Adapter 의존 금지",
      "severity": "ERROR",
      "description": "Config 클래스에서 Adapter 의존 금지",
      "antiPattern": "import.*\\.adapter\\.",
      "rationale": "Config 클래스는 Adapter에 의존하지 않음",
      "archUnitTest": "RedisConfigArchTest.config_MustNotDependOnAdapters"
    },
    {
      "ruleId": "REDIS-CFG-011",
      "name": "Application Layer 의존 금지",
      "severity": "ERROR",
      "description": "Config 클래스에서 Application Layer 의존 금지",
      "antiPattern": "import.*\\.application\\.",
      "rationale": "Config 클래스는 Application Layer에 의존하지 않음",
      "archUnitTest": "RedisConfigArchTest.config_MustNotDependOnApplicationLayer"
    }
  ]
}
```

---

## CONFIG_SAFETY (1 rule)

Config 안전성 규칙

```json
{
  "category": "CONFIG_SAFETY",
  "rules": [
    {
      "ruleId": "REDIS-CFG-012",
      "name": "@Value 외부화 필수",
      "severity": "WARNING",
      "description": "Redis 설정은 @Value로 외부화해야 함 (하드코딩 금지)",
      "pattern": "@Value\\(\"\\$\\{",
      "antiPattern": "host\\s*=\\s*\"localhost\"|port\\s*=\\s*6379",
      "rationale": "환경별 설정 분리, 보안 (비밀번호 등)",
      "archUnitTest": "RedisConfigArchTest.config_MustUseValueAnnotation"
    }
  ]
}
```

---

## 설정 예시

### LettuceConfig
```java
@Configuration
public class LettuceConfig {

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        LettuceConnectionFactory factory = new LettuceConnectionFactory(host, port);
        return factory;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }
}
```

### RedissonConfig
```java
@Configuration
public class RedissonConfig {

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer()
              .setAddress("redis://" + host + ":" + port)
              .setConnectionMinimumIdleSize(5)
              .setConnectionPoolSize(10);
        return Redisson.create(config);
    }
}
```

---

## application.yml 설정

```yaml
spring:
  data:
    redis:
      host: ${REDIS_HOST:localhost}
      port: ${REDIS_PORT:6379}
      password: ${REDIS_PASSWORD:}
      lettuce:
        pool:
          max-active: 16
          max-idle: 8
          min-idle: 4
      timeout: 3000ms
```

---

## 설정 체크리스트

### Lettuce (캐싱)
- [ ] Connection Pool 설정 (`max-active: 16`)
- [ ] `timeout: 3000ms` (Command Timeout)
- [ ] `GenericJackson2JsonRedisSerializer` 사용
- [ ] Key Naming Convention 준수
- [ ] TTL 전략 수립
- [ ] 환경 변수로 비밀번호 관리

### Redisson (분산락)
- [ ] `lockWatchdogTimeout: 30000ms` (Watchdog 주기)
- [ ] `nettyThreads` 설정
- [ ] Cluster/Sentinel 모드 설정 (Prod)
- [ ] Lock Timeout 전략 수립
