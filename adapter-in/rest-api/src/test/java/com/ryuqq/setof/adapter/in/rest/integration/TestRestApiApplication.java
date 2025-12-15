package com.ryuqq.setof.adapter.in.rest.integration;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

/**
 * REST API 통합 테스트용 Spring Boot Application
 *
 * <p>rest-api 모듈의 통합 테스트에서 사용됩니다. Bootstrap 모듈 없이 단독으로 테스트 컨텍스트를 실행합니다.
 *
 * <p>JPA 설정은 persistence-mysql 모듈의 JpaConfig에서 자동 구성됩니다.
 *
 * <p>테스트에서는 SecurityConfig와 OAuth 관련 빈을 제외하고, TestSecurityConfig를 대신 사용합니다.
 *
 * @author Development Team
 * @since 1.0.0
 */
@SpringBootApplication
@ComponentScan(
        basePackages = {
            // Domain
            "com.ryuqq.setof.domain",
            // Application
            "com.ryuqq.setof.application.brand",
            "com.ryuqq.setof.application.category",
            "com.ryuqq.setof.application.bank",
            "com.ryuqq.setof.application.shippingaddress",
            "com.ryuqq.setof.application.refundaccount",
            "com.ryuqq.setof.application.member",
            "com.ryuqq.setof.application.auth",
            "com.ryuqq.setof.application.common",
            "com.ryuqq.setof.application.product",
            "com.ryuqq.setof.application.productimage",
            "com.ryuqq.setof.application.productdescription",
            "com.ryuqq.setof.application.productnotice",
            "com.ryuqq.setof.application.productstock",
            // REST API - V2 Controllers
            "com.ryuqq.setof.adapter.in.rest.v2.brand.controller",
            "com.ryuqq.setof.adapter.in.rest.v2.brand.mapper",
            "com.ryuqq.setof.adapter.in.rest.v2.brand.error",
            "com.ryuqq.setof.adapter.in.rest.v2.category",
            "com.ryuqq.setof.adapter.in.rest.v2.bank",
            "com.ryuqq.setof.adapter.in.rest.v2.shippingaddress",
            "com.ryuqq.setof.adapter.in.rest.v2.refundaccount",
            "com.ryuqq.setof.adapter.in.rest.v2.member",
            "com.ryuqq.setof.adapter.in.rest.v2.product",
            "com.ryuqq.setof.adapter.in.rest.common",
            // REST API - Auth (controller, dto, mapper, filter, handler, etc.)
            "com.ryuqq.setof.adapter.in.rest.auth.controller",
            "com.ryuqq.setof.adapter.in.rest.auth.dto",
            "com.ryuqq.setof.adapter.in.rest.auth.mapper",
            "com.ryuqq.setof.adapter.in.rest.auth.filter",
            "com.ryuqq.setof.adapter.in.rest.auth.handler",
            "com.ryuqq.setof.adapter.in.rest.auth.component",
            "com.ryuqq.setof.adapter.in.rest.auth.security",
            "com.ryuqq.setof.adapter.in.rest.auth.utils",
            "com.ryuqq.setof.adapter.in.rest.auth.repository",
            // Test Stub Adapters
            "com.ryuqq.setof.adapter.in.rest.integration.stub",
            // Persistence
            "com.ryuqq.setof.adapter.out.persistence.brand",
            "com.ryuqq.setof.adapter.out.persistence.category",
            "com.ryuqq.setof.adapter.out.persistence.bank",
            "com.ryuqq.setof.adapter.out.persistence.shippingaddress",
            "com.ryuqq.setof.adapter.out.persistence.refundaccount",
            "com.ryuqq.setof.adapter.out.persistence.member",
            "com.ryuqq.setof.adapter.out.persistence.refreshtoken",
            "com.ryuqq.setof.adapter.out.persistence.config",
            "com.ryuqq.setof.adapter.out.persistence.product",
            "com.ryuqq.setof.adapter.out.persistence.productimage",
            "com.ryuqq.setof.adapter.out.persistence.productdescription",
            "com.ryuqq.setof.adapter.out.persistence.productnotice",
            "com.ryuqq.setof.adapter.out.persistence.productstock",
            // Security (Password Encoder, JWT, Config)
            "com.ryuqq.setof.adapter.out.security",
            "com.ryuqq.setof.adapter.out.security.config"
        },
        excludeFilters = {
            // rest-api auth config의 SecurityConfig만 제외 (TestSecurityConfig로 대체)
            // SecurityAdapterConfig는 포함해야 함 (JwtProperties 활성화용)
            @ComponentScan.Filter(
                    type = FilterType.REGEX,
                    pattern = ".*\\.rest\\.auth\\.config\\.SecurityConfig"),
            @ComponentScan.Filter(
                    type = FilterType.REGEX,
                    pattern = ".*OAuth2.*") // OAuth2 클라이언트 빈만 제외 (KakaoOAuth* 비즈니스 로직은 포함)
        })
public class TestRestApiApplication {}
