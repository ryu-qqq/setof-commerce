# Application Layer - Event & Scheduler Rules

## EVENT_REGISTRY (6 rules)

Transaction Event Registry 규칙 (트랜잭션 커밋 후 Event 발행)

```json
{
  "category": "EVENT_REGISTRY",
  "rules": [
    {
      "ruleId": "APP-ER-001",
      "name": "@Component 어노테이션 필수",
      "severity": "ERROR",
      "description": "EventRegistry는 @Component 어노테이션 필수",
      "pattern": "@Component.*class\\s+\\w+EventRegistry",
      "antiPattern": "@(Service|Repository).*class\\s+\\w+EventRegistry",
      "archUnitTest": "EventRegistryArchTest.eventRegistry_MustHaveComponentAnnotation"
    },
    {
      "ruleId": "APP-ER-002",
      "name": "TransactionSynchronizationManager 사용 필수",
      "severity": "ERROR",
      "description": "ThreadLocal 금지, TransactionSynchronizationManager.registerSynchronization 사용 필수",
      "pattern": "TransactionSynchronizationManager\\.registerSynchronization",
      "antiPattern": "ThreadLocal<",
      "zeroTolerance": true,
      "rationale": "ThreadLocal은 Virtual Thread에서 문제 발생",
      "archUnitTest": "EventRegistryArchTest.eventRegistry_MustUseTransactionSynchronization"
    },
    {
      "ruleId": "APP-ER-003",
      "name": "ApplicationEventPublisher 주입 필수",
      "severity": "ERROR",
      "description": "EventRegistry는 ApplicationEventPublisher 주입 필수",
      "pattern": "ApplicationEventPublisher\\s+\\w+",
      "archUnitTest": "EventRegistryArchTest.eventRegistry_MustInjectEventPublisher"
    },
    {
      "ruleId": "APP-ER-004",
      "name": "registerForPublish 메서드 제공",
      "severity": "WARNING",
      "description": "EventRegistry는 registerForPublish(DomainEvent) 메서드 제공 권장",
      "pattern": "public\\s+void\\s+registerForPublish\\(DomainEvent",
      "antiPattern": "(publish|emit)\\("
    },
    {
      "ruleId": "APP-ER-005",
      "name": "afterCommit에서 발행",
      "severity": "ERROR",
      "description": "Event 발행은 afterCommit()에서만, beforeCommit/afterCompletion 금지",
      "pattern": "afterCommit\\(\\)\\s*\\{[^}]*eventPublisher\\.publishEvent",
      "antiPattern": "(beforeCommit|afterCompletion)\\(",
      "zeroTolerance": true,
      "rationale": "커밋 성공 시에만 Event 발행",
      "archUnitTest": "EventRegistryArchTest.eventRegistry_MustPublishInAfterCommit"
    },
    {
      "ruleId": "APP-ER-006",
      "name": "Lombok 금지",
      "severity": "ERROR",
      "description": "EventRegistry에서 Lombok 어노테이션 사용 금지",
      "antiPattern": "@(RequiredArgsConstructor|AllArgsConstructor|Data)",
      "zeroTolerance": true,
      "archUnitTest": "EventRegistryArchTest.eventRegistry_MustNotUseLombok"
    }
  ]
}
```

---

## EVENT_LISTENER (7 rules)

Event Listener 규칙 (트랜잭션 커밋 후 동기 처리)

