package com.ryuqq.setof.adapter.in.rest.admin.common;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Admin API 테스트용 Security 설정
 *
 * <p>통합 테스트에서 Admin 인증 흐름을 지원합니다.
 *
 * <p><strong>동작 방식:</strong>
 *
 * <ul>
 *   <li>모든 요청 허용 (permitAll) - 테스트 환경에서 인증 우회
 *   <li>실제 Admin 인증은 별도의 Filter로 처리될 예정
 *   <li>인증 없이 호출 → 공개 API 테스트
 *   <li>인증 헤더와 함께 호출 → Admin API 테스트
 * </ul>
 *
 * @author Development Team
 * @since 1.0.0
 * @see TestMockBeanConfig
 */
@TestConfiguration
public class TestSecurityConfig {

    @Bean
    @Primary
    @Order(1)
    public SecurityFilterChain testSecurityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(
                        auth ->
                                auth
                                        // 테스트 환경에서는 모든 요청 허용
                                        .anyRequest()
                                        .permitAll());
        return http.build();
    }
}
