---
paths:
  - "**/*.java"
---

# 공통 규칙 (모든 Java 파일 자동 주입)

이 규칙은 **모든 Java 파일**을 작업할 때 자동으로 적용됩니다.

## 프로젝트 아키텍처

```
┌─────────────────────────────────────────────────────────┐
│                    adapter-in/web                        │  ← REST API Layer
│                    (Controller, API DTO)                 │
└──────────────────────────┬──────────────────────────────┘
                           │ UseCase (Port-In)
┌──────────────────────────▼──────────────────────────────┐
│                      application                         │  ← Application Layer
│            (Service, Manager, DTO, Port-Out)            │
└──────────────────────────┬──────────────────────────────┘
                           │ Port-Out Interface
┌──────────────────────────▼──────────────────────────────┐
│                        domain                            │  ← Domain Layer
│              (Aggregate, VO, Event, Exception)          │
└──────────────────────────┬──────────────────────────────┘
                           │ Domain Interface
┌──────────────────────────▼──────────────────────────────┐
│                adapter-out/persistence                   │  ← Persistence Layer
│            (Entity, Repository, Adapter)                │
└─────────────────────────────────────────────────────────┘
```

## 전역 Zero-Tolerance 규칙

### Lombok 완전 금지 (모든 레이어)
```java
// ❌ 절대 금지
@Data
@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
```

### 의존성 방향 (절대 역전 금지)
```
Domain ← Application ← REST API
Domain ← Application ← Persistence

// ❌ 금지: Domain이 Application/Persistence 의존
// ❌ 금지: Application이 REST API/Persistence 직접 의존
```

### 예외 처리 규칙
```java
// ❌ 금지: 공통 예외 클래스 분리
public class EntityNotFoundException extends DomainException {}  // NEVER
public class DeletionConstraintException extends DomainException {}  // NEVER

// ✅ 허용: DomainException 직접 사용
throw new DomainException(OrderErrorCode.NOT_FOUND, orderId);
```

### 시간 처리 규칙
```java
// ❌ 금지: Instant.now() 직접 호출
Instant now = Instant.now();

// ✅ 허용: TimeProvider 사용
Instant now = timeProvider.now();
```

### Record 사용 규칙
```java
// Command, Query, Response DTO → Record 필수
public record CreateOrderCommand(Long customerId, List<OrderLine> lines) {}
public record OrderResponse(Long id, String status) {}

// ID Value Object → Record 필수
public record OrderId(Long value) {}
```

## 네이밍 컨벤션

| 구분 | 패턴 | 예시 |
|------|------|------|
| UseCase | `*UseCase` | `CreateOrderUseCase` |
| Service | `*Service` | `CreateOrderService` |
| Manager | `*Manager` | `OrderManager` |
| Port-Out | `*Port` | `OrderCommandPort`, `OrderQueryPort` |
| Adapter | `*Adapter` | `OrderCommandAdapter` |
| Entity | `*JpaEntity` | `OrderJpaEntity` |
| ID VO | `*Id` | `OrderId`, `CustomerId` |
| Event | `*Event` (과거형) | `OrderCreatedEvent` |
| Command DTO | `*Command` | `CreateOrderCommand` |
| Response DTO | `*Response` | `OrderResponse` |
| API Request | `*ApiRequest` | `CreateOrderApiRequest` |
| API Response | `*ApiResponse` | `OrderApiResponse` |

## 상세 규칙 참조

레이어별 상세 규칙:
- Domain: `.claude/knowledge/rules/domain-rules.md`
- Application: `.claude/knowledge/rules/application-rules.md`
- Persistence: `.claude/knowledge/rules/persistence-rules.md`
- REST API: `.claude/knowledge/rules/rest-api-rules.md`
- Zero-Tolerance 전체: `.claude/knowledge/rules/zero-tolerance.md`
