package com.connectly.partnerAdmin.module.event.service.mileage;


import com.connectly.partnerAdmin.module.event.dto.EventMileageInfo;

public interface EventMileageRedisQueryService {
    void saveEventMileage(EventMileageInfo eventMileageInfo);

}
