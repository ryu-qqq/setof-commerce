package com.setof.connectly.auth.config;

import com.setof.connectly.auth.mapper.TokenMapper;
import com.setof.connectly.auth.token.AuthTokenProvider;
import com.setof.connectly.module.user.service.token.RefreshTokenRedisService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {
    @Value(value = "${token.secret}")
    private String secret;

    @Bean
    public AuthTokenProvider jwtProvider(
            RefreshTokenRedisService refreshTokenRedisQueryService, TokenMapper tokenMapper) {
        return new AuthTokenProvider(secret, refreshTokenRedisQueryService, tokenMapper);
    }
}
