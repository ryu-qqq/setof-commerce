package com.connectly.partnerAdmin.module.order.service.strategy;

import com.connectly.partnerAdmin.module.order.dto.query.NormalOrder;
import com.connectly.partnerAdmin.module.order.entity.Order;
import com.connectly.partnerAdmin.module.order.enums.OrderStatus;
import com.connectly.partnerAdmin.module.order.service.OrderFetchService;
import com.connectly.partnerAdmin.module.order.validator.OrderStatusProcessor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class OrderDeliveryPendingStrategy extends AbstractOrderUpdateStrategy<NormalOrder> {

    public OrderDeliveryPendingStrategy(OrderStatusProcessor orderStatusProcessor, OrderFetchService orderFetchService) {
        super(orderStatusProcessor, orderFetchService);
    }

    @Override
    public OrderStatus getOrderStatus() {
        return OrderStatus.DELIVERY_PENDING;
    }

    @Override
    protected void additionalUpdateLogic(Order order, NormalOrder updateOrder) {}

}
