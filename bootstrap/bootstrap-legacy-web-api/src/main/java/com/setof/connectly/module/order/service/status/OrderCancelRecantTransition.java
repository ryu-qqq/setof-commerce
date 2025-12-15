package com.setof.connectly.module.order.service.status;

import com.setof.connectly.module.order.enums.OrderStatus;
import org.springframework.stereotype.Component;

@Component
public class OrderCancelRecantTransition implements OrderStatusTransition {

    @Override
    public OrderStatus getHandledStatus() {
        return OrderStatus.CANCEL_REQUEST_RECANT;
    }

    /**
     * 현재 상태 결제완료 및 배송 준비중 -> 취소 요청
     *
     * @param toStatus
     * @return available
     */
    @Override
    public boolean canTransition(OrderStatus toStatus) {
        return toStatus.isCancelRequest();
    }
}
