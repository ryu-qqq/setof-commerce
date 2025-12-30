package com.connectly.partnerAdmin.module.notification.enums;

import com.connectly.partnerAdmin.module.order.enums.OrderStatus;

public enum OrderAlimTalkTemplateGroup {
    ORDER_TRIGGER,
    ORDER_CANCEL_TRIGGER,
    ORDER_RETURN_TRIGGER,
    ORDER_DELIVERY,
    ORDER_CANCELED,
    ORDER_RETURN_ACCEPT,
    ORDER_RETURN_REJECT,
    ORDER_SALE_CANCELED,
    VBANK_ORDER_CANCELED
    ;

    public static OrderAlimTalkTemplateGroup of(OrderStatus orderStatus){
        return switch (orderStatus) {
            case ORDER_COMPLETED -> ORDER_TRIGGER;
            case CANCEL_REQUEST -> ORDER_CANCEL_TRIGGER;
            case RETURN_REQUEST -> ORDER_RETURN_TRIGGER;
            case DELIVERY_PROCESSING -> ORDER_DELIVERY;
            case CANCEL_REQUEST_COMPLETED -> ORDER_CANCELED;
            case RETURN_REQUEST_REJECTED -> ORDER_RETURN_REJECT;
            case RETURN_REQUEST_CONFIRMED -> ORDER_RETURN_ACCEPT;
            case SALE_CANCELLED -> ORDER_SALE_CANCELED;
            case ORDER_FAILED -> VBANK_ORDER_CANCELED;
            default -> null;
        };
    }
}
