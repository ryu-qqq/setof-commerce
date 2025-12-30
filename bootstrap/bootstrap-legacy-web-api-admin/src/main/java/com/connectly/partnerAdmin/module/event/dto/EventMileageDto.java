package com.connectly.partnerAdmin.module.event.dto;


import com.connectly.partnerAdmin.module.display.entity.embedded.DisplayPeriod;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class EventMileageDto implements EventMileageInfo {

    private double mileageRate;
    private int expirationDate;
    private DisplayPeriod eventPeriod;

    @QueryProjection
    public EventMileageDto(double mileageRate, int expirationDate, DisplayPeriod eventPeriod) {
        this.mileageRate = mileageRate;
        this.expirationDate = expirationDate;
        this.eventPeriod = eventPeriod;
    }
}
