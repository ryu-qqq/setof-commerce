package com.connectly.partnerAdmin.module.external.dto;

import com.connectly.partnerAdmin.module.product.enums.option.OptionName;

public record ProductOptionInsertRequestDto(
        OptionName optionName,
        String optionValue
) {}
