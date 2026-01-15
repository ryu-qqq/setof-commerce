# SCHEDULER Layer 예제 코드 (20개)

## 개요

이 문서는 SCHEDULER Layer의 코딩 규칙에 대한 GOOD/BAD 예제를 제공합니다.

## 예제 목록

| Rule Code | Rule Name | Type |
|-----------|-----------|------|
| TSCH-002 | Thin Scheduler는 UseCase 인터페이스만 의존 | ✅ GOOD |
| TSCH-002 | Thin Scheduler는 UseCase 인터페이스만 의존 | ✅ GOOD |
| TSCH-002 | Thin Scheduler는 UseCase 인터페이스만 의존 | ❌ BAD |
| TSCH-002 | Thin Scheduler는 UseCase 인터페이스만 의존 | ❌ BAD |
| TSCH-003 | Thin Scheduler의 @Scheduled 메서드는 UseCase.... | ✅ GOOD |
| TSCH-003 | Thin Scheduler의 @Scheduled 메서드는 UseCase.... | ✅ GOOD |
| TSCH-003 | Thin Scheduler의 @Scheduled 메서드는 UseCase.... | ❌ BAD |
| TSCH-003 | Thin Scheduler의 @Scheduled 메서드는 UseCase.... | ❌ BAD |
| TSCH-004 | Thin Scheduler에 비즈니스 로직 금지 | ✅ GOOD |
| TSCH-004 | Thin Scheduler에 비즈니스 로직 금지 | ✅ GOOD |
| TSCH-004 | Thin Scheduler에 비즈니스 로직 금지 | ❌ BAD |
| TSCH-004 | Thin Scheduler에 비즈니스 로직 금지 | ❌ BAD |
| TSCH-005 | @Scheduled는 fixedDelay 권장 (작업 중복 방지) | ✅ GOOD |
| TSCH-005 | @Scheduled는 fixedDelay 권장 (작업 중복 방지) | ✅ GOOD |
| TSCH-005 | @Scheduled는 fixedDelay 권장 (작업 중복 방지) | ❌ BAD |
| TSCH-005 | @Scheduled는 fixedDelay 권장 (작업 중복 방지) | ❌ BAD |
| TSCH-008 | Thin Scheduler는 예외 처리를 UseCase에 위임 | ✅ GOOD |
| TSCH-008 | Thin Scheduler는 예외 처리를 UseCase에 위임 | ✅ GOOD |
| TSCH-008 | Thin Scheduler는 예외 처리를 UseCase에 위임 | ❌ BAD |
| TSCH-008 | Thin Scheduler는 예외 처리를 UseCase에 위임 | ❌ BAD |

---

## 상세 예제

### TSCH-002: Thin Scheduler는 UseCase 인터페이스만 의존

#### ✅ GOOD Example

```java
@Component
public class OrderOutboxScheduler {

    // ✅ UseCase 인터페이스만 의존
    private final ProcessOrderOutboxUseCase processOrderOutboxUseCase;

    public OrderOutboxScheduler(ProcessOrderOutboxUseCase processOrderOutboxUseCase) {
        this.processOrderOutboxUseCase = processOrderOutboxUseCase;
    }

    @Scheduled(fixedDelay = 5000)
    public void processOutbox() {
        // ✅ UseCase.execute() 단일 호출
        processOrderOutboxUseCase.execute();
    }
}
```

**설명**: Thin Scheduler는 UseCase 인터페이스만 의존. UseCase 인터페이스만 주입받아 비즈니스 로직을 완전히 위임합니다.

---

#### ✅ GOOD Example

```java
@Component
public class OrderOutboxScheduler {

    // ✅ UseCase 인터페이스만 의존
    private final ProcessOrderOutboxUseCase processOrderOutboxUseCase;

    public OrderOutboxScheduler(ProcessOrderOutboxUseCase processOrderOutboxUseCase) {
        this.processOrderOutboxUseCase = processOrderOutboxUseCase;
    }

    @Scheduled(fixedDelay = 5000)
    public void processOutbox() {
        // ✅ UseCase.execute() 단일 호출
        processOrderOutboxUseCase.execute();
    }
}
```

**설명**: Thin Scheduler는 UseCase 인터페이스만 의존. UseCase 인터페이스만 주입받아 비즈니스 로직을 완전히 위임합니다.

