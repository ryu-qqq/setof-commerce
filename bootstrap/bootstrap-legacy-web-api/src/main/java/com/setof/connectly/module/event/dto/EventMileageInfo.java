package com.setof.connectly.module.event.dto;

import com.setof.connectly.module.display.entity.embedded.DisplayPeriod;
import com.setof.connectly.module.event.enums.EventMileageType;

public interface EventMileageInfo {

    double getMileageRate();

    int getExpirationDate();

    DisplayPeriod getEventPeriod();

    EventMileageType getEventMileageType();
}
