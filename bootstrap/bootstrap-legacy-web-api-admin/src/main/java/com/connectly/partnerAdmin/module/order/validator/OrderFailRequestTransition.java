package com.connectly.partnerAdmin.module.order.validator;

import com.connectly.partnerAdmin.module.order.enums.OrderStatus;
import org.springframework.stereotype.Component;

@Component
public class OrderFailRequestTransition implements OrderStatusTransition{

    @Override
    public OrderStatus getHandledStatus() {
        return OrderStatus.ORDER_FAILED;
    }

    /**
     * 현재 상태 주문 실패
     *  -> 주문 진행, 주문 실패, 주문 완료, 판매 취소
     * @param toStatus
     * @return available
     */

    @Override
    public boolean canTransition(OrderStatus toStatus) {
        return toStatus.isOrderProcessing() || toStatus.isOrderFailed() || toStatus.isOrderCompleted() || toStatus.isSaleCancelled();
    }
}
