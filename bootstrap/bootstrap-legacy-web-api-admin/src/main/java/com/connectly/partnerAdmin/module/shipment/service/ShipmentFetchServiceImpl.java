package com.connectly.partnerAdmin.module.shipment.service;


import com.connectly.partnerAdmin.module.shipment.exception.ShipmentNotFoundException;
import com.connectly.partnerAdmin.module.shipment.dto.ShipmentCodeDto;
import com.connectly.partnerAdmin.module.shipment.entity.Shipment;
import com.connectly.partnerAdmin.module.shipment.repository.ShipmentFetchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class ShipmentFetchServiceImpl implements ShipmentFetchService {

    private final ShipmentFetchRepository shipmentFetchRepository;

    @Override
    public Shipment fetchShipmentEntity(long orderId){
        return shipmentFetchRepository.fetchShipmentEntity(orderId)
                .orElseThrow(()-> new ShipmentNotFoundException(orderId));
    }

    @Cacheable("shipmentCodes")
    @Override
    public List<ShipmentCodeDto> fetchShipmentCode() {
        return shipmentFetchRepository.fetchShipmentCode();
    }

}
