package com.connectly.partnerAdmin.module.display.service.component.query.gnb;


import com.connectly.partnerAdmin.module.common.enums.RedisKey;
import com.connectly.partnerAdmin.module.common.service.AbstractRedisQueryService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service

public class GnbRedisQueryServiceImpl extends AbstractRedisQueryService implements GnbRedisQueryService{

    public GnbRedisQueryServiceImpl(StringRedisTemplate redisTemplate) {
        super(redisTemplate);
    }

    @Override
    public void deleteGnbInRedis() {
        String key = generateKey(RedisKey.GNBS, "");
        delete(key);
    }

}
