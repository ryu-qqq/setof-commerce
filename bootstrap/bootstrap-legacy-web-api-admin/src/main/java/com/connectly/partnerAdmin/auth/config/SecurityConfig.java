package com.connectly.partnerAdmin.auth.config;

import com.connectly.partnerAdmin.auth.filter.CustomLoggingFilter;
import com.connectly.partnerAdmin.auth.filter.JwtAuthenticationFilter;
import com.connectly.partnerAdmin.auth.handler.JwtAccessDeniedHandler;
import com.connectly.partnerAdmin.auth.handler.JwtAuthorizationDeniedHandler;
import com.connectly.partnerAdmin.auth.provider.AuthTokenProvider;
import com.connectly.partnerAdmin.auth.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.security.config.Customizer;
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
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthTokenProvider authTokenProvider;
    private final JwtAuthorizationDeniedHandler jwtAuthorizationDeniedHandler;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authTokenProvider);

        http
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/api/v1/**").permitAll()
                        .requestMatchers("/test/**").permitAll()
                        .requestMatchers("/static/**").permitAll()
                        .requestMatchers("/health").permitAll()
                        .requestMatchers("/actuator/**").permitAll()
                        .requestMatchers("/docs/**").permitAll()  // /docs 및 하위 경로 모두 허용
                        .requestMatchers("/common/**").permitAll()  // /common 및 하위 경로 모두 허용
                )
                .formLogin(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(jwtAuthorizationDeniedHandler)
                        .accessDeniedHandler(jwtAccessDeniedHandler)
                )
                .userDetailsService(customUserDetailsService);

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
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
        registration.setEnabled(false);
        return registration;
    }

}
