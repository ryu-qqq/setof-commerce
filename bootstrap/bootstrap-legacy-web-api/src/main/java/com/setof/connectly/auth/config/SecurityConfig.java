package com.setof.connectly.auth.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.setof.connectly.auth.filter.CustomLoggingFilter;
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
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
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
                                authz.requestMatchers("/api/v1/**", "/actuator/**")
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
                                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                                        .accessDeniedHandler(jwtAccessDeniedHandler))
                .oauth2Login(
                        oauth2 ->
                                oauth2.authorizedClientRepository(customAuthorizedClientRepository)
                                        .authorizationEndpoint(
                                                authorization ->
                                                        authorization
                                                                .authorizationRequestRepository(
                                                                        customAuthorizationRequestRepository))
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

    @Bean
    public FilterRegistrationBean<CustomLoggingFilter> loggingFilterRegistration() {
        FilterRegistrationBean<CustomLoggingFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new CustomLoggingFilter());
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registration;
    }
}
