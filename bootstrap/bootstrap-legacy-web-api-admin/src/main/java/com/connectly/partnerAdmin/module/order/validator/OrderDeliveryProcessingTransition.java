package com.connectly.partnerAdmin.module.order.validator;

import com.connectly.partnerAdmin.module.order.enums.OrderStatus;
import org.springframework.stereotype.Component;

@Component
public class OrderDeliveryProcessingTransition implements OrderStatusTransition{
    @Override
    public OrderStatus getHandledStatus() {
        return OrderStatus.DELIVERY_PROCESSING;
    }

    /**
     * 현재 상태 배송 중
     *  -> 배송 중 , 반품 요청, 배송 완료 (수동 처리 .. 퀵, 잘못된 운송장 번호 기입, 직접 수령 등)
     * @param toStatus
     * @return available
     */

    @Override
    public boolean canTransition(OrderStatus toStatus) {
        return toStatus.isDeliveryCompleted() || toStatus.isDeliveryProcessing() || toStatus.isReturnRequest();
    }
}
