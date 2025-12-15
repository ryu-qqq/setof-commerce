package com.setof.connectly.module.seller.service;

import com.setof.connectly.module.common.enums.RedisKey;
import com.setof.connectly.module.common.service.AbstractRedisService;
import com.setof.connectly.module.seller.dto.SellerInfo;
import com.setof.connectly.module.utils.JsonUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class SellerRedisQueryServiceImpl extends AbstractRedisService
        implements SellerRedisQueryService {

    public SellerRedisQueryServiceImpl(StringRedisTemplate redisTemplate) {
        super(redisTemplate);
    }

    @Override
    public void saveSellerInRedis(SellerInfo sellerInfo) {
        String key = generateKey(RedisKey.SELLER, String.valueOf(sellerInfo.getSellerId()));
        String value = JsonUtils.toJson(sellerInfo);
        save(key, value);
    }
}
