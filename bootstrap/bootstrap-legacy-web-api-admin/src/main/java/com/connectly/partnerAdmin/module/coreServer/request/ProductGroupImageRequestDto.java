package com.connectly.partnerAdmin.module.coreServer.request;

import com.connectly.partnerAdmin.module.product.enums.image.ProductGroupImageType;

public record ProductGroupImageRequestDto(
	ProductGroupImageType type,
	String productImageUrl
) {
}
