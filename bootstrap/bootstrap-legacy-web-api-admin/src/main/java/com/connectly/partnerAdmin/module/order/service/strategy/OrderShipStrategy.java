package com.connectly.partnerAdmin.module.order.service.strategy;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.connectly.partnerAdmin.module.order.dto.query.ShipOrder;
import com.connectly.partnerAdmin.module.order.entity.Order;
import com.connectly.partnerAdmin.module.order.enums.OrderStatus;
import com.connectly.partnerAdmin.module.order.service.OrderFetchService;
import com.connectly.partnerAdmin.module.order.validator.OrderStatusProcessor;
import com.connectly.partnerAdmin.module.shipment.dto.ShipmentInfo;
import com.connectly.partnerAdmin.module.shipment.service.ShipmentQueryService;

@Transactional
@Service
public class OrderShipStrategy extends AbstractOrderUpdateStrategy<ShipOrder> {

    private final ShipmentQueryService shipmentQueryService;

    public OrderShipStrategy(OrderStatusProcessor orderStatusProcessor, OrderFetchService orderFetchService,
                             ShipmentQueryService shipmentQueryService) {
        super(orderStatusProcessor, orderFetchService);
        this.shipmentQueryService = shipmentQueryService;
    }

    @Override
    public OrderStatus getOrderStatus() {
        return OrderStatus.DELIVERY_PROCESSING;
    }

    private void deliveryStart(Order order, ShipmentInfo shipmentInfo) {
        shipmentQueryService.deliveryStart(order.getId(), shipmentInfo);
    }

    @Override
    protected void additionalUpdateLogic(Order order, ShipOrder shipOrder) {
        deliveryStart(order, shipOrder.getShipmentInfo());
    }
}
