package com.connectly.partnerAdmin.module.shipment.service;

import com.connectly.partnerAdmin.module.order.entity.Order;
import com.connectly.partnerAdmin.module.seller.core.BusinessSellerContext;
import com.connectly.partnerAdmin.module.shipment.dto.ShipmentInfo;

public interface ShipmentQueryService {

    void saveShipment(long paymentSnapShotShippingId, Order order, BusinessSellerContext businessSellerContext);

    ShipmentInfo deliveryStart(long orderId, ShipmentInfo shipmentInfo);

}
