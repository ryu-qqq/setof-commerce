package com.setof.connectly.module.event.service.mileage;

import com.setof.connectly.module.event.dto.EventMileageInfo;

public interface EventMileageRedisQueryService {
    void saveEventMileage(EventMileageInfo eventMileageInfo);
}
