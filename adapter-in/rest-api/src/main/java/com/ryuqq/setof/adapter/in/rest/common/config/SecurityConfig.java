package com.ryuqq.setof.adapter.in.rest.common.config;

import com.ryuqq.setof.adapter.in.rest.common.auth.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Value("${security.jwt.secret:}")
    private String jwtSecret;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(
                        authz ->
                                authz.requestMatchers(
                                                "/actuator/**",
                                                "/docs/**",
                                                "/v3/api-docs/**",
                                                "/swagger-ui/**",
                                                "/api/v1/brand/**",
                                                "/api/v1/brands/**",
                                                "/api/v1/category/**",
                                                "/api/v1/categories/**",
                                                "/api/v1/products/**",
                                                "/api/v1/displays/**",
                                                "/api/v1/sellers/**",
                                                "/api/v1/reviews/**",
                                                "/api/v1/faq/**",
                                                "/api/v2/auth/**",
                                                "/api/v2/health",
                                                "/oauth2/**",
                                                "/login/oauth2/**")
                                        .permitAll()
                                        .requestMatchers("/api/v1/user/**", "/api/v2/members/**")
                                        .authenticated()
                                        .anyRequest()
                                        .permitAll())
                .exceptionHandling(ex -> ex.authenticationEntryPoint(unauthorizedEntryPoint()))
                .addFilterBefore(
                        jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtSecret);
    }

    @Bean
    public AuthenticationEntryPoint unauthorizedEntryPoint() {
        return (request, response, authException) -> {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            response.getWriter()
                    .write("{\"status\":401,\"message\":\"인증이 필요합니다. 유효한 토큰을 제공해주세요.\"}");
        };
    }
}
