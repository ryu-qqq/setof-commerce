package com.setof.connectly.module.event.service.product;

import com.setof.connectly.module.common.enums.RedisKey;
import com.setof.connectly.module.common.service.AbstractRedisService;
import com.setof.connectly.module.event.dto.EventProductGroup;
import com.setof.connectly.module.event.enums.EventType;
import com.setof.connectly.module.utils.JsonUtils;
import java.time.Duration;
import java.time.LocalDateTime;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class EventProductRedisQueryServiceImpl extends AbstractRedisService
        implements EventProductRedisQueryService {

    public EventProductRedisQueryServiceImpl(StringRedisTemplate redisTemplate) {
        super(redisTemplate);
    }

    public void saveEventProductStockCheckInCache(EventProductGroup eventProductGroup) {
        String key =
                generateKey(
                        RedisKey.EVENT,
                        EventType.PRODUCT.name() + eventProductGroup.getProductGroupId());
        String value = JsonUtils.toJson(eventProductGroup);

        Duration ttl =
                Duration.between(
                        LocalDateTime.now(),
                        eventProductGroup.getEventPeriod().getDisplayEndDate());

        save(key, value, ttl);
    }
}
