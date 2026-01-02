package com.connectly.partnerAdmin.module.coreServer.request;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.Valid;

public record ProductInsertRequestDto(
	Long productId,
	boolean soldOut,
	boolean displayed,
	int quantity,
	BigDecimal additionalPrice,

	@Valid
	List<ProductOptionInsertRequestDto> options
) {
}
