package com.setof.connectly.module.common.service;

import com.setof.connectly.module.common.enums.RedisKey;

public interface RedisKeyGenerator {
    String generateKey(RedisKey redisKey, String key);
}
