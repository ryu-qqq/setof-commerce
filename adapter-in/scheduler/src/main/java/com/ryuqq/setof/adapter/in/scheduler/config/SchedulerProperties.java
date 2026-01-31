package com.ryuqq.setof.adapter.in.scheduler.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 스케줄러 설정 프로퍼티.
 *
 * <p>환경별 설정 파일(scheduler-{profile}.yml)에서 값을 주입받습니다.
 *
 * @see com.ryuqq.setof.adapter.in.scheduler.seller.SellerAuthOutboxScheduler
 */
@ConfigurationProperties(prefix = "scheduler")
public record SchedulerProperties(Jobs jobs) {

    public record Jobs(SellerAuthOutbox sellerAuthOutbox) {}

    public record SellerAuthOutbox(ProcessPending processPending, RecoverTimeout recoverTimeout) {}

    public record ProcessPending(
            boolean enabled, String cron, String timezone, int batchSize, int delaySeconds) {}

    public record RecoverTimeout(
            boolean enabled, String cron, String timezone, int batchSize, long timeoutSeconds) {}
}
