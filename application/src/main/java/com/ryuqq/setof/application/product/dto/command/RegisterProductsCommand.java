package com.ryuqq.setof.application.product.dto.command;

import java.util.List;

public record RegisterProductsCommand(long productGroupId, List<ProductData> products) {

    public record ProductData(
            String skuCode,
            int regularPrice,
            int currentPrice,
            int stockQuantity,
            int sortOrder,
            List<SelectedOption> selectedOptions) {}
}
