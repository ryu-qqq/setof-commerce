package com.connectly.partnerAdmin.module.order.validator;

import com.connectly.partnerAdmin.module.order.enums.OrderStatus;
import org.springframework.stereotype.Component;

@Component
public class OrderDeliveryPendingTransition implements OrderStatusTransition{
    @Override
    public OrderStatus getHandledStatus() {
        return OrderStatus.DELIVERY_PENDING;
    }

    /**
     * 현재 상태 배송 준비중
     *  -> 배송 준비중, 배송 중, 판매 취소(품절), 배송완료
     * @param toStatus
     * @return available
     */
    @Override
    public boolean canTransition(OrderStatus toStatus) {
        return toStatus.isDeliveryPending() || toStatus.isDeliveryProcessing() || toStatus.isSaleCancelled() || toStatus.isDeliveryCompleted();
    }
}
