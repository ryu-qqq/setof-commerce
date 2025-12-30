package com.connectly.partnerAdmin.module.order.validator;

import com.connectly.partnerAdmin.module.order.enums.OrderStatus;
import org.springframework.stereotype.Component;

@Component
public class OrderDeliveryCompletedTransition implements OrderStatusTransition{
    @Override
    public OrderStatus getHandledStatus() {
        return OrderStatus.DELIVERY_COMPLETED;
    }

    /**
     * 현재 상태 배송 완료
     *  -> 배송 완료 (수동 처리 .. 퀵, 잘못된 운송장 번호 기입, 직접 수령 등)
     * @param toStatus
     * @return available
     */


    @Override
    public boolean canTransition(OrderStatus toStatus) {
        return toStatus.isDeliveryProcessing() || toStatus.isDeliveryCompleted() || toStatus.isReturnRequest();
    }
}
