# SCHEDULER Layer 클래스 템플릿 (6개)

## 개요

이 문서는 SCHEDULER Layer에서 사용하는 클래스 템플릿을 정의합니다.

## 템플릿 목록

| Class Type | Naming Pattern | Description |
|------------|----------------|-------------|
| ^[A-Z][a-zA-Z]*CleanupScheduler$ | `THIN_SCHEDULER_CLEANUP` | 정리 전용 Thin Scheduler 템플릿. 오래된 데이터 정리, 만료 처리 등에 사용합... |
| ^[A-Z][a-zA-Z]*OutboxScheduler$ | `THIN_SCHEDULER` | Thin Scheduler 기본 템플릿. @Scheduled로 주기적 실행을 트리거하고, ... |
| ^[A-Z][a-zA-Z]*RetryScheduler$ | `THIN_SCHEDULER_RETRY` | 재시도 전용 Thin Scheduler 템플릿. 실패한 외부 호출을 재시도합니다. Outb... |
| THIN_SCHEDULER | `^[A-Z][a-zA-Z]*OutboxScheduler$` | Thin Scheduler 기본 템플릿. @Scheduled로 주기적 실행을 트리거하고, ... |
| THIN_SCHEDULER_CLEANUP | `^[A-Z][a-zA-Z]*CleanupScheduler$` | 정리 전용 Thin Scheduler 템플릿. 오래된 데이터 정리, 만료 처리 등에 사용합... |
| THIN_SCHEDULER_RETRY | `^[A-Z][a-zA-Z]*RetryScheduler$` | 재시도 전용 Thin Scheduler 템플릿. 실패한 외부 호출을 재시도합니다. Outb... |

---

## 상세 템플릿

### ^[A-Z][a-zA-Z]*CleanupScheduler$

**설명**: 정리 전용 Thin Scheduler 템플릿. 오래된 데이터 정리, 만료 처리 등에 사용합니다. cron 표현식으로 새벽 시간대 실행을 권장합니다.

**네이밍 패턴**: `THIN_SCHEDULER_CLEANUP`

**템플릿 코드**:
```java
package com.{company}.adapter.in.scheduler.{domain};

import com.{company}.application.{domain}.port.in.scheduler.Cleanup{Entity}UseCase;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * {Entity} Cleanup Scheduler.
 *
 * <p>오래된 {Entity} 데이터를 정리합니다.
 * 시스템 부하가 적은 새벽 시간대에 실행합니다.
 */
@Component
public class {Entity}CleanupScheduler {

    private final Cleanup{Entity}UseCase cleanup{Entity}UseCase;

    public {Entity}CleanupScheduler(Cleanup{Entity}UseCase cleanup{Entity}UseCase) {
        this.cleanup{Entity}UseCase = cleanup{Entity}UseCase;
    }

    /**
     * 오래된 {Entity} 데이터를 정리합니다.
     *
     * <p>cron: 매일 새벽 3시에 실행
     */
    @Scheduled(cron = "0 0 3 * * *")
    public void cleanup() {
        cleanup{Entity}UseCase.execute();
    }
}
```

---

### ^[A-Z][a-zA-Z]*OutboxScheduler$

**설명**: Thin Scheduler 기본 템플릿. @Scheduled로 주기적 실행을 트리거하고, UseCase.execute()만 호출합니다. 분산락, 배치 처리, 상태 관리 등 모든 비즈니스 로직은 Application Layer의 Scheduler Service에 위임합니다.

**네이밍 패턴**: `THIN_SCHEDULER`

**템플릿 코드**:
```java
package com.{company}.adapter.in.scheduler.{domain};

import com.{company}.application.{domain}.port.in.scheduler.Process{Entity}OutboxUseCase;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * {Entity} Outbox Scheduler.
 *
 * <p>Thin Scheduler 패턴을 따릅니다:
 * <ul>
 *   <li>@Scheduled로 주기적 실행 트리거만 담당</li>
 *   <li>UseCase.execute() 단일 호출</li>
 *   <li>비즈니스 로직 없음 (분산락, 배치 처리 등은 UseCase에서)</li>
 * </ul>
 */
@Component
public class {Entity}OutboxScheduler {

    private final Process{Entity}OutboxUseCase process{Entity}OutboxUseCase;

    public {Entity}OutboxScheduler(Process{Entity}OutboxUseCase process{Entity}OutboxUseCase) {
        this.process{Entity}OutboxUseCase = process{Entity}OutboxUseCase;
    }

    /**
     * Outbox 이벤트를 주기적으로 처리합니다.
     *
     * <p>fixedDelay: 이전 작업 완료 후 5초 대기 (작업 중첩 방지)
     */
    @Scheduled(fixedDelay = 5000)
    public void processOutbox() {
        process{Entity}OutboxUseCase.execute();
    }
}
```

---

### ^[A-Z][a-zA-Z]*RetryScheduler$

**설명**: 재시도 전용 Thin Scheduler 템플릿. 실패한 외부 호출을 재시도합니다. Outbox Scheduler보다 긴 주기(1분)를 사용합니다.

**네이밍 패턴**: `THIN_SCHEDULER_RETRY`

