package com.ryuqq.setof.batch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

/**
 * Batch Application 진입점
 *
 * <p>레거시/신규 DB 배치 작업을 처리하는 Spring Batch 애플리케이션입니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@SpringBootApplication
@ConfigurationPropertiesScan
public class BatchApplication {

    public static void main(String[] args) {
        SpringApplication.run(BatchApplication.class, args);
    }
}
