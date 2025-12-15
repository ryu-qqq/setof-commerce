package com.setof.connectly.module.order.service.status;

import com.setof.connectly.module.order.enums.OrderStatus;
import org.springframework.stereotype.Component;

@Component
public class OrderReturnRequestTransition implements OrderStatusTransition {

    @Override
    public OrderStatus getHandledStatus() {
        return OrderStatus.RETURN_REQUEST;
    }

    /**
     * 현재 상태 배송중, 배송완료, 반품 거절 -> 반품 요청
     *
     * @param toStatus
     * @return available
     */
    @Override
    public boolean canTransition(OrderStatus toStatus) {
        return toStatus.isDeliveryProcessing()
                || toStatus.isDeliveryCompleted()
                || toStatus.isReturnRequestRejected()
                || toStatus.isSettlementProcessing();
    }
}
