package com.connectly.partnerAdmin.module.common.service;


import com.connectly.partnerAdmin.module.common.enums.RedisKey;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public abstract class AbstractRedisService implements RedisKeyGenerator{

    private final StringRedisTemplate redisTemplate;

    protected final static String redisConnectionFailMessage = "Redis connection failure";
    protected final static String errorAccessRedisMessage = "Error accessing Redis data";

    protected StringRedisTemplate getRedisTemplate() {
        return redisTemplate;
    }

    protected ValueOperations<String, String> opsValue() {
        return redisTemplate.opsForValue();
    }

    @Override
    public String generateKey(RedisKey redisKey, String key) {
        return redisKey.generateKey(key);
    }

}
