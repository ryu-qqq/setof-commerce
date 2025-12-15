package com.setof.connectly.module.event.dto;

import com.setof.connectly.module.common.enums.Yn;
import com.setof.connectly.module.display.entity.embedded.DisplayPeriod;
import com.setof.connectly.module.event.enums.EventPayType;
import com.setof.connectly.module.event.enums.EventProductType;
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
