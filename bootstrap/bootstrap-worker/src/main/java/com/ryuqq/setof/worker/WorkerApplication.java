package com.ryuqq.setof.worker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Worker Application 진입점.
 *
 * <p>SQS Consumer 기반 워커 애플리케이션입니다.
 *
 * <ul>
 *   <li>Discount Outbox 가격 재계산 (SQS 수신)
 * </ul>
 */
@SpringBootApplication(
        scanBasePackages = {
            "com.ryuqq.setof.worker",
            "com.ryuqq.setof.adapter.in.sqs",
            "com.ryuqq.setof.application",
            "com.ryuqq.setof.adapter.out",
            "com.ryuqq.setof.storage.legacy"
        })
@EntityScan(basePackages = "com.ryuqq.setof.adapter.out.persistence")
@EnableJpaRepositories(basePackages = "com.ryuqq.setof.adapter.out.persistence")
@ConfigurationPropertiesScan(
        basePackages = {"com.ryuqq.setof.worker", "com.ryuqq.setof.adapter.in.sqs"})
public class WorkerApplication {

    public static void main(String[] args) {
        SpringApplication.run(WorkerApplication.class, args);
    }
}
