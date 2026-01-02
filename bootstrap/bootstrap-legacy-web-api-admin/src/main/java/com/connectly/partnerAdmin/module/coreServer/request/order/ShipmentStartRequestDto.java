package com.connectly.partnerAdmin.module.coreServer.request.order;

import com.connectly.partnerAdmin.module.shipment.enums.ShipmentCompanyCode;

public record ShipmentStartRequestDto(
    long siteId,
    long externalOrderId,
    long externalOrderPkId,
    String invoiceNo,
    ShipmentCompanyCode shipmentCode
) {
}
