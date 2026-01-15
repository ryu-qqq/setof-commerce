---
paths:
  - "application/**"
  - "application-*/**"
---

# Application Layer 규칙 (자동 주입)

이 규칙은 `application/` 또는 `application-*/` 경로의 파일을 작업할 때 **자동으로 적용**됩니다.

## Zero-Tolerance 규칙 (절대 위반 금지)

### 어노테이션 규칙
- **C-001**: Lombok 사용 금지
- **SVC-006**: Service 클래스에서 `@Transactional` 절대 금지
- **C-004**: `@Transactional`은 Manager/Facade에서만 메서드 단위로 사용
- **EVT-005**: EventListener는 `@EventListener` 사용 (`@TransactionalEventListener` 금지)
- **SCHS-003**: Application Layer에 `@Scheduled` 어노테이션 금지

### 구조 규칙
- **UC-001**: UseCase는 반드시 interface로 정의
- **UC-002**: UseCase는 `execute()` 단일 메서드 제공
- **SVC-002**: Service는 UseCase(Port-In) 인터페이스 구현 필수
- **CDTO-001**: Command DTO는 Record로 정의
- **QDTO-001**: Query DTO는 Record로 정의
- **RDTO-001**: Response DTO는 Record로 정의
- **FAC-003**: Facade는 2개 이상 Manager 조합할 때만 사용

### 행위 규칙
- **SVC-003**: Domain 객체 직접 생성 금지 → Manager 위임
- **SVC-005**: Domain 객체 직접 반환 금지 → DTO 변환
- **SVC-007**: Service에 비즈니스 로직 금지 → Domain 위임
- **SVC-009**: Service에서 시간/ID 생성 금지 → Manager 위임
- **FAC-006**: Facade에 비즈니스 로직 금지
- **EVT-002**: EventListener 처리 전 Outbox 저장 필수
- **SCHS-004**: Scheduler Service는 분산락(LockManager) 사용 필수

### 의존성 규칙
- **SVC-008**: Service에서 Port(Out) 직접 주입 금지 → Manager 통해 사용
- **C-005**: Port(Out) 직접 주입 금지
- **COMP-004**: Component는 Manager 의존 가능, Port 직접 의존 금지
- **VAL-003**: Validator는 ReadManager만 의존
- **FAC-004**: Facade는 RDB Persistence Manager만 의존
- **CDTO-007**: Command DTO는 Domain 타입 의존 금지
- **QDTO-005**: Query DTO는 Domain 타입 의존 금지
- **RDTO-008**: Response DTO는 Domain 타입 의존 금지

## UseCase 패턴

```java
// Port-In (Interface)
public interface CancelOrderUseCase {
    void execute(CancelOrderCommand command);
}

// Command DTO (Record)
public record CancelOrderCommand(
    Long orderId,
    String reason
) {}

// Service (구현체)
@Component
public class CancelOrderService implements CancelOrderUseCase {
    private final OrderManager orderManager;  // Manager 주입 (Port 직접 금지)

    @Override
    public void execute(CancelOrderCommand command) {
        // Service에 @Transactional 금지 → Manager에서 처리
        orderManager.cancel(command.orderId(), command.reason());
    }
}
```

## Manager 패턴

```java
@Component
public class OrderManager {
    private final OrderQueryPort orderQueryPort;
    private final OrderCommandPort orderCommandPort;
    private final TimeProvider timeProvider;

    @Transactional  // Manager에서만 @Transactional 사용
    public void cancel(Long orderId, String reason) {
        Order order = orderQueryPort.getById(orderId);
        order.cancel(reason, timeProvider.now());
        orderCommandPort.persist(order);
    }
}
```

## 상세 규칙 참조

더 자세한 규칙은 다음 파일을 참조하세요:
- `.claude/knowledge/rules/application-rules.md` (103개 규칙)
- `.claude/knowledge/templates/application-templates.md`
- `.claude/knowledge/examples/application-examples.md`