**템플릿 코드**:
```java
package com.{company}.adapter.in.scheduler.{domain};

import com.{company}.application.{domain}.port.in.scheduler.Retry{Entity}UseCase;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * {Entity} Retry Scheduler.
 *
 * <p>실패한 외부 호출을 주기적으로 재시도합니다.
 * Outbox Scheduler보다 긴 주기(1분)를 사용하여 외부 시스템 부하를 줄입니다.
 */
@Component
public class {Entity}RetryScheduler {

    private final Retry{Entity}UseCase retry{Entity}UseCase;

    public {Entity}RetryScheduler(Retry{Entity}UseCase retry{Entity}UseCase) {
        this.retry{Entity}UseCase = retry{Entity}UseCase;
    }

    /**
     * 실패한 {Entity} 처리를 주기적으로 재시도합니다.
     *
     * <p>fixedDelay: 이전 작업 완료 후 1분 대기
     */
    @Scheduled(fixedDelay = 60000)
    public void retryFailed() {
        retry{Entity}UseCase.execute();
    }
}
```

---

### THIN_SCHEDULER

**설명**: Thin Scheduler 기본 템플릿. @Scheduled로 주기적 실행을 트리거하고, UseCase.execute()만 호출합니다. 분산락, 배치 처리, 상태 관리 등 모든 비즈니스 로직은 Application Layer의 Scheduler Service에 위임합니다.

**네이밍 패턴**: `^[A-Z][a-zA-Z]*OutboxScheduler$`

**템플릿 코드**:
```java
package com.{company}.adapter.in.scheduler.{domain};

import com.{company}.application.{domain}.port.in.scheduler.Process{Entity}OutboxUseCase;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * {Entity} Outbox Scheduler.
 *
 * <p>Thin Scheduler 패턴을 따릅니다:
 * <ul>
 *   <li>@Scheduled로 주기적 실행 트리거만 담당</li>
 *   <li>UseCase.execute() 단일 호출</li>
 *   <li>비즈니스 로직 없음 (분산락, 배치 처리 등은 UseCase에서)</li>
 * </ul>
 */
@Component
public class {Entity}OutboxScheduler {

    private final Process{Entity}OutboxUseCase process{Entity}OutboxUseCase;

    public {Entity}OutboxScheduler(Process{Entity}OutboxUseCase process{Entity}OutboxUseCase) {
        this.process{Entity}OutboxUseCase = process{Entity}OutboxUseCase;
    }

    /**
     * Outbox 이벤트를 주기적으로 처리합니다.
     *
     * <p>fixedDelay: 이전 작업 완료 후 5초 대기 (작업 중첩 방지)
     */
    @Scheduled(fixedDelay = 5000)
    public void processOutbox() {
        process{Entity}OutboxUseCase.execute();
    }
}
```

---

### THIN_SCHEDULER_CLEANUP

**설명**: 정리 전용 Thin Scheduler 템플릿. 오래된 데이터 정리, 만료 처리 등에 사용합니다. cron 표현식으로 새벽 시간대 실행을 권장합니다.

**네이밍 패턴**: `^[A-Z][a-zA-Z]*CleanupScheduler$`

**템플릿 코드**:
```java
package com.{company}.adapter.in.scheduler.{domain};

import com.{company}.application.{domain}.port.in.scheduler.Cleanup{Entity}UseCase;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * {Entity} Cleanup Scheduler.
 *
 * <p>오래된 {Entity} 데이터를 정리합니다.
 * 시스템 부하가 적은 새벽 시간대에 실행합니다.
 */
@Component
public class {Entity}CleanupScheduler {

    private final Cleanup{Entity}UseCase cleanup{Entity}UseCase;

    public {Entity}CleanupScheduler(Cleanup{Entity}UseCase cleanup{Entity}UseCase) {
        this.cleanup{Entity}UseCase = cleanup{Entity}UseCase;
    }

    /**
     * 오래된 {Entity} 데이터를 정리합니다.
     *
     * <p>cron: 매일 새벽 3시에 실행
     */
    @Scheduled(cron = "0 0 3 * * *")
    public void cleanup() {
        cleanup{Entity}UseCase.execute();
    }
}
```

---

### THIN_SCHEDULER_RETRY

**설명**: 재시도 전용 Thin Scheduler 템플릿. 실패한 외부 호출을 재시도합니다. Outbox Scheduler보다 긴 주기(1분)를 사용합니다.

**네이밍 패턴**: `^[A-Z][a-zA-Z]*RetryScheduler$`

**템플릿 코드**:
```java
package com.{company}.adapter.in.scheduler.{domain};

import com.{company}.application.{domain}.port.in.scheduler.Retry{Entity}UseCase;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * {Entity} Retry Scheduler.
 *
 * <p>실패한 외부 호출을 주기적으로 재시도합니다.
 * Outbox Scheduler보다 긴 주기(1분)를 사용하여 외부 시스템 부하를 줄입니다.
 */
@Component
public class {Entity}RetryScheduler {

    private final Retry{Entity}UseCase retry{Entity}UseCase;

    public {Entity}RetryScheduler(Retry{Entity}UseCase retry{Entity}UseCase) {
        this.retry{Entity}UseCase = retry{Entity}UseCase;
    }

    /**
     * 실패한 {Entity} 처리를 주기적으로 재시도합니다.
     *
     * <p>fixedDelay: 이전 작업 완료 후 1분 대기
     */
    @Scheduled(fixedDelay = 60000)
    public void retryFailed() {
        retry{Entity}UseCase.execute();
    }
}
```

---

