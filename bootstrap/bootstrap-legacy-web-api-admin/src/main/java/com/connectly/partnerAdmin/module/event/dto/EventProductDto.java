package com.connectly.partnerAdmin.module.event.dto;

import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EventProductDto {

    private long productId;
    private Yn rewardsMileage;

    @QueryProjection
    public EventProductDto(long productId, Yn rewardsMileage) {
        this.productId = productId;
        this.rewardsMileage = rewardsMileage;
    }
}
