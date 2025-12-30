package com.connectly.partnerAdmin.module.coreServer.request.product;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record ProductGroupContextCommandRequestDto(

	@NotNull(message = "Product Group cannot be null.")
	@Valid
	ProductGroupInsertRequestDto productGroup,

	@NotNull(message = "Product Notice cannot be null.")
	@Valid
    ProductNoticeInsertRequestDto productNotice,

	@NotNull(message = "Product Delivery cannot be null.")
	@Valid
	ProductDeliveryRequestDto productDelivery,

	@NotNull(message = "Product Image List cannot be null.")
	@Size(min = 1, max = 10, message = "Product Image List cannot be empty.")
	@Valid
	List<ProductGroupImageRequestDto> productImageList,

	@NotNull(message = "Product Detail Description cannot be null.")
	@Valid
    ProductDetailDescriptionRequestDto detailDescription,

	@Valid
	List<ProductInsertRequestDto> productOptions
) {}
