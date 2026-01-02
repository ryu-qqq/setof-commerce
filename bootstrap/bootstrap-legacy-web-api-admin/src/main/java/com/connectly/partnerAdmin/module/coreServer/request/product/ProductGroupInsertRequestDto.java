package com.connectly.partnerAdmin.module.coreServer.request.product;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

import com.connectly.partnerAdmin.module.product.enums.group.ManagementType;
import com.connectly.partnerAdmin.module.product.enums.group.ProductCondition;
import com.connectly.partnerAdmin.module.product.enums.option.OptionType;

public record ProductGroupInsertRequestDto(

	Long productGroupId,

	@NotNull(message = "Brand ID cannot be null.")
	@Positive(message = "Brand ID must be a positive number.")
	long brandId,

	@NotNull(message = "Category ID cannot be null.")
	@Positive(message = "Category ID must be a positive number.")
	long categoryId,

	@NotNull(message = "Seller ID cannot be null.")
	@Positive(message = "Seller ID must be a positive number.")
	long sellerId,

	@NotBlank(message = "Product Group Name cannot be blank.")
	@Size(max = 250, message = "Product Group Name must be 100 characters or less.")
	String productGroupName,

	@Size(max = 50, message = "Style Code must be 50 characters or less.")
	String styleCode,

	@NotNull(message = "Product Condition Type cannot be null.")
	ProductCondition productCondition,

	@NotNull(message = "Management Type cannot be null.")
	ManagementType managementType,

	@NotNull(message = "Option Type cannot be null.")
	OptionType optionType,

	@NotNull(message = "Regular Price cannot be null.")
	@DecimalMax(value = "100000000", inclusive = true, message = "Regular Price must be less than or equal to 100,000,000.")
	BigDecimal regularPrice,

	@NotNull(message = "Current Price cannot be null.")
	@DecimalMax(value = "100000000", inclusive = true, message = "Current Price must be less than or equal to 100,000,000.")
	BigDecimal currentPrice,

	boolean soldOut,
	boolean displayed,
	String keywords

) {
}
