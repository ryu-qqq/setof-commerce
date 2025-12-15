package com.setof.connectly.module.event.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.setof.connectly.module.common.enums.Yn;
import com.setof.connectly.module.display.entity.embedded.DisplayPeriod;
import com.setof.connectly.module.event.enums.EventPayType;
import com.setof.connectly.module.event.enums.EventProductType;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EventProductStockCheck implements EventProductGroup {

    private long productGroupId;
    private Yn limitYn;
    private int limitQty;
    private DisplayPeriod eventPeriod;
    private EventPayType eventPayType;
    private EventProductType eventProductType;
    private Yn rewardsMileage;
    private Set<Long> productIds;

    @Builder
    @QueryProjection
    public EventProductStockCheck(
            long productGroupId,
            Yn limitYn,
            int limitQty,
            DisplayPeriod eventPeriod,
            EventPayType eventPayType,
            EventProductType eventProductType,
            Yn rewardsMileage,
            Set<Long> productIds) {
        this.productGroupId = productGroupId;
        this.limitYn = limitYn;
        this.limitQty = limitQty;
        this.eventPeriod = eventPeriod;
        this.eventPayType = eventPayType;
        this.eventProductType = eventProductType;
        this.rewardsMileage = rewardsMileage;
        this.productIds = productIds;
    }
}
