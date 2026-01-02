package com.connectly.partnerAdmin.auth.service;

import com.connectly.partnerAdmin.auth.core.AuthToken;
import com.connectly.partnerAdmin.auth.core.JwtAuthToken;
import com.connectly.partnerAdmin.module.common.enums.RedisKey;
import com.connectly.partnerAdmin.module.common.service.AbstractRedisFetchService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;


@Service
public class RefreshTokenRedisFetchService extends AbstractRedisFetchService implements RefreshTokenFetchService{

    public RefreshTokenRedisFetchService(StringRedisTemplate redisTemplate) {
        super(redisTemplate);
    }

    @Override
    public Optional<AuthToken> fetchRefreshToken(String email) {
        String key = generateKey(RedisKey.REFRESH_TOKEN, email);
        String value = getValue(key);
        if(StringUtils.hasText(value)) {
            Optional<JwtAuthToken> jwtAuthTokenOptional = parseJson(value, JwtAuthToken.class);
            return jwtAuthTokenOptional.map(jwtAuthToken -> (AuthToken) jwtAuthToken);
        }
        return Optional.empty();
    }


}
