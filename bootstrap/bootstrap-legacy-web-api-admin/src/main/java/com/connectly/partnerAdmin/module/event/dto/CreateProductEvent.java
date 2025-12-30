package com.connectly.partnerAdmin.module.event.dto;

import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.connectly.partnerAdmin.module.event.enums.EventPayType;
import com.connectly.partnerAdmin.module.event.enums.EventProductType;
import com.connectly.partnerAdmin.module.event.enums.EventType;
import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;




@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@JsonTypeName("productEvent")
public class CreateProductEvent implements SubEvent {

    @NotNull(message = "productGroupId는 필수입니다")
    private long productGroupId;

    private Yn limitYn;

    @Max(value = 100, message = "최대값 100을 넘을 수 없습니다.")
    @Min(value = 0, message = "최소 0 이상이여야 합니다.")
    private int limitQty;

    @NotNull(message = "eventPayType는 필수입니다")
    private EventPayType eventPayType;

    @NotNull(message = "eventProductType는 필수입니다")
    private EventProductType eventProductType;

    @NotNull(message = "rewardsMileage는 필수입니다")
    private Yn rewardsMileage;



    @Override
    public EventType getEventType() {
        return EventType.PRODUCT;
    }

}
