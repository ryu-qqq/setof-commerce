package com.setof.connectly.module.event.service.mileage;

import com.setof.connectly.module.event.enums.EventMileageType;

public interface EventMileageRedisFindService {

    String fetchEventMileage(EventMileageType eventMileageType);
}
