package com.connectly.partnerAdmin.module.common.service;

import com.connectly.partnerAdmin.module.common.enums.RedisKey;

public interface RedisKeyGenerator {
    String generateKey(RedisKey redisKey, String key);

}
