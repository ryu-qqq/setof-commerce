package com.connectly.partnerAdmin.module.order.validator;

import com.connectly.partnerAdmin.module.order.enums.OrderStatus;
import org.springframework.stereotype.Component;


@Component
public class OrderCompletedTransition implements OrderStatusTransition{

    @Override
    public OrderStatus getHandledStatus() {
        return OrderStatus.ORDER_COMPLETED;
    }

    /**
     * 현재 상태 결제 완료
     *  -> 결제 완료, 배송 준비중, 배송 중, 판매 취소, 판매 취소 요청
     * @param toStatus
     * @return available
     */
    @Override
    public boolean canTransition(OrderStatus toStatus) {
        return toStatus.isOrderCompleted() || toStatus.isDeliveryProcessing() || toStatus.isDeliveryPending() || toStatus.isSaleCancelled() || toStatus.isCancelRequest();
    }

}
