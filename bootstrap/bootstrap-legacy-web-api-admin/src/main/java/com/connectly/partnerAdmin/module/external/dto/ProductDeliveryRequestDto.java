package com.connectly.partnerAdmin.module.external.dto;

import com.connectly.partnerAdmin.module.product.enums.delivery.ReturnMethod;
import com.connectly.partnerAdmin.module.shipment.enums.ShipmentCompanyCode;

import java.math.BigDecimal;

public record ProductDeliveryRequestDto(
        String deliveryArea,
        BigDecimal deliveryFee,
        int deliveryPeriodAverage,
        ReturnMethod returnMethodDomestic,
        ShipmentCompanyCode returnCourierDomestic,
        BigDecimal returnChargeDomestic,
        String returnExchangeAreaDomestic
){
}
