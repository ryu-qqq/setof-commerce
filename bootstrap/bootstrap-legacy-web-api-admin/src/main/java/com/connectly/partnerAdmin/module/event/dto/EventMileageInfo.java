package com.connectly.partnerAdmin.module.event.dto;

import com.connectly.partnerAdmin.module.display.entity.embedded.DisplayPeriod;

public interface EventMileageInfo {

    double getMileageRate();
    int getExpirationDate();
    DisplayPeriod getEventPeriod();


}