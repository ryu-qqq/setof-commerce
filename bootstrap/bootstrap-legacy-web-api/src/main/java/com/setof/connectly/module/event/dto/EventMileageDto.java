package com.setof.connectly.module.event.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.setof.connectly.module.display.entity.embedded.DisplayPeriod;
import com.setof.connectly.module.event.enums.EventMileageType;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EventMileageDto implements EventMileageInfo {

    private double mileageAmount;
    private double mileageRate;
    private int expirationDate;
    private EventMileageType eventMileageType;
    private DisplayPeriod eventPeriod;

    @QueryProjection
    public EventMileageDto(
            double mileageAmount,
            double mileageRate,
            int expirationDate,
            EventMileageType eventMileageType,
            DisplayPeriod eventPeriod) {
        this.mileageAmount = mileageAmount;
        this.mileageRate = mileageRate;
        this.expirationDate = expirationDate;
        this.eventMileageType = eventMileageType;
        this.eventPeriod = eventPeriod;
    }

    public static EventMileageDto defaultEventMileageSetting() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDate = now.minusDays(30);
        LocalDateTime endDate = now.plusDays(30);
        return new EventMileageDto(
                0, 1, 30, EventMileageType.ORDER, new DisplayPeriod(startDate, endDate));
    }
}
