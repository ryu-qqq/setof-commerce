package com.ryuqq.setof.adapter.in.rest.common.config;

import com.ryuqq.setof.adapter.in.rest.common.auth.JwtAuthenticationFilter;
import com.ryuqq.setof.adapter.in.rest.common.oauth2.handler.OAuth2AuthenticationFailureHandler;
import com.ryuqq.setof.adapter.in.rest.common.oauth2.handler.OAuth2AuthenticationSuccessHandler;
import com.ryuqq.setof.adapter.in.rest.common.oauth2.repository.OAuth2AuthorizationRequestCookieRepository;
import com.ryuqq.setof.adapter.in.rest.common.oauth2.service.CustomOAuth2UserService;
import com.ryuqq.setof.adapter.in.rest.common.util.CookieUtils;
import com.ryuqq.setof.application.auth.port.out.cache.RefreshTokenCacheQueryPort;
import com.ryuqq.setof.application.auth.port.out.client.TokenProviderPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
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
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@EnableConfigurationProperties(CookieProperties.class)
public class SecurityConfig {

    @Value("${security.jwt.secret:}")
    private String jwtSecret;

    private final CookieUtils cookieUtils;
    private final TokenProviderPort tokenProviderPort;
    private final RefreshTokenCacheQueryPort refreshTokenCacheQueryPort;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2AuthenticationSuccessHandler oAuth2SuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2FailureHandler;
    private final OAuth2AuthorizationRequestCookieRepository oAuth2AuthorizationRequestRepository;

    public SecurityConfig(
            CookieUtils cookieUtils,
            TokenProviderPort tokenProviderPort,
            RefreshTokenCacheQueryPort refreshTokenCacheQueryPort,
            CustomOAuth2UserService customOAuth2UserService,
            OAuth2AuthenticationSuccessHandler oAuth2SuccessHandler,
            OAuth2AuthenticationFailureHandler oAuth2FailureHandler,
            OAuth2AuthorizationRequestCookieRepository oAuth2AuthorizationRequestRepository) {
        this.cookieUtils = cookieUtils;
        this.tokenProviderPort = tokenProviderPort;
        this.refreshTokenCacheQueryPort = refreshTokenCacheQueryPort;
        this.customOAuth2UserService = customOAuth2UserService;
        this.oAuth2SuccessHandler = oAuth2SuccessHandler;
        this.oAuth2FailureHandler = oAuth2FailureHandler;
        this.oAuth2AuthorizationRequestRepository = oAuth2AuthorizationRequestRepository;
    }

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
                                                // 인프라
                                                "/actuator/**",
                                                "/docs/**",
                                                "/v3/api-docs/**",
                                                "/swagger-ui/**",
                                                // v1 공개 API (비회원 접근)
                                                "/api/v1/brand/**",
                                                "/api/v1/brands/**",
                                                "/api/v1/category/**",
                                                "/api/v1/categories/**",
                                                "/api/v1/products/**",
                                                "/api/v1/displays/**",
                                                "/api/v1/sellers/**",
                                                "/api/v1/reviews/**",
                                                "/api/v1/faq/**",
                                                "/api/v1/qna/product/**",
                                                // v1 Auth 공개 엔드포인트 (레거시 호환)
                                                "/api/v1/user/join",
                                                "/api/v1/user/login",
                                                "/api/v1/user/exists",
                                                "/api/v1/user/password",
                                                // v2 Auth
                                                "/api/v2/auth/**",
                                                "/api/v2/health",
                                                // OAuth2
                                                "/oauth2/**",
                                                "/login/oauth2/**",
                                                "/api/v1/oauth2/**",
                                                "/api/v1/login/oauth2/**",
                                                // 리다이렉트
                                                "/redirect")
                                        .permitAll()
                                        .requestMatchers("/api/v1/user/**", "/api/v2/members/**")
                                        .authenticated()
                                        .anyRequest()
                                        .permitAll())
                .oauth2Login(
                        oauth2 ->
                                oauth2.authorizationEndpoint(
                                                authorization ->
                                                        authorization
                                                                .baseUri(
                                                                        "/api/v1/oauth2/authorization")
                                                                .authorizationRequestRepository(
                                                                        oAuth2AuthorizationRequestRepository))
                                        .redirectionEndpoint(
                                                redirection ->
                                                        redirection.baseUri(
                                                                "/api/v1/login/oauth2/code/*"))
                                        .userInfoEndpoint(
                                                userInfo ->
                                                        userInfo.userService(
                                                                customOAuth2UserService))
                                        .successHandler(oAuth2SuccessHandler)
                                        .failureHandler(oAuth2FailureHandler))
                .exceptionHandling(
                        ex ->
                                ex.authenticationEntryPoint(unauthorizedEntryPoint())
                                        .accessDeniedHandler(accessDeniedHandler()))
                .addFilterBefore(
                        jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(
                jwtSecret, cookieUtils, tokenProviderPort, refreshTokenCacheQueryPort);
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

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"status\":403,\"message\":\"접근 권한이 없습니다.\"}");
        };
    }
}
