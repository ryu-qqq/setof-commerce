package com.setof.connectly.module.event.service.mileage;

import com.setof.connectly.module.event.dto.EventMileageDto;
import com.setof.connectly.module.event.enums.EventMileageType;

public interface EventMileageFindService {
    EventMileageDto fetchEventMileage(EventMileageType eventMileageType);
}
