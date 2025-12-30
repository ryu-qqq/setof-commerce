package com.connectly.partnerAdmin.module.order.validator;

import com.connectly.partnerAdmin.module.order.enums.OrderStatus;
import org.springframework.stereotype.Component;

@Component
public class OrderReturnRequestTransition implements OrderStatusTransition{

    @Override
    public OrderStatus getHandledStatus() {
        return OrderStatus.RETURN_REQUEST;
    }

    /**
     * 현재 상태 반품 요청
     *  -> 반품 요청 승인, 반품 요청 거절
     * @param toStatus
     * @return available
     */
    @Override
    public boolean canTransition(OrderStatus toStatus) {
        return toStatus.isReturnRequestConfirmed() || toStatus.isReturnRequestRejected() ||
                toStatus.isReturnRequest();
    }
}
