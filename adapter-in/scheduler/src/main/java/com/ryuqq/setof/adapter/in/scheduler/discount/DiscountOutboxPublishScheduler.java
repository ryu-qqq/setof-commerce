package com.ryuqq.setof.adapter.in.scheduler.discount;

import com.ryuqq.setof.adapter.in.scheduler.annotation.SchedulerJob;
import com.ryuqq.setof.adapter.in.scheduler.config.SchedulerProperties;
import com.ryuqq.setof.application.discount.port.in.command.PublishDiscountOutboxUseCase;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Discount Outbox PENDING → SQS 발행 스케줄러.
 *
 * <p>1초 주기로 PENDING 상태의 아웃박스를 조회하여 SQS로 발행합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
@ConditionalOnProperty(
        name = "scheduler.jobs.discount-outbox.publish-pending.enabled",
        havingValue = "true")
public class DiscountOutboxPublishScheduler {

    private final PublishDiscountOutboxUseCase publishDiscountOutboxUseCase;
    private final SchedulerProperties properties;

    public DiscountOutboxPublishScheduler(
            PublishDiscountOutboxUseCase publishDiscountOutboxUseCase,
            SchedulerProperties properties) {
        this.publishDiscountOutboxUseCase = publishDiscountOutboxUseCase;
        this.properties = properties;
    }

    @Scheduled(
            cron = "${scheduler.jobs.discount-outbox.publish-pending.cron}",
            zone = "${scheduler.jobs.discount-outbox.publish-pending.timezone}")
    @SchedulerJob("DiscountOutboxPublish")
    public int publish() {
        int batchSize = properties.jobs().discountOutbox().publishPending().batchSize();
        return publishDiscountOutboxUseCase.execute(batchSize);
    }
}
