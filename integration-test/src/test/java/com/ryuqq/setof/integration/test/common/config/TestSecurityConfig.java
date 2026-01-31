package com.ryuqq.setof.integration.test.common.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * 통합 테스트용 Security 설정.
 *
 * <p>테스트 환경에서 모든 엔드포인트에 대한 접근을 허용합니다. 실제 운영 환경에서는 적절한 인증/인가가 적용되어야 합니다.
 */
@TestConfiguration
@Profile("test")
public class TestSecurityConfig {

    /**
     * 테스트용 SecurityFilterChain.
     *
     * <p>모든 요청을 허용하고 CSRF를 비활성화합니다.
     *
     * @param http HttpSecurity 설정
     * @return SecurityFilterChain
     * @throws Exception 설정 오류 시
     */
    @Bean
    @Order(1)
    public SecurityFilterChain testSecurityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .build();
    }
}
