package com.connectly.partnerAdmin.module.event.dto;

import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.connectly.partnerAdmin.module.display.entity.embedded.DisplayPeriod;
import com.connectly.partnerAdmin.module.event.enums.EventPayType;
import com.connectly.partnerAdmin.module.event.enums.EventProductType;
import lombok.*;

import java.util.Set;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class EventProductStockCheck implements EventProductGroup{

    private long productGroupId;
    private Yn limitYn;
    private int limitQty;
    private DisplayPeriod eventPeriod;
    private EventPayType eventPayType;
    private EventProductType eventProductType;
    private Yn rewardsMileage;
    private Set<Long> productIds;



}
