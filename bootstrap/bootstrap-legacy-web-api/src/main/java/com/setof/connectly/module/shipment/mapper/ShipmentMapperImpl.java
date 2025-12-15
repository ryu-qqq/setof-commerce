package com.setof.connectly.module.shipment.mapper;

import com.setof.connectly.module.order.entity.order.Order;
import com.setof.connectly.module.seller.dto.SenderDto;
import com.setof.connectly.module.shipment.entity.Shipment;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class ShipmentMapperImpl implements ShipmentMapper {

    @Override
    public List<Shipment> toEntities(
            long shippingId, Map<Long, List<Order>> sellerIdMap, List<SenderDto> senders) {
        HashSet<SenderDto> temps = new HashSet<>(senders);
        Map<Long, SenderDto> senderMap =
                temps.stream()
                        .collect(Collectors.toMap(SenderDto::getSellerId, Function.identity()));

        Set<Map.Entry<Long, List<Order>>> entries = sellerIdMap.entrySet();

        List<Shipment> results = new ArrayList<>();

        for (Map.Entry<Long, List<Order>> entry : entries) {
            SenderDto senderDto = senderMap.get(entry.getKey());
            List<Shipment> shipments =
                    entry.getValue().stream()
                            .map(order -> new Shipment(order.getId(), shippingId, senderDto))
                            .collect(Collectors.toList());

            results.addAll(shipments);
        }

        return results;
    }
}
