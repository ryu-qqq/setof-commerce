package com.connectly.partnerAdmin.module.coreServer.request.product;

import jakarta.validation.constraints.NotBlank;

public record ProductDetailDescriptionRequestDto(
	@NotBlank(message = "Detail Description cannot be blank.")
	String detailDescription
) {
}
