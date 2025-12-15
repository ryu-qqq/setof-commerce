package com.setof.connectly.module.cart.service.fetch;

import com.setof.connectly.module.common.enums.RedisKey;
import com.setof.connectly.module.common.service.AbstractRedisService;
import com.setof.connectly.module.utils.JsonUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class CartCountRedisFindServiceImpl extends AbstractRedisService
        implements CartCountRedisFindService {

    public CartCountRedisFindServiceImpl(StringRedisTemplate redisTemplate) {
        super(redisTemplate);
    }

    public Long fetchCartCountInCache(long userId) {
        String key = generateKey(RedisKey.CART_COUNT, String.valueOf(userId));
        String value = getValue(key);
        if (StringUtils.hasText(value)) {
            return parseCount(value);
        }
        return null;
    }

    private Long parseCount(String value) {
        return JsonUtils.fromJson(value, Long.class);
    }
}
