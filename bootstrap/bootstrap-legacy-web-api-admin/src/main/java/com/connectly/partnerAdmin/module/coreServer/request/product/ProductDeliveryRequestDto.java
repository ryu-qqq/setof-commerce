package com.connectly.partnerAdmin.module.coreServer.request.product;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

import com.connectly.partnerAdmin.module.product.enums.delivery.ReturnMethod;
import com.connectly.partnerAdmin.module.shipment.enums.ShipmentCompanyCode;

public record ProductDeliveryRequestDto(
	@NotBlank(message = "Delivery Area cannot be blank.")
	@Size(max = 200, message = "Delivery Area must be 15 characters or less.")
	String deliveryArea,

	@NotNull(message = "Delivery Fee cannot be null.")
	@DecimalMax(value = "50000", inclusive = true, message = "Delivery Fee must be less than or equal to 50,000.")
	BigDecimal deliveryFee,

	@NotNull(message = "Delivery Period Average cannot be null.")
	@Max(value = 30, message = "Delivery Period Average must be 30 days or less.")
	int deliveryPeriodAverage,

	@NotNull(message = "Return Method Domestic cannot be null.")
	ReturnMethod returnMethodDomestic,

	@NotNull(message = "Return Courier Domestic cannot be null.")
	ShipmentCompanyCode returnCourierDomestic,

	@NotNull(message = "Return Charge Domestic cannot be null.")
	@DecimalMax(value = "50000", inclusive = true, message = "Return Charge Domestic must be less than or equal to 50,000.")
	BigDecimal returnChargeDomestic,

	@NotBlank(message = "Return Exchange Area Domestic cannot be blank.")
	@Size(max = 200, message = "Return Exchange Area Domestic must be 15 characters or less.")
	String returnExchangeAreaDomestic
) {
}
