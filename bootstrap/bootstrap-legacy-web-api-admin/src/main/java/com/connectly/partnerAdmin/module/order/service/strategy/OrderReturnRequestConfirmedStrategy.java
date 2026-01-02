package com.connectly.partnerAdmin.module.order.service.strategy;

import com.connectly.partnerAdmin.module.order.dto.query.ClaimOrder;
import com.connectly.partnerAdmin.module.order.enums.OrderStatus;
import com.connectly.partnerAdmin.module.order.service.OrderFetchService;
import com.connectly.partnerAdmin.module.order.service.OrderRefundService;
import com.connectly.partnerAdmin.module.order.validator.OrderStatusProcessor;
import com.connectly.partnerAdmin.module.product.service.stock.InventoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@Service
public class OrderReturnRequestConfirmedStrategy extends AbstractRefundOrderStrategy<ClaimOrder> {


    public OrderReturnRequestConfirmedStrategy(OrderStatusProcessor orderStatusProcessor, OrderFetchService orderFetchService, OrderRefundService orderRefundService, InventoryService inventoryService) {
        super(orderStatusProcessor, orderFetchService, orderRefundService, inventoryService);
    }

    @Override
    public OrderStatus getOrderStatus() {
        return OrderStatus.RETURN_REQUEST_CONFIRMED;
    }


}
