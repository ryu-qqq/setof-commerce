package com.ryuqq.setof.application.product.dto.command;

import java.util.List;

public record ProductDiffUpdateEntry(
        Long productId,
        String skuCode,
        int regularPrice,
        int currentPrice,
        int stockQuantity,
        int sortOrder,
        List<SelectedOption> selectedOptions) {

    public ProductDiffUpdateEntry {
        selectedOptions = List.copyOf(selectedOptions);
    }
}
