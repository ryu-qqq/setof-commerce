package com.connectly.partnerAdmin.module.coreServer.request;

import com.connectly.partnerAdmin.module.product.enums.group.Origin;

public record ProductNoticeInsertRequestDto(

	String material,
	String color,
	String size,
	String maker,
	Origin origin,

	String washingMethod,
	String yearMonth,
	String assuranceStandard,
	String asPhone
) {
}
