package com.connectly.partnerAdmin.module.coreServer.request;

import java.math.BigDecimal;

import com.connectly.partnerAdmin.module.product.enums.group.ManagementType;
import com.connectly.partnerAdmin.module.product.enums.group.ProductCondition;
import com.connectly.partnerAdmin.module.product.enums.option.OptionType;

public record ProductGroupInsertRequestDto(

	Long productGroupId,
	long brandId,
	long categoryId,
	long sellerId,
	String productGroupName,
	String styleCode,
	ProductCondition productCondition,
	ManagementType managementType,
	OptionType optionType,
	BigDecimal regularPrice,
	BigDecimal currentPrice,
	boolean soldOut,
	boolean displayed,
	String keywords

) {
}
