package com.ryuqq.setof.adapter.in.rest.admin.integration;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

/**
 * REST API Admin 통합 테스트용 Spring Boot Application
 *
 * <p>rest-api-admin 모듈의 통합 테스트에서 사용됩니다. Bootstrap 모듈 없이 단독으로 테스트 컨텍스트를 실행합니다.
 *
 * <p>JPA 설정은 persistence-mysql 모듈의 JpaConfig에서 자동 구성됩니다.
 *
 * @author Development Team
 * @since 1.0.0
 */
@SpringBootApplication
@ComponentScan(
        basePackages = {
            // Domain
            "com.ryuqq.setof.domain",
            // Application - Seller, ShippingPolicy, RefundPolicy, ProductStock, Product 관련
            "com.ryuqq.setof.application.seller",
            "com.ryuqq.setof.application.shippingpolicy",
            "com.ryuqq.setof.application.refundpolicy",
            "com.ryuqq.setof.application.productstock",
            "com.ryuqq.setof.application.product",
            "com.ryuqq.setof.application.productdescription",
            "com.ryuqq.setof.application.productimage",
            "com.ryuqq.setof.application.productnotice",
            "com.ryuqq.setof.application.common",
            // REST API Admin - V2 Controllers
            "com.ryuqq.setof.adapter.in.rest.admin.v2.seller",
            "com.ryuqq.setof.adapter.in.rest.admin.v2.shippingpolicy",
            "com.ryuqq.setof.adapter.in.rest.admin.v2.refundpolicy",
            "com.ryuqq.setof.adapter.in.rest.admin.v2.productstock",
            "com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup",
            "com.ryuqq.setof.adapter.in.rest.admin.v2.productdescription",
            "com.ryuqq.setof.adapter.in.rest.admin.v2.productimage",
            "com.ryuqq.setof.adapter.in.rest.admin.v2.productnotice",
            "com.ryuqq.setof.adapter.in.rest.admin.common",
            // Persistence - Seller, ShippingPolicy, RefundPolicy, ProductStock, Product 관련
            "com.ryuqq.setof.adapter.out.persistence.seller",
            "com.ryuqq.setof.adapter.out.persistence.shippingpolicy",
            "com.ryuqq.setof.adapter.out.persistence.refundpolicy",
            "com.ryuqq.setof.adapter.out.persistence.productstock",
            "com.ryuqq.setof.adapter.out.persistence.product",
            "com.ryuqq.setof.adapter.out.persistence.productdescription",
            "com.ryuqq.setof.adapter.out.persistence.productimage",
            "com.ryuqq.setof.adapter.out.persistence.productnotice",
            "com.ryuqq.setof.adapter.out.persistence.config",
        },
        excludeFilters = {
            // Security 관련 설정 제외 (TestSecurityConfig로 대체)
            @ComponentScan.Filter(type = FilterType.REGEX, pattern = ".*\\.security\\.config\\..*")
        })
public class TestRestApiAdminApplication {}
