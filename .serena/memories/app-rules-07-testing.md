# Application Layer 테스트 규칙

> **핵심**: Mock 기반 단위 테스트, Spring Context 없이, BDD Mockito 스타일

---

## 테스트 태그 규칙 (필수)

### 기본 태그 (모든 Application 테스트)
```java
@Tag("unit")
@Tag("application")
```

### 컴포넌트별 추가 태그
| 컴포넌트 | 추가 태그 | 전체 조합 |
|----------|----------|-----------|
| CommandService | `@Tag("service")` | unit + application + service |
| QueryService | `@Tag("service")` | unit + application + service |
| Facade | `@Tag("facade")` | unit + application + facade |
| Manager | `@Tag("manager")` | unit + application + manager |
| Assembler | `@Tag("assembler")` | unit + application + assembler |
| Factory | `@Tag("factory")` | unit + application + factory |

---

## Zero-Tolerance 규칙

### 절대 금지
- ❌ `@SpringBootTest` - Spring Context 금지
- ❌ `@DataJpaTest` - 실제 DB 금지
- ❌ `Mockito.when()` - **BDD 스타일 `given()` 사용**
- ❌ `Mockito.verify()` - **BDD 스타일 `then()` 사용**
- ❌ 실제 Port 구현체 사용

### 허용/필수
- ✅ `@ExtendWith(MockitoExtension.class)`
- ✅ `@Mock` Port 인터페이스
- ✅ `@InjectMocks` Service
- ✅ BDD Mockito (`given()`, `willReturn()`, `then()`)

---

## 테스트 구조

### Service 테스트 템플릿
```java
@Tag("unit")
@Tag("application")
@Tag("service")
@ExtendWith(MockitoExtension.class)
@DisplayName("OrderCommandService 단위 테스트")
class OrderCommandServiceTest {

    @Mock
    private OrderCommandPort orderCommandPort;

    @InjectMocks
    private OrderCommandService orderCommandService;

    @Nested
    @DisplayName("createOrder")
    class CreateOrderTests {

        @Test
        @DisplayName("성공 - 주문 생성")
        void shouldCreateOrder() {
            // Given
            given(orderCommandPort.save(any())).willReturn(savedOrder);

            // When
            OrderId result = orderCommandService.createOrder(command);

            // Then
            assertThat(result).isNotNull();
            then(orderCommandPort).should().save(any());
        }
    }
}
```

### BDD Mockito 필수 사용
```java
// ✅ 올바른 방식
given(port.findById(any())).willReturn(Optional.of(entity));
then(port).should().save(any());

// ❌ 금지
when(port.findById(any())).thenReturn(Optional.of(entity));
verify(port).save(any());
```

---

## 실행 속도 기준

| 기준 | 임계값 |
|------|--------|
| 단일 테스트 | < 50ms (정상) |
| 단일 테스트 | > 200ms (리팩토링 필수) |
| 전체 Application | < 30초 |

---

## 태그 기반 실행

```bash
# Application 전체
./gradlew :application:test

# Service만
./gradlew :application:test -PincludeTags=service

# Facade만
./gradlew :application:test -PincludeTags=facade
```

---

## 참조
- 상세 가이드: `docs/coding_convention/03-application-layer/testing/01_application-testing-guide.md`
