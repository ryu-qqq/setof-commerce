package com.ryuqq.setof.adapter.in.scheduler.discount;

import com.ryuqq.setof.adapter.in.scheduler.annotation.SchedulerJob;
import com.ryuqq.setof.adapter.in.scheduler.config.SchedulerProperties;
import com.ryuqq.setof.application.discount.port.in.command.RecoverStuckDiscountOutboxUseCase;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Discount Outbox Stuck 복구 스케줄러.
 *
 * <p>5분 주기로 PUBLISHED 상태에서 일정 시간 이상 멈춘 아웃박스를 PENDING으로 복구합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
@ConditionalOnProperty(
        name = "scheduler.jobs.discount-outbox.recover-stuck.enabled",
        havingValue = "true")
public class DiscountOutboxRecoverScheduler {

    private final RecoverStuckDiscountOutboxUseCase recoverStuckDiscountOutboxUseCase;
    private final SchedulerProperties properties;

    public DiscountOutboxRecoverScheduler(
            RecoverStuckDiscountOutboxUseCase recoverStuckDiscountOutboxUseCase,
            SchedulerProperties properties) {
        this.recoverStuckDiscountOutboxUseCase = recoverStuckDiscountOutboxUseCase;
        this.properties = properties;
    }

    @Scheduled(
            cron = "${scheduler.jobs.discount-outbox.recover-stuck.cron}",
            zone = "${scheduler.jobs.discount-outbox.recover-stuck.timezone}")
    @SchedulerJob("DiscountOutboxRecover")
    public int recover() {
        SchedulerProperties.RecoverStuck config = properties.jobs().discountOutbox().recoverStuck();
        return recoverStuckDiscountOutboxUseCase.execute(
                config.timeoutSeconds(), config.batchSize());
    }
}