---

#### ❌ BAD Example

```java
@Component
public class OrderOutboxScheduler {

    // ❌ Manager 직접 주입 금지
    private final OutboxReadManager outboxReadManager;
    private final OutboxPersistenceManager outboxPersistenceManager;
    private final DistributedLockManager lockManager;

    @Scheduled(fixedDelay = 5000)
    public void processOutbox() {
        // ❌ 비즈니스 로직이 Scheduler에 존재
        if (!lockManager.tryLock("order-outbox", Duration.ofMinutes(5))) {
            return;
        }
        try {
            List<Outbox> items = outboxReadManager.findPending(100);
            for (Outbox item : items) {
                // 처리 로직...
                outboxPersistenceManager.markAsProcessed(item.id());
            }
        } finally {
            lockManager.unlock("order-outbox");
        }
    }
}
```

**설명**: Thin Scheduler에 Manager/Port 직접 주입 금지. Manager, Port, Repository를 직접 주입하면 Thin Layer 원칙을 위반합니다.

---

#### ❌ BAD Example

```java
@Component
public class OrderOutboxScheduler {

    // ❌ Manager 직접 주입 금지
    private final OutboxReadManager outboxReadManager;
    private final OutboxPersistenceManager outboxPersistenceManager;
    private final DistributedLockManager lockManager;

    @Scheduled(fixedDelay = 5000)
    public void processOutbox() {
        // ❌ 비즈니스 로직이 Scheduler에 존재
        if (!lockManager.tryLock("order-outbox", Duration.ofMinutes(5))) {
            return;
        }
        try {
            List<Outbox> items = outboxReadManager.findPending(100);
            for (Outbox item : items) {
                // 처리 로직...
                outboxPersistenceManager.markAsProcessed(item.id());
            }
        } finally {
            lockManager.unlock("order-outbox");
        }
    }
}
```

**설명**: Thin Scheduler에 Manager/Port 직접 주입 금지. Manager, Port, Repository를 직접 주입하면 Thin Layer 원칙을 위반합니다.

---

### TSCH-003: Thin Scheduler의 @Scheduled 메서드는 UseCase.execute() 단일 호출

#### ✅ GOOD Example

```java
@Component
public class PaymentRetryScheduler {

    private final RetryFailedPaymentUseCase retryFailedPaymentUseCase;

    public PaymentRetryScheduler(RetryFailedPaymentUseCase retryFailedPaymentUseCase) {
        this.retryFailedPaymentUseCase = retryFailedPaymentUseCase;
    }

    @Scheduled(fixedDelay = 60000)
    public void retryFailedPayments() {
        // ✅ UseCase.execute() 단일 호출만!
        retryFailedPaymentUseCase.execute();
    }
}
```

**설명**: @Scheduled 메서드는 UseCase.execute() 한 줄만. @Scheduled 메서드 body에는 UseCase.execute() 호출만 포함합니다.

---

#### ✅ GOOD Example

```java
@Component
public class PaymentRetryScheduler {

    private final RetryFailedPaymentUseCase retryFailedPaymentUseCase;

    public PaymentRetryScheduler(RetryFailedPaymentUseCase retryFailedPaymentUseCase) {
        this.retryFailedPaymentUseCase = retryFailedPaymentUseCase;
    }

    @Scheduled(fixedDelay = 60000)
    public void retryFailedPayments() {
        // ✅ UseCase.execute() 단일 호출만!
        retryFailedPaymentUseCase.execute();
    }
}
```

**설명**: @Scheduled 메서드는 UseCase.execute() 한 줄만. @Scheduled 메서드 body에는 UseCase.execute() 호출만 포함합니다.

---

#### ❌ BAD Example

```java
@Component
public class PaymentRetryScheduler {

    private final RetryFailedPaymentUseCase retryFailedPaymentUseCase;

    @Scheduled(fixedDelay = 60000)
    public void retryFailedPayments() {
        // ❌ 로깅 추가 금지
        log.info("Starting payment retry...");

        // ❌ 시간 측정 금지
        long startTime = System.currentTimeMillis();

        retryFailedPaymentUseCase.execute();

        // ❌ 후처리 로직 금지
        long elapsed = System.currentTimeMillis() - startTime;
        log.info("Completed in {} ms", elapsed);
    }
}
```

