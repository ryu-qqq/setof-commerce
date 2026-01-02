package com.connectly.partnerAdmin.module.order.service.strategy;

import com.connectly.partnerAdmin.module.mileage.service.MileagePublishService;
import com.connectly.partnerAdmin.module.order.dto.query.NormalOrder;
import com.connectly.partnerAdmin.module.order.entity.Order;
import com.connectly.partnerAdmin.module.order.enums.OrderStatus;
import com.connectly.partnerAdmin.module.order.service.OrderFetchService;
import com.connectly.partnerAdmin.module.order.validator.OrderStatusProcessor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Transactional
@Service
public class OrderSettlementCompletedStrategy extends AbstractOrderUpdateStrategy<NormalOrder> {

    private final MileagePublishService mileagePublishService;

    public OrderSettlementCompletedStrategy(OrderStatusProcessor orderStatusProcessor, OrderFetchService orderFetchService, MileagePublishService mileagePublishService) {
        super(orderStatusProcessor, orderFetchService);
        this.mileagePublishService = mileagePublishService;
    }

    @Override
    public OrderStatus getOrderStatus() {
        return OrderStatus.SETTLEMENT_COMPLETED;
    }


    @Override
    protected void additionalUpdateLogic(Order order, NormalOrder updateOrder) {
        order.setPurchaseConfirmedDate(LocalDateTime.now());
        order.getSettlement().settlementCompleted();
        mileagePublishService.publicMileage(order);
    }

}
