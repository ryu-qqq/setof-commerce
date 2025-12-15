package com.setof.connectly.module.event.service.product;

import com.setof.connectly.module.common.enums.RedisKey;
import com.setof.connectly.module.common.service.AbstractRedisService;
import com.setof.connectly.module.event.enums.EventType;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class EventProductRedisFindServiceImpl extends AbstractRedisService
        implements EventProductRedisFindService {

    public EventProductRedisFindServiceImpl(StringRedisTemplate redisTemplate) {
        super(redisTemplate);
    }

    @Override
    public List<String> fetchEventProductStockCheck(List<Long> productGroupIds) {
        List<String> keys =
                productGroupIds.stream()
                        .map(
                                productGroupId ->
                                        generateKey(
                                                RedisKey.EVENT,
                                                EventType.PRODUCT.name() + productGroupId))
                        .collect(Collectors.toList());

        return getValues(keys);
    }

    @Override
    public String fetchEventProduct(long productGroupId) {
        String key = generateKey(RedisKey.EVENT, EventType.PRODUCT.name() + productGroupId);
        return getValue(key);
    }
}