```json
{
  "category": "EVENT_LISTENER",
  "rules": [
    {
      "ruleId": "APP-EL-001",
      "name": "@Component 어노테이션 필수",
      "severity": "ERROR",
      "description": "EventListener는 @Component 어노테이션 필수",
      "pattern": "@Component.*class\\s+\\w+EventListener",
      "antiPattern": "@(Service|Repository).*class\\s+\\w+EventListener",
      "archUnitTest": "EventListenerArchTest.eventListener_MustHaveComponentAnnotation"
    },
    {
      "ruleId": "APP-EL-002",
      "name": "@EventListener 사용",
      "severity": "WARNING",
      "description": "@TransactionalEventListener 대신 @EventListener 사용 (Registry가 커밋 후 발행)",
      "pattern": "@EventListener",
      "antiPattern": "@TransactionalEventListener",
      "rationale": "TransactionEventRegistry가 커밋 후 발행하므로 @EventListener로 충분"
    },
    {
      "ruleId": "APP-EL-003",
      "name": "try-catch 예외 처리 필수",
      "severity": "ERROR",
      "description": "EventListener 메서드에서 try-catch 예외 처리 필수",
      "pattern": "try\\s*\\{[^}]+\\}\\s*catch\\s*\\(Exception",
      "zeroTolerance": true,
      "rationale": "리스너 실패해도 원래 작업 롤백 안 됨, 예외 전파 방지",
      "archUnitTest": "EventListenerArchTest.eventListener_MustHaveTryCatch"
    },
    {
      "ruleId": "APP-EL-004",
      "name": "부가 작업만 처리",
      "severity": "ERROR",
      "description": "EventListener는 캐시 무효화, 메트릭 기록 등 부가 작업만 (중요 작업은 Outbox)",
      "pattern": "(cacheService|metricsService|log\\.)",
      "antiPattern": "(paymentService|externalApi|CommandPort)",
      "zeroTolerance": true,
      "rationale": "중요 작업은 Outbox 패턴 사용",
      "archUnitTest": "EventListenerArchTest.eventListener_MustHandleOnlySideEffects"
    },
    {
      "ruleId": "APP-EL-005",
      "name": "실패 시 로깅 필수",
      "severity": "WARNING",
      "description": "EventListener catch 블록에서 로깅 필수",
      "pattern": "catch\\s*\\([^)]+\\)\\s*\\{[^}]*log\\.(warn|error)",
      "antiPattern": "catch\\s*\\([^)]+\\)\\s*\\{\\s*\\}"
    },
    {
      "ruleId": "APP-EL-006",
      "name": "패키지 위치 준수",
      "severity": "ERROR",
      "description": "EventListener는 application.[bc].listener 패키지에 위치",
      "pattern": "application\\.\\w+\\.listener\\.\\w+EventListener",
      "antiPattern": "(domain\\.|adapter\\.|application\\.common).*EventListener",
      "archUnitTest": "EventListenerArchTest.eventListener_MustBeInCorrectPackage"
    },
    {
      "ruleId": "APP-EL-007",
      "name": "Lombok 금지",
      "severity": "ERROR",
      "description": "EventListener에서 Lombok 어노테이션 사용 금지",
      "antiPattern": "@(RequiredArgsConstructor|AllArgsConstructor)",
      "zeroTolerance": true,
      "archUnitTest": "EventListenerArchTest.eventListener_MustNotUseLombok"
    }
  ]
}
```

---

## SCHEDULER (11 rules)

Scheduler 규칙 (주기적 배치 작업 오케스트레이션)

