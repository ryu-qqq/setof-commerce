package com.ryuqq.setof.adapter.in.rest.common;

import com.ryuqq.setof.adapter.in.rest.auth.component.MdcContextHolder;
import com.ryuqq.setof.adapter.in.rest.auth.component.SecurityContextAuthenticator;
import com.ryuqq.setof.adapter.in.rest.auth.component.TokenCookieWriter;
import com.ryuqq.setof.adapter.in.rest.auth.config.SecurityProperties;
import com.ryuqq.setof.adapter.in.rest.auth.filter.JwtAuthenticationFilter;
import com.ryuqq.setof.adapter.in.rest.auth.paths.ApiV2Paths;
import com.ryuqq.setof.application.auth.port.out.client.TokenProviderPort;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 테스트용 Security 설정
 *
 * <p>통합 테스트에서 JWT 인증 흐름을 지원합니다.
 *
 * <p><strong>동작 방식:</strong>
 *
 * <ul>
 *   <li>모든 요청 허용 (permitAll) - 401 에러 방지
 *   <li>JwtAuthenticationFilter 포함 - 토큰이 있으면 SecurityContext에 Principal 설정
 *   <li>인증 없이 호출 → Principal null (공개 API 테스트)
 *   <li>인증 토큰과 함께 호출 → Principal 설정됨 (인증 필요 API 테스트)
 * </ul>
 *
 * @author Development Team
 * @since 1.0.0
 * @see TestMockBeanConfig
 */
@TestConfiguration
@EnableWebSecurity
public class TestSecurityConfig {

    private final TokenProviderPort tokenProviderPort;

    public TestSecurityConfig(TokenProviderPort tokenProviderPort) {
        this.tokenProviderPort = tokenProviderPort;
    }

    @Bean
    @Primary
    @Order(1)
    public SecurityFilterChain testSecurityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .anonymous(AbstractHttpConfigurer::disable)
                .sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(
                        auth ->
                                auth
                                        // 인증 필수 엔드포인트
                                        .requestMatchers(ApiV2Paths.Members.ME)
                                        .authenticated()
                                        .requestMatchers(ApiV2Paths.Auth.LOGOUT)
                                        .authenticated()
                                        // 나머지는 허용
                                        .anyRequest()
                                        .permitAll())
                .exceptionHandling(
                        ex ->
                                ex.authenticationEntryPoint(
                                        new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .addFilterBefore(
                        testJwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public JwtAuthenticationFilter testJwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(
                tokenProviderPort,
                testTokenCookieWriter(),
                testSecurityContextAuthenticator(),
                testMdcContextHolder());
    }

    @Bean
    public SecurityProperties testSecurityProperties() {
        // 테스트용 기본 SecurityProperties (localhost, secure=false)
        return new SecurityProperties();
    }

    @Bean
    public TokenCookieWriter testTokenCookieWriter() {
        return new TokenCookieWriter(testSecurityProperties());
    }

    @Bean
    public SecurityContextAuthenticator testSecurityContextAuthenticator() {
        return new SecurityContextAuthenticator(tokenProviderPort);
    }

    @Bean
    public MdcContextHolder testMdcContextHolder() {
        return new MdcContextHolder();
    }
}
