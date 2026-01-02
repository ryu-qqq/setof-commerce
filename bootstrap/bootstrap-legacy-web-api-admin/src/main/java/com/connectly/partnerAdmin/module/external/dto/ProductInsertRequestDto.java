package com.connectly.partnerAdmin.module.external.dto;

import java.math.BigDecimal;
import java.util.List;

public record ProductInsertRequestDto(
        boolean soldOutYn,
        boolean displayYn,
        int quantity,
        BigDecimal additionalPrice,
        List<ProductOptionInsertRequestDto> options
) {
}
