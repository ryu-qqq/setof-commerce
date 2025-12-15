package com.setof.connectly.module.order.service;

import com.setof.connectly.module.order.enums.OrderStatus;
import com.setof.connectly.module.order.enums.OrderStatusGroup;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class OrderStatusGroupMapping {

    private static final Map<OrderStatus, OrderStatusGroup> statusGroupMap = new HashMap<>();

    static {
        statusGroupMap.put(OrderStatus.ORDER_FAILED, OrderStatusGroup.FAIL);
        statusGroupMap.put(OrderStatus.ORDER_COMPLETED, OrderStatusGroup.COMPLETED);
        statusGroupMap.put(OrderStatus.CANCEL_REQUEST_RECANT, OrderStatusGroup.COMPLETED);
        statusGroupMap.put(OrderStatus.RETURN_REQUEST_RECANT, OrderStatusGroup.COMPLETED);

        // 결제 취소 주문 상태 매핑(아임포트와 통신해야함) claimOrder
        statusGroupMap.put(OrderStatus.CANCEL_REQUEST, OrderStatusGroup.PAYMENT_CANCELED);
        statusGroupMap.put(OrderStatus.RETURN_REQUEST, OrderStatusGroup.PAYMENT_CANCELED);
    }

    public static OrderStatusGroup getGroupForStatus(OrderStatus status) {
        return statusGroupMap.get(status);
    }
}
