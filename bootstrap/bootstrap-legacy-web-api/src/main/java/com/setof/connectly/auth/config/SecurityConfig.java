package com.setof.connectly.auth.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.setof.connectly.auth.filter.TokenAuthenticationExceptionFilter;
import com.setof.connectly.auth.filter.TokenAuthenticationFilter;
import com.setof.connectly.auth.handler.OAuth2AuthenticationFailureHandler;
import com.setof.connectly.auth.handler.OAuth2AuthenticationSuccessHandler;
import com.setof.connectly.auth.repository.CustomAuthorizationRequestRepository;
import com.setof.connectly.auth.repository.CustomAuthorizedClientRepository;
import com.setof.connectly.auth.service.CustomOAuth2Service;
import com.setof.connectly.auth.token.AuthTokenProvider;
import com.setof.connectly.module.user.mapper.UserMapper;
import com.setof.connectly.module.user.service.fetch.UserFindServiceImpl;
import com.setof.connectly.module.user.service.query.UserQueryServiceImpl;
import com.setof.connectly.module.user.service.token.RefreshTokenRedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final CustomOAuth2Service customOAuth2Service;
    private final CustomAuthorizedClientRepository customAuthorizedClientRepository;
    private final CustomAuthorizationRequestRepository customAuthorizationRequestRepository;
    private final UserQueryServiceImpl userQueryService;
    private final UserFindServiceImpl userFindService;
    private final AuthTokenProvider tokenProvider;
    private final RefreshTokenRedisService refreshTokenRedisQueryService;
    private final UserMapper userMapper;
    private final ObjectMapper objectMapper;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(
                        authz ->
                                authz.requestMatchers(
                                                "/api/v1/oauth2/authorization/**",
                                                "/api/v1/login/oauth2/code/**")
                                        .permitAll()
                                        .requestMatchers("/api/v1/**", "/actuator/**")
                                        .permitAll()
                                        .anyRequest()
                                        .authenticated())
                .formLogin(AbstractHttpConfigurer::disable)
                .cors(cors -> {})
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(
                        exceptions ->
                                exceptions
                                        .defaultAuthenticationEntryPointFor(
                                                (request, response, authException) -> {
                                                    // OAuth2 경로는 리다이렉트로 처리
                                                    response.sendRedirect("/api/v1/oauth2/authorization/kakao");
                                                },
                                                new org.springframework.security.web.util.matcher.AntPathRequestMatcher("/api/v1/login/oauth2/code/**"))
                                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                                        .accessDeniedHandler(jwtAccessDeniedHandler))
                .oauth2Login(
                        oauth2 ->
                                oauth2.authorizedClientRepository(customAuthorizedClientRepository)
                                        .authorizationEndpoint(
                                                authorization ->
                                                        authorization
                                                                .baseUri("/api/v1/oauth2/authorization")
                                                                .authorizationRequestRepository(
                                                                        customAuthorizationRequestRepository))
                                        .redirectionEndpoint(
                                                redirection ->
                                                        redirection.baseUri("/api/v1/login/oauth2/code/*"))
                                        .userInfoEndpoint(
                                                userInfoEndpointConfig ->
                                                        userInfoEndpointConfig.userService(
                                                                customOAuth2Service))
                                        .successHandler(oAuth2AuthenticationSuccessHandler())
                                        .failureHandler(oAuth2AuthenticationFailureHandler()))
                .addFilterBefore(
                        new TokenAuthenticationFilter(
                                tokenProvider, userFindService, refreshTokenRedisQueryService),
                        UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(
                        new TokenAuthenticationExceptionFilter(objectMapper),
                        TokenAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler() {
        return new OAuth2AuthenticationSuccessHandler(
                customAuthorizedClientRepository,
                customAuthorizationRequestRepository,
                userQueryService,
                userFindService,
                tokenProvider,
                userMapper);
    }

    @Bean
    OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler() {
        return new OAuth2AuthenticationFailureHandler(
                customAuthorizedClientRepository, customAuthorizationRequestRepository);
    }

    @Bean
    public BCryptPasswordEncoder encodePassword() {
        return new BCryptPasswordEncoder();
    }

    // CustomLoggingFilter 제거됨 - SDK(observability-adapter)가 HTTP 로깅 담당
    // UserContextFilter(@Component)가 사용자 컨텍스트 MDC 추가 담당
}
