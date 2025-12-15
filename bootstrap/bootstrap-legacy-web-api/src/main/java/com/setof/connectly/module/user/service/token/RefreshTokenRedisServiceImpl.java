package com.setof.connectly.module.user.service.token;

import com.setof.connectly.module.common.enums.RedisKey;
import com.setof.connectly.module.common.service.AbstractRedisService;
import com.setof.connectly.module.user.redis.RefreshToken;
import com.setof.connectly.module.utils.JsonUtils;
import java.time.Duration;
import java.util.Optional;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class RefreshTokenRedisServiceImpl extends AbstractRedisService
        implements RefreshTokenRedisService {

    public RefreshTokenRedisServiceImpl(StringRedisTemplate redisTemplate) {
        super(redisTemplate);
    }

    @Override
    public void saveRefreshToken(RefreshToken refreshToken) {
        String key = generateKey(RedisKey.REFRESH_TOKEN, refreshToken.getId());
        String value = JsonUtils.toJson(refreshToken);
        Duration ttl = Duration.ofSeconds(refreshToken.getExpiration());
        save(key, value, ttl);
    }

    @Override
    public Optional<RefreshToken> findByUserId(String userId) {
        String key = generateKey(RedisKey.REFRESH_TOKEN, userId);
        String value = getValue(key);
        if (StringUtils.hasText(value)) return Optional.of(parseRefreshToken(value));
        return Optional.empty();
    }

    private RefreshToken parseRefreshToken(String value) {
        return JsonUtils.fromJson(value, RefreshToken.class);
    }
}
