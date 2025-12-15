package com.setof.connectly.auth.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.setof.connectly.auth.filter.TokenAuthenticationExceptionFilter;
import com.setof.connectly.auth.filter.TokenAuthenticationFilter;
import com.setof.connectly.auth.token.AuthTokenProvider;
import com.setof.connectly.module.user.service.fetch.UserFindServiceImpl;
import com.setof.connectly.module.user.service.token.RefreshTokenRedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
public class TokenSecurityConfig
        extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private final UserFindServiceImpl userFindService;
    private final AuthTokenProvider tokenProvider;
    private final RefreshTokenRedisService refreshTokenRedisQueryService;

    private final ObjectMapper objectMapper;

    @Override
    public void configure(HttpSecurity http) {
        TokenAuthenticationFilter tokenAuthenticationFilter =
                new TokenAuthenticationFilter(
                        tokenProvider, userFindService, refreshTokenRedisQueryService);
        TokenAuthenticationExceptionFilter exceptionHandlerFilter =
                new TokenAuthenticationExceptionFilter(objectMapper);

        http.addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(exceptionHandlerFilter, TokenAuthenticationFilter.class);
    }
}
