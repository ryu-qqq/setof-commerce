package com.connectly.partnerAdmin.module.order.service.strategy;

import com.connectly.partnerAdmin.module.order.dto.query.ClaimRejectedAndShipmentOrder;
import com.connectly.partnerAdmin.module.order.entity.Order;
import com.connectly.partnerAdmin.module.order.enums.OrderStatus;
import com.connectly.partnerAdmin.module.order.service.OrderFetchService;
import com.connectly.partnerAdmin.module.order.validator.OrderStatusProcessor;
import com.connectly.partnerAdmin.module.shipment.dto.ShipmentInfo;
import com.connectly.partnerAdmin.module.shipment.entity.Shipment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class OrderCancelRequestRejectedStrategy extends AbstractOrderUpdateStrategy<ClaimRejectedAndShipmentOrder> {


    public OrderCancelRequestRejectedStrategy(OrderStatusProcessor orderStatusProcessor, OrderFetchService orderFetchService) {
        super(orderStatusProcessor, orderFetchService);
    }

    @Override
    public OrderStatus getOrderStatus() {
        return OrderStatus.CANCEL_REQUEST_REJECTED;
    }


    @Override
    protected void additionalUpdateLogic(Order order, ClaimRejectedAndShipmentOrder updateOrder) {
        Shipment shipment = order.getShipment();
        ShipmentInfo shipmentInfo = updateOrder.getShipmentInfo();
        shipment.deliveryStart(shipmentInfo.getInvoiceNo(), shipmentInfo.getCompanyCode(), shipmentInfo.getShipmentType());
    }
}
