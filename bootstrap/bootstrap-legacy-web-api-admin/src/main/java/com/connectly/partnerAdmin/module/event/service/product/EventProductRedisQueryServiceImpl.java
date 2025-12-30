package com.connectly.partnerAdmin.module.event.service.product;


import com.connectly.partnerAdmin.module.common.enums.RedisKey;
import com.connectly.partnerAdmin.module.common.service.AbstractRedisQueryService;
import com.connectly.partnerAdmin.module.event.dto.EventProductGroup;
import com.connectly.partnerAdmin.module.event.enums.EventType;
import com.connectly.partnerAdmin.module.utils.JsonUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class EventProductRedisQueryServiceImpl extends AbstractRedisQueryService implements EventProductRedisQueryService{

    public EventProductRedisQueryServiceImpl(StringRedisTemplate redisTemplate) {
        super(redisTemplate);
    }


    public void saveEventProductStockCheckInCache(EventProductGroup eventProductGroup){
        String key = generateKey(RedisKey.EVENT, EventType.PRODUCT.name() + eventProductGroup.getProductGroupId());
        String value = JsonUtils.toJson(eventProductGroup);

        Duration ttl = Duration.between(LocalDateTime.now(), eventProductGroup.getEventPeriod().getDisplayEndDate());

        save(key, value, ttl);
    }

}
