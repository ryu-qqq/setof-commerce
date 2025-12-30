package com.connectly.partnerAdmin.module.event.dto;

import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.connectly.partnerAdmin.module.display.entity.embedded.DisplayPeriod;
import com.connectly.partnerAdmin.module.event.enums.EventPayType;
import com.connectly.partnerAdmin.module.event.enums.EventProductType;

import java.util.Set;

public interface EventProductGroup {

    long getProductGroupId();
    Yn getLimitYn();
    int getLimitQty();
    DisplayPeriod getEventPeriod();
    EventPayType getEventPayType();
    EventProductType getEventProductType();
    Yn getRewardsMileage();
    Set<Long> getProductIds();

}
