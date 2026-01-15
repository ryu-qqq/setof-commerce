# APPLICATION Layer 클래스 템플릿 (5개)

## 개요

이 문서는 APPLICATION Layer에서 사용하는 클래스 템플릿을 정의합니다.

## 템플릿 목록

| Class Type | Naming Pattern | Description |
|------------|----------------|-------------|
| CLASS | `{Entity}PersistBundle` | Persist Bundle - Class (mutable). ID Enrichment 패턴... |
| COMPONENT | `{Entity}EventListener` | Event Listener - 외부 호출 전용. @Async + @EventListener... |
| INTERFACE | `Create{Entity}UseCase` | Command UseCase 인터페이스 - execute(Command) 단일 메서드. |
| RECORD | `Create{Entity}Command` | Command DTO - Record 필수. 생성 커맨드. |
| SERVICE | `Process{Entity}OutboxService` | Scheduler Service - Outbox 처리. 분산락 필수. Manager + F... |

---

## 상세 템플릿

### CLASS

**설명**: Persist Bundle - Class (mutable). ID Enrichment 패턴.

**네이밍 패턴**: `{Entity}PersistBundle`

**템플릿 코드**:
```java
package com.ryuqq.application.{bc}.dto.bundle;

import com.ryuqq.domain.{bc}.aggregate.{Entity};
import com.ryuqq.domain.{bc}.aggregate.{Entity}Outbox;

/**
 * {Entity}PersistBundle - {Entity} 영속화 번들
 *
 * <p><strong>핵심 규칙:</strong>
 * <ul>
 *   <li>Class로 정의 (mutable) - ID Enrichment 필요</li>
 *   <li>with{Entity}Id() 메서드로 ID 세팅</li>
 *   <li>Law of Demeter 준수: ID Getter 위임</li>
 * </ul>
 *
 * @author ryu-qqq
 * @since {date}
 */
public class {Entity}PersistBundle {

    private final {Entity} {entity};
    private final {Entity}Outbox outbox;

    public {Entity}PersistBundle({Entity} {entity}, {Entity}Outbox outbox) {
        this.{entity} = {entity};
        this.outbox = outbox;
    }

    public void with{Entity}Id(Long id) {
        {entity}.withId(id);
        outbox.with{Entity}Id(id);
    }

    public {Entity} {entity}() {
        return {entity};
    }

    public {Entity}Outbox outbox() {
        return outbox;
    }

    public Long {entity}Id() {
        return {entity}.id();
    }

    public Long outboxId() {
        return outbox.id();
    }
}
```

---

### COMPONENT

**설명**: Event Listener - 외부 호출 전용. @Async + @EventListener. 멱등키 필수.

**네이밍 패턴**: `{Entity}EventListener`

**템플릿 코드**:
```java
package com.ryuqq.application.{bc}.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import com.ryuqq.application.{bc}.manager.client.{External}ClientManager;
import com.ryuqq.application.{bc}.manager.command.{Entity}OutboxPersistenceManager;
import com.ryuqq.domain.{bc}.event.{Entity}CreatedEvent;

/**
 * {Entity}EventListener - {Entity} 이벤트 리스너
 *
 * <p><strong>핵심 규칙:</strong>
 * <ul>
 *   <li>@Async + @EventListener (NOT @TransactionalEventListener)</li>
 *   <li>외부 호출 전용 (내부 도메인 통신 금지)</li>
 *   <li>멱등키(outboxId) 필수 전달</li>
 *   <li>실패 시 로깅만 (예외 전파 금지)</li>
 *   <li>ClientManager 의존 (ClientPort 직접 주입 금지)</li>
 * </ul>
 *
 * @author ryu-qqq
 * @since {date}
 */
@Component
public class {Entity}EventListener {

    private static final Logger log = LoggerFactory.getLogger({Entity}EventListener.class);

    private final {External}ClientManager clientManager;
    private final {Entity}OutboxPersistenceManager outboxPersistenceManager;

    public {Entity}EventListener(
            {External}ClientManager clientManager,
            {Entity}OutboxPersistenceManager outboxPersistenceManager) {
        this.clientManager = clientManager;
        this.outboxPersistenceManager = outboxPersistenceManager;
    }

    @Async
    @EventListener
    public void handle{Entity}Created({Entity}CreatedEvent event) {
        try {
            clientManager.send(event.payload(), event.outboxId());
            outboxPersistenceManager.markSuccess(event.outboxId());
        } catch (Exception e) {
            log.warn("Failed to process event for outbox: {}", event.outboxId(), e);
            // 실패 시 로깅만 - Scheduler가 재처리
        }
    }
}
```

---

### INTERFACE

**설명**: Command UseCase 인터페이스 - execute(Command) 단일 메서드.

**네이밍 패턴**: `Create{Entity}UseCase`