**설명**: @Scheduled 메서드에 여러 줄 로직 금지. 로깅, 조건 분기, 전후 처리 등을 추가하면 안 됩니다.

---

#### ❌ BAD Example

```java
@Component
public class PaymentRetryScheduler {

    private final RetryFailedPaymentUseCase retryFailedPaymentUseCase;

    @Scheduled(fixedDelay = 60000)
    public void retryFailedPayments() {
        // ❌ 로깅 추가 금지
        log.info("Starting payment retry...");

        // ❌ 시간 측정 금지
        long startTime = System.currentTimeMillis();

        retryFailedPaymentUseCase.execute();

        // ❌ 후처리 로직 금지
        long elapsed = System.currentTimeMillis() - startTime;
        log.info("Completed in {} ms", elapsed);
    }
}
```

**설명**: @Scheduled 메서드에 여러 줄 로직 금지. 로깅, 조건 분기, 전후 처리 등을 추가하면 안 됩니다.

---

### TSCH-004: Thin Scheduler에 비즈니스 로직 금지

#### ✅ GOOD Example

```java
@Component
public class NotificationScheduler {

    private final ProcessNotificationOutboxUseCase processNotificationOutboxUseCase;

    public NotificationScheduler(ProcessNotificationOutboxUseCase processNotificationOutboxUseCase) {
        this.processNotificationOutboxUseCase = processNotificationOutboxUseCase;
    }

    // ✅ Thin Scheduler: 호출만 담당
    // 분산락, 배치 처리, 예외 처리 등은 모두 UseCase 내부에서 처리
    @Scheduled(fixedDelay = 5000)
    public void processNotifications() {
        processNotificationOutboxUseCase.execute();
    }
}
```

**설명**: Thin Scheduler는 비즈니스 로직 없이 호출만. 분산락, 배치 조회, 상태 업데이트 등 모든 로직은 Application Layer에 위임합니다.

---

#### ✅ GOOD Example

```java
@Component
public class NotificationScheduler {

    private final ProcessNotificationOutboxUseCase processNotificationOutboxUseCase;

    public NotificationScheduler(ProcessNotificationOutboxUseCase processNotificationOutboxUseCase) {
        this.processNotificationOutboxUseCase = processNotificationOutboxUseCase;
    }

    // ✅ Thin Scheduler: 호출만 담당
    // 분산락, 배치 처리, 예외 처리 등은 모두 UseCase 내부에서 처리
    @Scheduled(fixedDelay = 5000)
    public void processNotifications() {
        processNotificationOutboxUseCase.execute();
    }
}
```

**설명**: Thin Scheduler는 비즈니스 로직 없이 호출만. 분산락, 배치 조회, 상태 업데이트 등 모든 로직은 Application Layer에 위임합니다.

---

#### ❌ BAD Example

```java
@Component
public class NotificationScheduler {

    // ❌ 여러 컴포넌트 주입
    private final DistributedLockManager lockManager;
    private final NotificationOutboxReadManager outboxReadManager;
    private final NotificationClientManager clientManager;
    private final NotificationOutboxPersistenceManager outboxPersistenceManager;

    @Scheduled(fixedDelay = 5000)
    public void processNotifications() {
        // ❌ 분산락 로직
        if (!lockManager.tryLock("notification-outbox", Duration.ofMinutes(5))) {
            return;
        }

        try {
            // ❌ 배치 조회 로직
            List<NotificationOutbox> outboxes = outboxReadManager.findPending(100);

            for (NotificationOutbox outbox : outboxes) {
                try {
                    // ❌ 외부 호출 로직
                    clientManager.send(outbox.toNotification());
                    // ❌ 상태 업데이트 로직
                    outboxPersistenceManager.markAsSuccess(outbox.id());
                } catch (Exception e) {
                    outboxPersistenceManager.incrementRetryCount(outbox.id());
                }
            }
        } finally {
            lockManager.unlock("notification-outbox");
        }
    }
}
```

**설명**: Thin Scheduler에 분산락/배치 로직 포함 금지. Thin Scheduler에 비즈니스 로직이 들어가면 테스트와 유지보수가 어려워집니다.

---

#### ❌ BAD Example

