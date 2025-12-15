package com.setof.connectly.module.common.service;

import com.setof.connectly.module.common.enums.RedisKey;
import java.time.Duration;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public abstract class AbstractRedisService
        implements RedisFetchService, RedisKeyGenerator, RedisQueryService {

    private static final String redisConnectionFailMessage = "Redis connection failure";
    private static final String errorAccessRedisMessage = "Error accessing Redis data";
    private final StringRedisTemplate redisTemplate;

    @Override
    public String generateKey(RedisKey redisKey, String key) {
        return redisKey.generateKey(key);
    }

    @Override
    public String getValue(String key) {
        try {
            return redisTemplate.opsForValue().get(key);
        } catch (RedisConnectionFailureException e) {
            throw new ServiceException(redisConnectionFailMessage, e);
        } catch (DataAccessException e) {
            throw new ServiceException(errorAccessRedisMessage, e);
        }
    }

    @Override
    public List<String> getValues(List<String> keys) {
        try {
            return redisTemplate.opsForValue().multiGet(keys);
        } catch (RedisConnectionFailureException e) {
            throw new ServiceException(redisConnectionFailMessage, e);
        } catch (DataAccessException e) {
            throw new ServiceException(errorAccessRedisMessage, e);
        }
    }

    @Override
    public ValueOperations<String, String> opsValue() {
        return redisTemplate.opsForValue();
    }

    public void save(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void save(String key, String value, Duration ttl) {
        redisTemplate.opsForValue().set(key, value, ttl);
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }
}