**템플릿 코드**:
```java
package com.ryuqq.application.{bc}.port.in.command;

import com.ryuqq.application.{bc}.dto.command.Create{Entity}Command;
import com.ryuqq.application.{bc}.dto.response.{Entity}IdResponse;

/**
 * Create{Entity}UseCase - {Entity} 생성 UseCase
 *
 * <p>Command UseCase는 execute() 단일 메서드만 정의합니다.
 *
 * @author ryu-qqq
 * @since {date}
 */
public interface Create{Entity}UseCase {

    {Entity}IdResponse execute(Create{Entity}Command command);
}
```

---

### RECORD

**설명**: Command DTO - Record 필수. 생성 커맨드.

**네이밍 패턴**: `Create{Entity}Command`

**템플릿 코드**:
```java
package com.ryuqq.application.{bc}.dto.command;

/**
 * Create{Entity}Command - {Entity} 생성 커맨드
 *
 * <p>Command DTO는 반드시 Record로 정의합니다.
 *
 * @param name {Entity} 이름
 * @param description {Entity} 설명
 * @author ryu-qqq
 * @since {date}
 */
public record Create{Entity}Command(
        String name,
        String description
) {
}
```

---

### SERVICE

**설명**: Scheduler Service - Outbox 처리. 분산락 필수. Manager + Factory 의존 (Port 금지).

**네이밍 패턴**: `Process{Entity}OutboxService`

**템플릿 코드**:
```java
package com.ryuqq.application.{bc}.service.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.ryuqq.application.{bc}.factory.query.{Entity}OutboxQueryFactory;
import com.ryuqq.application.{bc}.manager.client.{External}ClientManager;
import com.ryuqq.application.{bc}.manager.read.{Entity}OutboxReadManager;
import com.ryuqq.application.{bc}.manager.command.{Entity}OutboxPersistenceManager;
import com.ryuqq.application.{bc}.port.in.scheduler.Process{Entity}OutboxUseCase;
import com.ryuqq.application.common.port.LockManager;
import com.ryuqq.domain.{bc}.aggregate.{Entity}Outbox;
import com.ryuqq.domain.{bc}.query.criteria.{Entity}OutboxSearchCriteria;
import java.util.List;

/**
 * Process{Entity}OutboxService - {Entity} Outbox 처리 Scheduler Service
 *
 * <p><strong>핵심 규칙:</strong>
 * <ul>
 *   <li>분산락 필수 (LockManager)</li>
 *   <li>Manager + Factory 의존 (Port 직접 주입 금지)</li>
 *   <li>Criteria는 Factory를 통해 생성 (직접 생성 금지)</li>
 *   <li>finally 블록에서 락 해제</li>
 *   <li>건별 독립 트랜잭션</li>
 * </ul>
 *
 * @author ryu-qqq
 * @since {date}
 */
@Component
public class Process{Entity}OutboxService implements Process{Entity}OutboxUseCase {

    private static final Logger log = LoggerFactory.getLogger(Process{Entity}OutboxService.class);
    private static final String LOCK_KEY = "{entity}-outbox-processor";
    private static final int BATCH_SIZE = 100;
    private static final int MAX_RETRY_COUNT = 3;

    private final LockManager lockManager;
    private final {Entity}OutboxQueryFactory outboxQueryFactory;
    private final {Entity}OutboxReadManager outboxReadManager;
    private final {Entity}OutboxPersistenceManager outboxPersistenceManager;
    private final {External}ClientManager clientManager;

    public Process{Entity}OutboxService(
            LockManager lockManager,
            {Entity}OutboxQueryFactory outboxQueryFactory,
            {Entity}OutboxReadManager outboxReadManager,
            {Entity}OutboxPersistenceManager outboxPersistenceManager,
            {External}ClientManager clientManager) {
        this.lockManager = lockManager;
        this.outboxQueryFactory = outboxQueryFactory;
        this.outboxReadManager = outboxReadManager;
        this.outboxPersistenceManager = outboxPersistenceManager;
        this.clientManager = clientManager;
    }

    @Override
    public void execute() {
        if (!lockManager.tryLock(LOCK_KEY)) {
            log.debug("Failed to acquire lock for {}", LOCK_KEY);
            return;
        }
        try {
            processOutboxBatch();
        } finally {
            lockManager.unlock(LOCK_KEY);
        }
    }

    private void processOutboxBatch() {
        {Entity}OutboxSearchCriteria criteria = outboxQueryFactory.createPendingCriteria(BATCH_SIZE, MAX_RETRY_COUNT);
        List<{Entity}Outbox> pendingOutboxes = outboxReadManager.findByCriteria(criteria);

        for ({Entity}Outbox outbox : pendingOutboxes) {
            processOutbox(outbox);
        }
    }

    private void processOutbox({Entity}Outbox outbox) {
        try {
            clientManager.send(outbox.payload(), outbox.id());
            outboxPersistenceManager.markSuccess(outbox.id());
        } catch (Exception e) {
            log.warn("Failed to process outbox: {}", outbox.id(), e);
            handleFailure(outbox);
        }
    }

    private void handleFailure({Entity}Outbox outbox) {
        if (outbox.retryCount() >= MAX_RETRY_COUNT) {
            outboxPersistenceManager.markFailed(outbox.id());
        } else {
            outboxPersistenceManager.incrementRetryCount(outbox.id());
        }
    }
}
```

---

