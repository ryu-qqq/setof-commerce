package com.setof.connectly.module.notification.enums;

import com.setof.connectly.module.order.enums.OrderStatus;

public enum OrderAlimTalkTemplateGroup {
    ORDER_TRIGGER,
    ORDER_CANCEL_TRIGGER,
    ORDER_RETURN_TRIGGER,

    ORDER_DELIVERY,
    ORDER_CANCELED,
    ORDER_RETURN_ACCEPT,
    ORDER_RETURN_REJECT,
    ORDER_SALE_CANCELED,
    ;

    public static OrderAlimTalkTemplateGroup of(OrderStatus orderStatus) {
        switch (orderStatus) {
            case ORDER_COMPLETED:
                return ORDER_TRIGGER;
            case CANCEL_REQUEST:
                return ORDER_CANCEL_TRIGGER;
            case RETURN_REQUEST:
                return ORDER_RETURN_TRIGGER;
            case DELIVERY_PROCESSING:
                return ORDER_DELIVERY;
            case CANCEL_REQUEST_COMPLETED:
                return ORDER_CANCELED;
            default:
                return null;
        }
    }
}
