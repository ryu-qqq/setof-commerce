package com.connectly.partnerAdmin.module.event.dto;

import com.connectly.partnerAdmin.module.event.enums.EventType;
import com.connectly.partnerAdmin.module.mileage.enums.MileageIssueType;
import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@JsonTypeName("mileageEvent")
public class CreateMileageEvent implements SubEvent{


    private double mileageRate;

    private int expirationDate;

    @NotNull(message = "mileageType은 필수 입니다.")
    private MileageIssueType mileageType;

    private double mileageAmount;

    @Override
    public EventType getEventType() {
        return EventType.MILEAGE;
    }

}
