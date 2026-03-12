package com.ryuqq.setof.admin.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ryuqq.setof.adapter.in.rest.admin.common.auth.ServiceTokenAuthenticationFilter;
import com.ryuqq.setof.adapter.in.rest.admin.common.auth.ServiceTokenProperties;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Admin API Security Configuration.
 *
 * <p>Service Token 기반 인증을 적용합니다.
 *
 * <ul>
 *   <li>Service Token 인증 (X-Service-Name + X-Service-Token)
 *   <li>Stateless 세션 (서버 간 통신이므로 세션 불필요)
 *   <li>CSRF 비활성화 (브라우저 접근 없음)
 * </ul>
 *
 * <p>401/403 응답은 RFC 7807 ProblemDetail 형식으로 GlobalExceptionHandler와 일관됩니다.
 *
 * @since 1.0.0
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@EnableConfigurationProperties(ServiceTokenProperties.class)
public class SecurityConfig {

    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

    private final ObjectMapper objectMapper;

    public SecurityConfig(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Bean
    public ServiceTokenAuthenticationFilter serviceTokenAuthenticationFilter(
            ServiceTokenProperties properties) {
        return new ServiceTokenAuthenticationFilter(properties);
    }

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http, ServiceTokenAuthenticationFilter serviceTokenFilter)
            throws Exception {

        http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .addFilterBefore(serviceTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(
                        authz ->
                                authz
                                        // Actuator endpoints
                                        .requestMatchers("/actuator/**")
                                        .permitAll()
                                        // Swagger UI
                                        .requestMatchers("/api/v2/swagger-ui/**")
                                        .permitAll()
                                        .requestMatchers("/api/v2/swagger-ui.html")
                                        .permitAll()
                                        .requestMatchers("/api/v2/api-docs/**")
                                        .permitAll()
                                        // REST Docs
                                        .requestMatchers("/api/v2/docs/**")
                                        .permitAll()
                                        // Static resources
                                        .requestMatchers("/static/**")
                                        .permitAll()
                                        // All other requests require authentication
                                        .anyRequest()
                                        .authenticated())
                .exceptionHandling(
                        ex ->
                                ex.authenticationEntryPoint(this::handleAuthenticationError)
                                        .accessDeniedHandler(this::handleAccessDenied));

        return http.build();
    }

    private void handleAuthenticationError(
            HttpServletRequest request, HttpServletResponse response, AuthenticationException ex)
            throws IOException {

        log.warn("Unauthorized: uri={}, reason={}", request.getRequestURI(), ex.getMessage());
        writeProblemDetail(
                response,
                HttpStatus.UNAUTHORIZED,
                "Unauthorized",
                "Service token is missing or invalid",
                "SERVICE_TOKEN_REQUIRED",
                request);
    }

    private void handleAccessDenied(
            HttpServletRequest request, HttpServletResponse response, AccessDeniedException ex)
            throws IOException {

        log.warn("AccessDenied: uri={}, reason={}", request.getRequestURI(), ex.getMessage());
        writeProblemDetail(
                response,
                HttpStatus.FORBIDDEN,
                "Forbidden",
                "접근 권한이 없습니다",
                "ACCESS_DENIED",
                request);
    }

    private void writeProblemDetail(
            HttpServletResponse response,
            HttpStatus status,
            String title,
            String detail,
            String code,
            HttpServletRequest request)
            throws IOException {

        ProblemDetail pd = ProblemDetail.forStatusAndDetail(status, detail);
        pd.setTitle(title);
        pd.setType(URI.create("about:blank"));
        pd.setInstance(URI.create(request.getRequestURI()));
        pd.setProperty("code", code);
        pd.setProperty("timestamp", Instant.now().toString());

        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE);
        response.setHeader("x-error-code", code);
        objectMapper.writeValue(response.getOutputStream(), pd);
    }
}
