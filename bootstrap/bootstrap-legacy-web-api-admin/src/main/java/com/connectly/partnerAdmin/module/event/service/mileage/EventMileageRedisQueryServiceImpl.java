package com.connectly.partnerAdmin.module.event.service.mileage;

import com.connectly.partnerAdmin.module.common.enums.RedisKey;
import com.connectly.partnerAdmin.module.common.service.AbstractRedisQueryService;
import com.connectly.partnerAdmin.module.event.dto.EventMileageInfo;
import com.connectly.partnerAdmin.module.event.enums.EventType;
import com.connectly.partnerAdmin.module.utils.JsonUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class EventMileageRedisQueryServiceImpl extends AbstractRedisQueryService implements EventMileageRedisQueryService{

    public EventMileageRedisQueryServiceImpl(StringRedisTemplate redisTemplate) {
        super(redisTemplate);
    }

    @Override
    public void saveEventMileage(EventMileageInfo eventMileageInfo) {
        String key = generateKey(RedisKey.EVENT, EventType.MILEAGE.name());
        String value = JsonUtils.toJson(eventMileageInfo);
        Duration ttl = Duration.between(LocalDateTime.now(), eventMileageInfo.getEventPeriod().getDisplayEndDate());
        save(key, value, ttl);
    }


}
