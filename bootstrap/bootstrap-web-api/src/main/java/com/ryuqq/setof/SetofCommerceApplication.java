package com.ryuqq.setof;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Setof Commerce Application Entry Point.
 *
 * <p>Spring Boot 애플리케이션의 메인 클래스입니다. 헥사고날 아키텍처 기반으로 구성된 커머스 플랫폼입니다.
 *
 * @author ryuqq
 * @since 1.0.0
 */
@SpringBootApplication(scanBasePackages = "com.ryuqq.setof")
public class SetofCommerceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SetofCommerceApplication.class, args);
    }
}
