package com.connectly.partnerAdmin.module.shipment.enums;

import com.connectly.partnerAdmin.module.common.enums.EnumType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DeliveryStatus implements EnumType {

    DELIVERY_PENDING,
    DELIVERY_PROCESSING,
    DELIVERY_COMPLETED,

//    DELIVERY_CHANGE_REQUEST,
//    DELIVERY_CHANGE_PROCESSING,
//    DELIVERY_CHANGE_COMPLETE,

    DELIVERY_RETURN_REQUEST,
    DELIVERY_RETURN_PROCESSING,
    DELIVERY_RETURN_COMPLETE;


    @Override
    public String getName() {
        return name();
    }

    @Override
    public String getDescription() {
        return name();
    }

    public boolean isDeliveryProcessing(){
        return this.equals(DELIVERY_PROCESSING);
    }

    public boolean isDeliveryCompleted(){
        return this.equals(DELIVERY_COMPLETED);
    }
}