```java
@Component
public class NotificationScheduler {

    // ❌ 여러 컴포넌트 주입
    private final DistributedLockManager lockManager;
    private final NotificationOutboxReadManager outboxReadManager;
    private final NotificationClientManager clientManager;
    private final NotificationOutboxPersistenceManager outboxPersistenceManager;

    @Scheduled(fixedDelay = 5000)
    public void processNotifications() {
        // ❌ 분산락 로직
        if (!lockManager.tryLock("notification-outbox", Duration.ofMinutes(5))) {
            return;
        }

        try {
            // ❌ 배치 조회 로직
            List<NotificationOutbox> outboxes = outboxReadManager.findPending(100);

            for (NotificationOutbox outbox : outboxes) {
                try {
                    // ❌ 외부 호출 로직
                    clientManager.send(outbox.toNotification());
                    // ❌ 상태 업데이트 로직
                    outboxPersistenceManager.markAsSuccess(outbox.id());
                } catch (Exception e) {
                    outboxPersistenceManager.incrementRetryCount(outbox.id());
                }
            }
        } finally {
            lockManager.unlock("notification-outbox");
        }
    }
}
```

**설명**: Thin Scheduler에 분산락/배치 로직 포함 금지. Thin Scheduler에 비즈니스 로직이 들어가면 테스트와 유지보수가 어려워집니다.

---

### TSCH-005: @Scheduled는 fixedDelay 권장 (작업 중복 방지)

#### ✅ GOOD Example

```java
@Component
public class OrderOutboxScheduler {

    private final ProcessOrderOutboxUseCase processOrderOutboxUseCase;

    public OrderOutboxScheduler(ProcessOrderOutboxUseCase processOrderOutboxUseCase) {
        this.processOrderOutboxUseCase = processOrderOutboxUseCase;
    }

    // ✅ fixedDelay: 이전 작업 완료 후 5초 대기
    // 작업 시간이 길어져도 중첩 실행되지 않음
    @Scheduled(fixedDelay = 5000)
    public void processOutbox() {
        processOrderOutboxUseCase.execute();
    }
}
```

**설명**: fixedDelay로 작업 중첩 방지. fixedDelay는 이전 작업 완료 후 지연을 적용하여 작업이 중첩되지 않습니다.

---

#### ✅ GOOD Example

```java
@Component
public class OrderOutboxScheduler {

    private final ProcessOrderOutboxUseCase processOrderOutboxUseCase;

    public OrderOutboxScheduler(ProcessOrderOutboxUseCase processOrderOutboxUseCase) {
        this.processOrderOutboxUseCase = processOrderOutboxUseCase;
    }

    // ✅ fixedDelay: 이전 작업 완료 후 5초 대기
    // 작업 시간이 길어져도 중첩 실행되지 않음
    @Scheduled(fixedDelay = 5000)
    public void processOutbox() {
        processOrderOutboxUseCase.execute();
    }
}
```

**설명**: fixedDelay로 작업 중첩 방지. fixedDelay는 이전 작업 완료 후 지연을 적용하여 작업이 중첩되지 않습니다.

---

#### ❌ BAD Example

```java
@Component
public class OrderOutboxScheduler {

    private final ProcessOrderOutboxUseCase processOrderOutboxUseCase;

    public OrderOutboxScheduler(ProcessOrderOutboxUseCase processOrderOutboxUseCase) {
        this.processOrderOutboxUseCase = processOrderOutboxUseCase;
    }

    // ⚠️ fixedRate: 작업 완료와 무관하게 5초마다 실행
    // 작업이 5초 이상 걸리면 중첩 실행됨!
    // 예: 작업A(7초) 실행 중에 작업B 시작 → 동시 실행 문제
    @Scheduled(fixedRate = 5000)
    public void processOutbox() {
        processOrderOutboxUseCase.execute();
    }
}
```

**설명**: fixedRate는 작업 중첩 위험. fixedRate는 작업 완료와 무관하게 일정 간격으로 실행되어 중첩될 수 있습니다.

---

#### ❌ BAD Example

