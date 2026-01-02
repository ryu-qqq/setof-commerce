package com.connectly.partnerAdmin.module.order.dto.query;


import com.connectly.partnerAdmin.module.generic.money.Money;
import com.connectly.partnerAdmin.module.order.enums.OrderStatus;

public interface OrderSheet {

    long getUserId();
    OrderStatus getOrderStatus();
    long getProductId();
    long getProductGroupId();
    Long getPaymentId();
    long getSellerId();
    Money getOrderAmount();
    int getQuantity();
}
