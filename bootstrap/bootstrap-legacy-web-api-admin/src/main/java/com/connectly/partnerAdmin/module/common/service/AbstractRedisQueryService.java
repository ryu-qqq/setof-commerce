package com.connectly.partnerAdmin.module.common.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class AbstractRedisQueryService extends AbstractRedisService implements RedisQueryService{

    public AbstractRedisQueryService(StringRedisTemplate redisTemplate) {
        super(redisTemplate);
    }

    @Override
    public void save(String key, String value){
        opsValue().set(key, value);
    }

    @Override
    public void save(String key, String value, Duration ttl){
        opsValue().set(key, value, ttl);
    }

    @Override
    public void delete(String key){
        getRedisTemplate().delete(key);
    }

}
