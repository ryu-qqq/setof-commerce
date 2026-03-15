package com.ryuqq.setof.integration.test.common.config;

import com.ryuqq.setof.admin.config.security.SecurityConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * E2E 통합 테스트 전용 Application.
 *
 * <p>Legacy 모듈을 제외한 Admin Application 구성입니다. 테스트 환경에서는 단일 H2 DataSource를 사용하므로 레거시 Dual DataSource
 * 설정이 불필요합니다.
 */
@SpringBootApplication
@ComponentScan(
        basePackages = {
            "com.ryuqq.setof.admin.config",
            "com.ryuqq.setof.domain",
            "com.ryuqq.setof.application.common",
            "com.ryuqq.setof.application.commoncode",
            "com.ryuqq.setof.application.commoncodetype",
            "com.ryuqq.setof.application.product",
            "com.ryuqq.setof.application.productdescription",
            "com.ryuqq.setof.application.productgroup",
            "com.ryuqq.setof.application.productgroupimage",
            "com.ryuqq.setof.application.productnotice",
            "com.ryuqq.setof.application.refundpolicy",
            "com.ryuqq.setof.application.seller",
            "com.ryuqq.setof.application.selleroption",
            "com.ryuqq.setof.application.imagevariant",
            "com.ryuqq.setof.application.shippingpolicy",
            "com.ryuqq.setof.application.banner",
            "com.ryuqq.setof.application.navigation",
            "com.ryuqq.setof.application.contentpage",
            "com.ryuqq.setof.application.discount",
            "com.ryuqq.setof.adapter.in.rest.admin",
            "com.ryuqq.setof.adapter.out.persistence",
            "com.ryuqq.setof.adapter.out.security",
            "com.ryuqq.setof.adapter.out.client",
        },
        excludeFilters = {
            @ComponentScan.Filter(
                    type = FilterType.REGEX,
                    pattern = "com\\.ryuqq\\.setof\\.storage\\.legacy\\..*"),
            @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)
        })
@EnableJpaRepositories(basePackages = "com.ryuqq.setof.adapter.out.persistence")
@EntityScan(basePackages = "com.ryuqq.setof.adapter.out.persistence")
public class TestAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestAdminApplication.class, args);
    }
}
