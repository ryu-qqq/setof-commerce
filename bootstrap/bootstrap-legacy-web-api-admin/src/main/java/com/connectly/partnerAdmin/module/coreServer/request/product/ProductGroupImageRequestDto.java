package com.connectly.partnerAdmin.module.coreServer.request.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import com.connectly.partnerAdmin.module.product.enums.image.ProductGroupImageType;

public record ProductGroupImageRequestDto(
	@NotNull(message = "ProductImage Type cannot be null.")
	ProductGroupImageType productImageType,

	@NotBlank(message = "Image Url Name cannot be blank.")
	@Size(max = 255, message = "Image Url must be 255 characters or less.")
	String imageUrl
) {
}