```java
@Component
public class OrderOutboxScheduler {

    private final ProcessOrderOutboxUseCase processOrderOutboxUseCase;

    public OrderOutboxScheduler(ProcessOrderOutboxUseCase processOrderOutboxUseCase) {
        this.processOrderOutboxUseCase = processOrderOutboxUseCase;
    }

    // ⚠️ fixedRate: 작업 완료와 무관하게 5초마다 실행
    // 작업이 5초 이상 걸리면 중첩 실행됨!
    // 예: 작업A(7초) 실행 중에 작업B 시작 → 동시 실행 문제
    @Scheduled(fixedRate = 5000)
    public void processOutbox() {
        processOrderOutboxUseCase.execute();
    }
}
```

**설명**: fixedRate는 작업 중첩 위험. fixedRate는 작업 완료와 무관하게 일정 간격으로 실행되어 중첩될 수 있습니다.

---

### TSCH-008: Thin Scheduler는 예외 처리를 UseCase에 위임

#### ✅ GOOD Example

```java
@Component
public class InventoryScheduler {

    private final SyncInventoryUseCase syncInventoryUseCase;

    public InventoryScheduler(SyncInventoryUseCase syncInventoryUseCase) {
        this.syncInventoryUseCase = syncInventoryUseCase;
    }

    // ✅ 예외 처리 없음 - UseCase에서 처리
    // UseCase 내부에서 try-catch, 로깅, 재시도 등 처리
    @Scheduled(fixedDelay = 30000)
    public void syncInventory() {
        syncInventoryUseCase.execute();
    }
}
```

**설명**: Thin Scheduler는 예외 처리를 UseCase에 위임. 모든 예외 처리는 UseCase 구현체 내부에서 처리합니다.

---

#### ✅ GOOD Example

```java
@Component
public class InventoryScheduler {

    private final SyncInventoryUseCase syncInventoryUseCase;

    public InventoryScheduler(SyncInventoryUseCase syncInventoryUseCase) {
        this.syncInventoryUseCase = syncInventoryUseCase;
    }

    // ✅ 예외 처리 없음 - UseCase에서 처리
    // UseCase 내부에서 try-catch, 로깅, 재시도 등 처리
    @Scheduled(fixedDelay = 30000)
    public void syncInventory() {
        syncInventoryUseCase.execute();
    }
}
```

**설명**: Thin Scheduler는 예외 처리를 UseCase에 위임. 모든 예외 처리는 UseCase 구현체 내부에서 처리합니다.

---

#### ❌ BAD Example

```java
@Component
public class InventoryScheduler {

    private final SyncInventoryUseCase syncInventoryUseCase;

    public InventoryScheduler(SyncInventoryUseCase syncInventoryUseCase) {
        this.syncInventoryUseCase = syncInventoryUseCase;
    }

    @Scheduled(fixedDelay = 30000)
    public void syncInventory() {
        // ❌ Scheduler에서 예외 처리 금지
        try {
            syncInventoryUseCase.execute();
        } catch (ExternalApiException e) {
            // ❌ 재시도 로직 금지 - UseCase에서 처리해야 함
            log.error("External API failed, will retry next cycle", e);
        } catch (Exception e) {
            // ❌ 일반 예외 처리도 금지
            log.error("Unexpected error in inventory sync", e);
        }
    }
}
```

**설명**: Thin Scheduler에서 try-catch 금지. Scheduler에서 예외 처리하면 비즈니스 로직이 분산됩니다.

---

#### ❌ BAD Example

```java
@Component
public class InventoryScheduler {

    private final SyncInventoryUseCase syncInventoryUseCase;

    public InventoryScheduler(SyncInventoryUseCase syncInventoryUseCase) {
        this.syncInventoryUseCase = syncInventoryUseCase;
    }

    @Scheduled(fixedDelay = 30000)
    public void syncInventory() {
        // ❌ Scheduler에서 예외 처리 금지
        try {
            syncInventoryUseCase.execute();
        } catch (ExternalApiException e) {
            // ❌ 재시도 로직 금지 - UseCase에서 처리해야 함
            log.error("External API failed, will retry next cycle", e);
        } catch (Exception e) {
            // ❌ 일반 예외 처리도 금지
            log.error("Unexpected error in inventory sync", e);
        }
    }
}
```

**설명**: Thin Scheduler에서 try-catch 금지. Scheduler에서 예외 처리하면 비즈니스 로직이 분산됩니다.

---

