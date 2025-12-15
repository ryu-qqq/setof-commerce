package com.setof.connectly.module.order.service.status;

import com.setof.connectly.module.order.enums.OrderStatus;
import org.springframework.stereotype.Component;

@Component
public class OrderReturnRecantTransition implements OrderStatusTransition {

    @Override
    public OrderStatus getHandledStatus() {
        return OrderStatus.RETURN_REQUEST_RECANT;
    }

    /**
     * 반품 요청, 배송완료, 반품 거절 -> 반품 요청 취소
     *
     * @param toStatus
     * @return available
     */
    @Override
    public boolean canTransition(OrderStatus toStatus) {
        return toStatus.isReturnRequest();
    }
}
