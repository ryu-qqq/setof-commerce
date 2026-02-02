package com.ryuqq.setof.admin.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Admin API Security Configuration.
 *
 * <p>Admin API의 보안 설정을 정의합니다.
 *
 * <p><strong>공개 엔드포인트:</strong>
 *
 * <ul>
 *   <li>Actuator: /actuator/**
 *   <li>Swagger UI: /api/v2/swagger-ui/**, /api/v2/swagger-ui.html, /api/v2/api-docs/**
 *   <li>REST Docs: /api/v2/docs/**
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(
                        authz ->
                                authz
                                        // Actuator endpoints
                                        .requestMatchers("/actuator/**")
                                        .permitAll()
                                        // Swagger UI (/api/v2 prefix - 컨트롤러와 동일)
                                        .requestMatchers("/api/v2/swagger-ui/**")
                                        .permitAll()
                                        .requestMatchers("/api/v2/swagger-ui.html")
                                        .permitAll()
                                        .requestMatchers("/api/v2/api-docs/**")
                                        .permitAll()
                                        // REST Docs (/api/v2 prefix)
                                        .requestMatchers("/api/v2/docs/**")
                                        .permitAll()
                                        // Static resources
                                        .requestMatchers("/static/**")
                                        .permitAll()
                                        // All other requests require authentication
                                        .anyRequest()
                                        .permitAll() // TODO: 인증 구현 후 authenticated()로 변경
                        )
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}
