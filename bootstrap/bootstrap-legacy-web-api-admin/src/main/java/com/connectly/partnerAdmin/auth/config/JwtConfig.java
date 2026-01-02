package com.connectly.partnerAdmin.auth.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class JwtConfig {

    @Value(value = "${token.secret}")
    private String secret;

    @Value("${token.ACCESS_TOKEN_EXPIRE_TIME}")
    private long expirationTime;

    @Value("${token.REFRESH_TOKEN_EXPIRE_TIME}")
    private long refreshExpirationTime;

    @Value("${token.ADMIN_EMAIL}")
    private String administratorEmail;


    public long getExpirationTime(boolean isRefreshToken) {
        if(isRefreshToken) return refreshExpirationTime;
        return expirationTime;
    }



}
