package com.connectly.partnerAdmin.module.order.validator;

import com.connectly.partnerAdmin.module.order.enums.OrderStatus;
import org.springframework.stereotype.Component;


@Component
public class OrderSettlementCompleteTransition implements OrderStatusTransition{
    @Override
    public OrderStatus getHandledStatus() {
        return OrderStatus.SETTLEMENT_COMPLETED;
    }

    /**
     * 현재 상태 정산 완료 -> 정산 완료
     * @param toStatus
     * @return available
     */
    @Override
    public boolean canTransition(OrderStatus toStatus) {
        return  toStatus.isSettlementCompleted();
    }

}
