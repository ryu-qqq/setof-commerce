package com.connectly.partnerAdmin.module.coreServer.request;

import com.connectly.partnerAdmin.module.product.enums.option.OptionName;

public record ProductOptionInsertRequestDto(
	OptionName optionName,
	String optionValue
) {
}
