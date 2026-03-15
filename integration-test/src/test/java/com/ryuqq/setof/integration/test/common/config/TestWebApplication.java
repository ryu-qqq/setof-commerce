package com.ryuqq.setof.integration.test.common.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Web E2E 통합 테스트 전용 Application.
 *
 * <p>Legacy 모듈, Admin 모듈, Security/OAuth2를 제외한 Web API Application 구성입니다. 테스트 환경에서는 단일 H2
 * DataSource를 사용하므로 레거시 Dual DataSource 설정이 불필요합니다.
 *
 * <p>Application 레이어는 외부 포트 의존성이 없는 도메인만 화이트리스트로 스캔합니다.
 */
@SpringBootApplication
@ComponentScan(
        basePackages = {
            "com.ryuqq.setof.config",
            "com.ryuqq.setof.domain",
            // Application - 외부 포트 의존이 없는 도메인만 스캔
            "com.ryuqq.setof.application.common",
            "com.ryuqq.setof.application.banner",
            "com.ryuqq.setof.application.navigation",
            "com.ryuqq.setof.application.contentpage",
            "com.ryuqq.setof.application.brand",
            "com.ryuqq.setof.application.category",
            "com.ryuqq.setof.application.seller",
            "com.ryuqq.setof.application.product",
            "com.ryuqq.setof.application.productgroup",
            "com.ryuqq.setof.application.productdescription",
            "com.ryuqq.setof.application.productgroupdescription",
            "com.ryuqq.setof.application.productgroupimage",
            "com.ryuqq.setof.application.productnotice",
            "com.ryuqq.setof.application.commoncode",
            "com.ryuqq.setof.application.commoncodetype",
            "com.ryuqq.setof.application.selleroption",
            "com.ryuqq.setof.application.refundpolicy",
            "com.ryuqq.setof.application.shippingpolicy",
            // Adapter-In (Web API v1 - 외부 포트 의존 없는 컨트롤러만)
            "com.ryuqq.setof.adapter.in.rest.v1.banner",
            "com.ryuqq.setof.adapter.in.rest.v1.brand",
            "com.ryuqq.setof.adapter.in.rest.v1.category",
            "com.ryuqq.setof.adapter.in.rest.v1.content",
            "com.ryuqq.setof.adapter.in.rest.v1.navigation",
            "com.ryuqq.setof.adapter.in.rest.v1.productgroup",
            "com.ryuqq.setof.adapter.in.rest.v1.seller",
            "com.ryuqq.setof.adapter.in.rest.v1.common",
            "com.ryuqq.setof.adapter.in.rest.common.error",
            "com.ryuqq.setof.adapter.in.rest.common.exception",
            "com.ryuqq.setof.adapter.in.rest.common.response",
            // Adapter-Out
            "com.ryuqq.setof.adapter.out.persistence",
        },
        excludeFilters =
                @ComponentScan.Filter(
                        type = FilterType.REGEX,
                        pattern = "com\\.ryuqq\\.setof\\.storage\\.legacy\\..*"))
@EnableJpaRepositories(basePackages = "com.ryuqq.setof.adapter.out.persistence")
@EntityScan(basePackages = "com.ryuqq.setof.adapter.out.persistence")
public class TestWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestWebApplication.class, args);
    }
}
