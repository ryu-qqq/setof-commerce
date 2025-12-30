package com.connectly.partnerAdmin.module.coreServer.request;

import java.math.BigDecimal;

import com.connectly.partnerAdmin.module.product.enums.delivery.ReturnMethod;
import com.connectly.partnerAdmin.module.shipment.enums.ShipmentCompanyCode;

public record ProductDeliveryRequestDto(

	String deliveryArea,
	BigDecimal deliveryFee,
	int deliveryPeriodAverage,
	ReturnMethod returnMethodDomestic,
	ShipmentCompanyCode returnCourierDomestic,
	BigDecimal returnChargeDomestic,
	String returnExchangeAreaDomestic
) {
}
