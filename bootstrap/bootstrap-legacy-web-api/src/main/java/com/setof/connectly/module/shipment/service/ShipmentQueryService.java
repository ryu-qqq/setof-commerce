package com.setof.connectly.module.shipment.service;

import com.setof.connectly.module.order.entity.order.Order;
import java.util.List;

public interface ShipmentQueryService {

    void saveShipments(long paymentId, List<Order> orders);
}
