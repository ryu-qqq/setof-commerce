package com.connectly.partnerAdmin.module.external.dto;


import com.connectly.partnerAdmin.module.product.enums.group.ManagementType;
import com.connectly.partnerAdmin.module.product.enums.group.ProductCondition;
import com.connectly.partnerAdmin.module.product.enums.option.OptionType;

import java.math.BigDecimal;

public record ProductGroupInsertRequestDto(
        Long setofProductGroupId,
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
        boolean soldOutYn,
        boolean displayYn,
        String keywords
        ) {
}
