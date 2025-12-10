# Domain Layer 테스트 규칙

> **핵심**: Pure Java 단위 테스트, 외부 의존성 제로, 밀리초 단위 실행

---

## 테스트 태그 규칙 (필수)

### 기본 태그 (모든 Domain 테스트)
```java
@Tag("unit")
@Tag("domain")
```

### 컴포넌트별 추가 태그
| 컴포넌트 | 추가 태그 | 전체 조합 |
|----------|----------|-----------|
| Aggregate | `@Tag("aggregate")` | unit + domain + aggregate |
| VO | `@Tag("vo")` | unit + domain + vo |
| Exception | `@Tag("exception")` | unit + domain + exception |
| Event | `@Tag("event")` | unit + domain + event |
| Policy | `@Tag("policy")` | unit + domain + policy |

---

## Zero-Tolerance 규칙

### 절대 금지
- ❌ `@SpringBootTest` - Spring Context 로딩 금지
- ❌ `@DataJpaTest` - DB 의존성 금지
- ❌ `@Autowired` - DI 금지
- ❌ `@Mock` 과다 사용 - Pure Java 원칙 위반
- ❌ Testcontainers - 인프라 의존성 금지

### 허용되는 Mock
- ✅ `Clock` - 시간 고정
- ✅ `Random` - 결정적 테스트

---

## 테스트 구조

### 클래스 구조
```java
@Tag("unit")
@Tag("domain")
@Tag("aggregate")  // 컴포넌트별 추가
@DisplayName("Order Aggregate 단위 테스트")
class OrderTest {

    private static final Clock FIXED_CLOCK = Clock.fixed(
        Instant.parse("2024-01-01T00:00:00Z"),
        ZoneId.of("UTC")
    );

    @Nested
    @DisplayName("정적 팩토리 메서드")
    class FactoryMethodTests { }

    @Nested
    @DisplayName("비즈니스 메서드")
    class BusinessMethodTests { }
}
```

### 필수 사항
- `@DisplayName` 한글 작성
- `@Nested`로 관심사 분리
- AssertJ 사용 (JUnit Assertions 금지)
- Object Mother 패턴 권장

---

## 실행 속도 기준

| 기준 | 임계값 |
|------|--------|
| 단일 테스트 | < 10ms (정상) |
| 단일 테스트 | > 100ms (리팩토링 필수) |
| 전체 Domain | < 5초 |

---

## 태그 기반 실행

```bash
# Domain 전체
./gradlew :domain:test

# Aggregate만
./gradlew :domain:test -PincludeTags=aggregate

# VO만
./gradlew :domain:test -PincludeTags=vo
```

---

## 참조
- 상세 가이드: `docs/coding_convention/02-domain-layer/testing/01_domain-testing-guide.md`
