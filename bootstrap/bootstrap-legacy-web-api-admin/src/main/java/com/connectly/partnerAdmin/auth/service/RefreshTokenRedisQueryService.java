package com.connectly.partnerAdmin.auth.service;

import com.connectly.partnerAdmin.auth.core.AuthToken;
import com.connectly.partnerAdmin.module.common.enums.RedisKey;
import com.connectly.partnerAdmin.module.common.service.AbstractRedisQueryService;
import com.connectly.partnerAdmin.module.utils.JsonUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RefreshTokenRedisQueryService extends AbstractRedisQueryService implements RefreshTokenQueryService {

    public RefreshTokenRedisQueryService(StringRedisTemplate redisTemplate) {
        super(redisTemplate);
    }

    @Override
    public void saveToken(AuthToken token) {
        String key = generateKey(RedisKey.REFRESH_TOKEN, token.getSubject());
        String value = JsonUtils.toJson(token);
        save(key, value, RedisKey.REFRESH_TOKEN.getHourDuration());
    }

}
