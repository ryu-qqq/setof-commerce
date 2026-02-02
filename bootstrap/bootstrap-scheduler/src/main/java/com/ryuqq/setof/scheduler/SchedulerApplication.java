package com.ryuqq.setof.scheduler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Scheduler Application 진입점.
 *
 * <p>Outbox 패턴 기반 스케줄러 애플리케이션입니다.
 *
 * <ul>
 *   <li>SellerAuthOutbox 처리 (Identity 서비스 연동)
 *   <li>타임아웃 Outbox 복구
 * </ul>
 */
@SpringBootApplication(
        scanBasePackages = {
            "com.ryuqq.setof.scheduler",
            "com.ryuqq.setof.adapter.in.scheduler",
            "com.ryuqq.setof.application",
            "com.ryuqq.setof.adapter.out"
        })
@EntityScan(basePackages = "com.ryuqq.setof.adapter.out.persistence")
@EnableJpaRepositories(basePackages = "com.ryuqq.setof.adapter.out.persistence")
@ConfigurationPropertiesScan(
        basePackages = {"com.ryuqq.setof.scheduler", "com.ryuqq.setof.adapter.in.scheduler"})
@EnableScheduling
public class SchedulerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SchedulerApplication.class, args);
    }
}
