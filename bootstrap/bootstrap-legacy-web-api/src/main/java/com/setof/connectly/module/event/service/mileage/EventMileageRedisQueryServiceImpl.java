package com.setof.connectly.module.event.service.mileage;

import com.setof.connectly.module.common.enums.RedisKey;
import com.setof.connectly.module.common.service.AbstractRedisService;
import com.setof.connectly.module.event.dto.EventMileageInfo;
import com.setof.connectly.module.event.enums.EventType;
import com.setof.connectly.module.utils.JsonUtils;
import java.time.Duration;
import java.time.LocalDateTime;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class EventMileageRedisQueryServiceImpl extends AbstractRedisService
        implements EventMileageRedisQueryService {

    public EventMileageRedisQueryServiceImpl(StringRedisTemplate redisTemplate) {
        super(redisTemplate);
    }

    @Override
    public void saveEventMileage(EventMileageInfo eventMileageInfo) {
        String key =
                generateKey(
                        RedisKey.EVENT,
                        EventType.MILEAGE.name() + eventMileageInfo.getEventMileageType().name());
        String value = JsonUtils.toJson(eventMileageInfo);
        Duration ttl =
                Duration.between(
                        LocalDateTime.now(), eventMileageInfo.getEventPeriod().getDisplayEndDate());
        save(key, value, ttl);
    }
}