```json
{
  "category": "SCHEDULER",
  "rules": [
    {
      "ruleId": "APP-SCH-001",
      "name": "@Component + @ConditionalOnProperty 필수",
      "severity": "ERROR",
      "description": "Scheduler는 @Component와 @ConditionalOnProperty(환경별 활성화) 필수",
      "pattern": "@Component.*@ConditionalOnProperty\\(name\\s*=\\s*\"scheduler\\.",
      "antiPattern": "@Component(?!.*@ConditionalOnProperty)",
      "rationale": "환경별 활성화/비활성화 제어 필수",
      "archUnitTest": "SchedulerArchTest.scheduler_MustHaveConditionalOnProperty"
    },
    {
      "ruleId": "APP-SCH-002",
      "name": "UseCase/Service만 호출",
      "severity": "ERROR",
      "description": "Scheduler는 UseCase/Service만 호출, Port/Manager 직접 호출 금지",
      "pattern": "(useCase\\.execute\\(\\)|service\\.)",
      "antiPattern": "(QueryPort\\.|PersistencePort\\.|readManager\\.|transactionManager\\.)",
      "zeroTolerance": true,
      "rationale": "Scheduler는 오케스트레이션만 담당",
      "archUnitTest": "SchedulerArchTest.scheduler_MustOnlyCallUseCase"
    },
    {
      "ruleId": "APP-SCH-003",
      "name": "분산 락 사용 필수",
      "severity": "ERROR",
      "description": "Scheduler는 분산 락 사용 필수 (여러 인스턴스 동시 실행 방지)",
      "pattern": "(lockPort\\.tryLock\\(|DistributedLockPort)",
      "antiPattern": "@Scheduled(?!.*lockPort)",
      "zeroTolerance": true,
      "rationale": "여러 인스턴스에서 동시 실행 방지",
      "archUnitTest": "SchedulerArchTest.scheduler_MustUseDistributedLock"
    },
    {
      "ruleId": "APP-SCH-004",
      "name": "최대 반복 횟수 제한",
      "severity": "ERROR",
      "description": "Scheduler는 최대 반복 횟수 제한 필수 (무한 루프 방지)",
      "pattern": "(MAX_ITERATIONS|for\\s*\\(int\\s+\\w+\\s*=\\s*0;\\s*\\w+\\s*<\\s*MAX)",
      "antiPattern": "(while\\s*\\(true\\)|while\\s*\\(!\\w+\\.isEmpty\\(\\)\\))",
      "zeroTolerance": true,
      "rationale": "무한 루프 방지",
      "archUnitTest": "SchedulerArchTest.scheduler_MustHaveMaxIterations"
    },
    {
      "ruleId": "APP-SCH-005",
      "name": "메트릭 기록 필수",
      "severity": "WARNING",
      "description": "Scheduler는 메트릭 기록 권장 (실행 시간, 처리 건수, 성공/실패율)",
      "pattern": "(MeterRegistry|Timer\\.start|counter\\.increment)"
    },
    {
      "ruleId": "APP-SCH-006",
      "name": "finally 블록에서 unlock",
      "severity": "ERROR",
      "description": "분산 락 해제는 finally 블록에서 필수 (예외 발생해도 락 해제 보장)",
      "pattern": "finally\\s*\\{[^}]*lockPort\\.unlock\\(",
      "antiPattern": "try\\s*\\{[^}]*lockPort\\.unlock\\(",
      "zeroTolerance": true,
      "rationale": "예외 발생해도 락 해제 보장",
      "archUnitTest": "SchedulerArchTest.scheduler_MustUnlockInFinally"
    },
    {
      "ruleId": "APP-SCH-007",
      "name": "개별 실패 격리",
      "severity": "WARNING",
      "description": "Scheduler는 개별 항목 처리 시 try-catch로 실패 격리 권장",
      "pattern": "for\\s*\\([^)]+\\)\\s*\\{\\s*try\\s*\\{[^}]+process",
      "rationale": "한 건 실패해도 나머지 계속 처리"
    },
    {
      "ruleId": "APP-SCH-008",
      "name": "패키지 위치 준수",
      "severity": "ERROR",
      "description": "Scheduler는 application.[bc].scheduler 패키지에 위치",
      "pattern": "application\\.\\w+\\.scheduler\\.\\w+Scheduler",
      "antiPattern": "(domain\\.|adapter\\.|application\\.common).*Scheduler",
      "archUnitTest": "SchedulerArchTest.scheduler_MustBeInCorrectPackage"
    },
    {
      "ruleId": "APP-SCH-009",
      "name": "Lombok 금지",
      "severity": "ERROR",
      "description": "Scheduler에서 Lombok 어노테이션 사용 금지",
      "antiPattern": "@(RequiredArgsConstructor|AllArgsConstructor)",
      "zeroTolerance": true,
      "archUnitTest": "SchedulerArchTest.scheduler_MustNotUseLombok"
    },
    {
      "ruleId": "APP-SCH-010",
      "name": "로깅 필수",
      "severity": "WARNING",
      "description": "Scheduler는 시작/종료/실패 로깅 필수",
      "pattern": "log\\.info\\(\"\\[.*\\]\\s*(Starting|Completed)\"",
      "antiPattern": "class\\s+\\w+Scheduler(?!.*log\\.)"
    },
    {
      "ruleId": "APP-SCH-011",
      "name": "DTO 변환은 Assembler에서",
      "severity": "WARNING",
      "description": "Scheduler 내 DTO 변환 로직 금지, Assembler 사용",
      "pattern": "assembler\\.(toMessage|to)",
      "antiPattern": "(private\\s+\\w+\\s+toMessage\\(|new\\s+\\w+Message\\()"
    }
  ]
}
```

---

## Zero-Tolerance 규칙 요약

