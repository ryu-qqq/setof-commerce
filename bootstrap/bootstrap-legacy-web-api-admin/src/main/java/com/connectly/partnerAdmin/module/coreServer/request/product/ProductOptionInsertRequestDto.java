package com.connectly.partnerAdmin.module.coreServer.request.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import com.connectly.partnerAdmin.module.product.enums.option.OptionName;

public record ProductOptionInsertRequestDto(

	@NotNull(message = "Option Name cannot be null.")
	OptionName optionName,
	@NotBlank(message = "Option Value cannot be blank.")
	@Size(max = 100, message = "Option Value must be 100 characters or less.")
	String optionValue
) {
}
