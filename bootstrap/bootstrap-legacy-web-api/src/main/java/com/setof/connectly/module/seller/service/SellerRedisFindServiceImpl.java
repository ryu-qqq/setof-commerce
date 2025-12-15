package com.setof.connectly.module.seller.service;

import com.setof.connectly.module.common.enums.RedisKey;
import com.setof.connectly.module.common.service.AbstractRedisService;
import com.setof.connectly.module.seller.dto.SellerInfo;
import com.setof.connectly.module.utils.JsonUtils;
import java.util.Optional;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class SellerRedisFindServiceImpl extends AbstractRedisService
        implements SellerRedisFindService {

    public SellerRedisFindServiceImpl(StringRedisTemplate redisTemplate) {
        super(redisTemplate);
    }

    @Override
    public Optional<SellerInfo> fetchSellerInRedis(long sellerId) {
        String key = generateKey(RedisKey.SELLER, String.valueOf(sellerId));
        String value = getValue(key);
        if (StringUtils.hasText(value)) return parseSellerInfo(value);
        else return Optional.empty();
    }

    private Optional<SellerInfo> parseSellerInfo(String value) {
        SellerInfo sellerInfo = JsonUtils.fromJson(value, SellerInfo.class);
        return Optional.of(sellerInfo);
    }
}
