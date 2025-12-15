package com.setof.connectly.module.shipment.service;

import com.setof.connectly.module.order.entity.order.Order;
import com.setof.connectly.module.payment.service.snapshot.fetch.PaymentSnapShotShippingFetchService;
import com.setof.connectly.module.seller.dto.SenderDto;
import com.setof.connectly.module.seller.service.SellerFindService;
import com.setof.connectly.module.shipment.entity.Shipment;
import com.setof.connectly.module.shipment.mapper.ShipmentMapper;
import com.setof.connectly.module.shipment.repository.ShipmentJdbcRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class ShipmentQueryServiceImpl implements ShipmentQueryService {
    private final ShipmentJdbcRepository shipmentJdbcRepository;
    private final PaymentSnapShotShippingFetchService paymentSnapShotShippingFetchService;
    private final SellerFindService sellerFindService;
    private final ShipmentMapper shipmentMapper;

    @Override
    public void saveShipments(long paymentId, List<Order> orders) {
        long shippingId =
                paymentSnapShotShippingFetchService.fetchPaymentSnapShotShippingId(paymentId);

        Map<Long, List<Order>> sellerIdMap =
                orders.stream().collect(Collectors.groupingBy(Order::getSellerId));

        List<SenderDto> senders =
                sellerFindService.fetchSenders(new ArrayList<>(sellerIdMap.keySet()));
        List<Shipment> shipments = shipmentMapper.toEntities(shippingId, sellerIdMap, senders);
        saveShipment(shipments);
    }

    public void saveShipment(List<Shipment> shipments) {
        shipmentJdbcRepository.saveAll(shipments);
    }
}
