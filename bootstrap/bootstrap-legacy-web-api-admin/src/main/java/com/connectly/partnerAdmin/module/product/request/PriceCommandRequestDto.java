package com.connectly.partnerAdmin.module.product.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record PriceCommandRequestDto(
    @NotNull(message = "Product ID cannot be null.")
    long productGroupId,
    @NotNull(message = "Regular Price cannot be null.")
    @DecimalMax(value = "100000000", inclusive = true, message = "Regular Price must be less than or equal to 100,000,000.")
    BigDecimal regularPrice,
    @NotNull(message = "Current Price cannot be null.")
    @DecimalMax(value = "100000000", inclusive = true, message = "Current Price must be less than or equal to 100,000,000.")
    BigDecimal currentPrice
) {
}
