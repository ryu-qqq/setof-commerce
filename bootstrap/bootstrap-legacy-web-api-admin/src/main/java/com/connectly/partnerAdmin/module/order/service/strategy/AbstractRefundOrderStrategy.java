package com.connectly.partnerAdmin.module.order.service.strategy;

import com.connectly.partnerAdmin.module.order.dto.query.UpdateOrder;
import com.connectly.partnerAdmin.module.order.entity.Order;
import com.connectly.partnerAdmin.module.order.service.OrderFetchService;
import com.connectly.partnerAdmin.module.order.service.OrderRefundService;
import com.connectly.partnerAdmin.module.order.validator.OrderStatusProcessor;
import com.connectly.partnerAdmin.module.product.dto.query.UpdateProductStock;
import com.connectly.partnerAdmin.module.product.service.stock.InventoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Transactional
@Service
public abstract class AbstractRefundOrderStrategy <T extends UpdateOrder> extends AbstractOrderUpdateStrategy<T>{

    private final OrderRefundService orderRefundService;
    private final InventoryService inventoryService;

    public AbstractRefundOrderStrategy(OrderStatusProcessor orderStatusProcessor, OrderFetchService orderFetchService, OrderRefundService orderRefundService, InventoryService inventoryService) {
        super(orderStatusProcessor, orderFetchService);
        this.orderRefundService = orderRefundService;
        this.inventoryService = inventoryService;
    }

    @Override
    protected void additionalUpdateLogic(Order order, T updateOrder) {
        updateStock(order);
        refund(order);
    }

    protected void updateStock(Order order){
        UpdateProductStock updateProductStock = new UpdateProductStock(order.getProductId(), order.getQuantity());
        inventoryService.increase(Collections.singletonList(updateProductStock));
    }


    protected void refund(Order order){
        orderRefundService.refund(order);
    }

}
