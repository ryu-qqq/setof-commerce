package com.setof.connectly.module.shipment.mapper;

import com.setof.connectly.module.order.entity.order.Order;
import com.setof.connectly.module.seller.dto.SenderDto;
import com.setof.connectly.module.shipment.entity.Shipment;
import java.util.List;
import java.util.Map;

public interface ShipmentMapper {

    List<Shipment> toEntities(
            long shippingId, Map<Long, List<Order>> sellerIdMap, List<SenderDto> senders);
}
