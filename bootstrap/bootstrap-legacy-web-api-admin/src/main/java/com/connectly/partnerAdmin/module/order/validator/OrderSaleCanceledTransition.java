package com.connectly.partnerAdmin.module.order.validator;

import com.connectly.partnerAdmin.module.order.enums.OrderStatus;
import org.springframework.stereotype.Component;

@Component
public class OrderSaleCanceledTransition implements OrderStatusTransition{

    @Override
    public OrderStatus getHandledStatus() {
        return OrderStatus.SALE_CANCELLED;
    }

    /**
     * 현재 상태 판매 취소 -> 판매 취소, 판매 취소 완료
     * @param toStatus
     * @return available
     */

    @Override
    public boolean canTransition(OrderStatus toStatus) {
        return toStatus.isSaleCancelled() || toStatus.isCancelRequestCompleted();
    }
}
