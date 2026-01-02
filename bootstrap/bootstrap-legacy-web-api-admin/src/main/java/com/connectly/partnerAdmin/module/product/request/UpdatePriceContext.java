package com.connectly.partnerAdmin.module.product.request;

import java.util.List;

public record UpdatePriceContext(
    List<PriceCommandRequestDto> priceCommands
) {
}
