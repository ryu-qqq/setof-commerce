package com.connectly.partnerAdmin.module.shipment.controller;


import com.connectly.partnerAdmin.module.payload.ApiResponse;
import com.connectly.partnerAdmin.module.shipment.dto.ShipmentCodeDto;
import com.connectly.partnerAdmin.module.shipment.service.ShipmentFetchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.connectly.partnerAdmin.module.common.config.EndPointsConstants.BASE_END_POINT_V1;
import static com.connectly.partnerAdmin.module.common.config.SecurityConstants.HAS_ANY_AUTHORITY_MASTER_SELLER;

@PreAuthorize(HAS_ANY_AUTHORITY_MASTER_SELLER)
@RestController
@RequestMapping(BASE_END_POINT_V1)
@RequiredArgsConstructor
public class ShipmentController {

    private final ShipmentFetchService shipmentFetchService;

    @GetMapping("/shipment/company-codes")
    public ResponseEntity<ApiResponse<List<ShipmentCodeDto>>> getOrder(){
        return ResponseEntity.ok(ApiResponse.success(shipmentFetchService.fetchShipmentCode()));
    }

}
