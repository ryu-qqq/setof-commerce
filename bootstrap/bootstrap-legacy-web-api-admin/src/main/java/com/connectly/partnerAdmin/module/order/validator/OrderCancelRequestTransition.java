package com.connectly.partnerAdmin.module.order.validator;

import com.connectly.partnerAdmin.module.order.enums.OrderStatus;
import org.springframework.stereotype.Component;

@Component
public class OrderCancelRequestTransition implements OrderStatusTransition{

    @Override
    public OrderStatus getHandledStatus() {
        return OrderStatus.CANCEL_REQUEST;
    }


    /**
     * 현재 상태 취소요청
     *  -> 취소 요청, 취소 요청 승인, 배송 준비중, 배송 중
     * @param toStatus
     * @return available
     */
    @Override
    public boolean canTransition(OrderStatus toStatus) {
        return toStatus.isCancelRequest() || toStatus.isCancelRequestConfirmed() || toStatus.isCancelRequestRejected()
                || toStatus.isDeliveryPending() || toStatus.isDeliveryProcessing();
    }
}
