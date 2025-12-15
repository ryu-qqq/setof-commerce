package com.setof.connectly.module.event.service.mileage;

import com.setof.connectly.module.common.enums.RedisKey;
import com.setof.connectly.module.common.service.AbstractRedisService;
import com.setof.connectly.module.event.enums.EventMileageType;
import com.setof.connectly.module.event.enums.EventType;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class EventMileageRedisFindServiceImpl extends AbstractRedisService
        implements EventMileageRedisFindService {

    public EventMileageRedisFindServiceImpl(StringRedisTemplate redisTemplate) {
        super(redisTemplate);
    }

    @Override
    public String fetchEventMileage(EventMileageType eventMileageType) {
        String key =
                generateKey(RedisKey.EVENT, EventType.MILEAGE.name() + eventMileageType.name());
        return getValue(key);
    }
}