| 카테고리 | 규칙 ID | 규칙 내용 |
|---------|---------|----------|
| EVENT_REGISTRY | APP-ER-002 | TransactionSynchronizationManager 사용 (ThreadLocal 금지) |
| EVENT_REGISTRY | APP-ER-005 | afterCommit에서 발행 |
| EVENT_REGISTRY | APP-ER-006 | Lombok 금지 |
| EVENT_LISTENER | APP-EL-003 | try-catch 예외 처리 필수 |
| EVENT_LISTENER | APP-EL-004 | 부가 작업만 처리 |
| EVENT_LISTENER | APP-EL-007 | Lombok 금지 |
| SCHEDULER | APP-SCH-002 | UseCase/Service만 호출 (Port/Manager 금지) |
| SCHEDULER | APP-SCH-003 | 분산 락 사용 필수 |
| SCHEDULER | APP-SCH-004 | 최대 반복 횟수 제한 |
| SCHEDULER | APP-SCH-006 | finally에서 unlock |
| SCHEDULER | APP-SCH-009 | Lombok 금지 |

---

## 코드 예시

### TransactionEventRegistry

```java
@Component
public class TransactionEventRegistry {
    private final ApplicationEventPublisher eventPublisher;

    public TransactionEventRegistry(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void registerForPublish(DomainEvent event) {
        TransactionSynchronizationManager.registerSynchronization(
            new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    eventPublisher.publishEvent(event);
                }
            }
        );
    }
}
```

### EventListener

```java
@Component
public class OrderCacheEventListener {
    private final OrderCachePort cachePort;
    private static final Logger log = LoggerFactory.getLogger(OrderCacheEventListener.class);

    public OrderCacheEventListener(OrderCachePort cachePort) {
        this.cachePort = cachePort;
    }

    @EventListener
    public void handleOrderPlaced(OrderPlacedEvent event) {
        try {
            cachePort.evict(OrderCacheKey.of(event.orderId().value()));
        } catch (Exception e) {
            log.warn("캐시 무효화 실패: orderId={}", event.orderId(), e);
        }
    }
}
```

### Scheduler

```java
@Component
@ConditionalOnProperty(name = "scheduler.order-cleanup.enabled", havingValue = "true")
public class OrderCleanupScheduler {
    private static final int MAX_ITERATIONS = 1000;
    private static final Logger log = LoggerFactory.getLogger(OrderCleanupScheduler.class);
    
    private final CleanupExpiredOrdersUseCase cleanupUseCase;
    private final DistributedLockPort lockPort;
    private final MeterRegistry meterRegistry;

    public OrderCleanupScheduler(
        CleanupExpiredOrdersUseCase cleanupUseCase,
        DistributedLockPort lockPort,
        MeterRegistry meterRegistry
    ) {
        this.cleanupUseCase = cleanupUseCase;
        this.lockPort = lockPort;
        this.meterRegistry = meterRegistry;
    }

    @Scheduled(cron = "0 0 2 * * *")
    public void cleanup() {
        OrderLockKey lockKey = OrderLockKey.of("cleanup");
        
        if (!lockPort.tryLock(lockKey, Duration.ofMinutes(10))) {
            log.info("[OrderCleanup] 락 획득 실패, 다른 인스턴스가 실행 중");
            return;
        }
        
        try {
            log.info("[OrderCleanup] Starting cleanup");
            Timer.Sample sample = Timer.start(meterRegistry);
            
            int processed = 0;
            for (int i = 0; i < MAX_ITERATIONS; i++) {
                List<OrderId> batch = cleanupUseCase.execute();
                if (batch.isEmpty()) break;
                processed += batch.size();
            }
            
            sample.stop(meterRegistry.timer("scheduler.order.cleanup"));
            log.info("[OrderCleanup] Completed, processed={}", processed);
            
        } finally {
            lockPort.unlock(lockKey);
        }
    }
}
```

---

## 관련 문서

- [Transaction Event Registry Guide](docs/coding_convention/03-application-layer/event/transaction-event-registry-guide.md)
- [Event Listener Guide](docs/coding_convention/03-application-layer/listener/event-listener-guide.md)
- [Scheduler Guide](docs/coding_convention/03-application-layer/scheduler/scheduler-guide.md)

---

**버전**: 2.0.0 (JSON 구조로 변환)
**작성일**: 2025-12-09
