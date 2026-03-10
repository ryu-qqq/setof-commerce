package com.ryuqq.setof.application.product.dto.command;

import java.util.List;

public record UpdateProductsCommand(
        long productGroupId, List<OptionGroupData> optionGroups, List<ProductData> products) {

    public record OptionGroupData(
            Long sellerOptionGroupId,
            String optionGroupName,
            int sortOrder,
            List<OptionValueData> optionValues) {}

    public record OptionValueData(
            Long sellerOptionValueId, String optionValueName, int sortOrder) {}

    public record ProductData(
            Long productId,
            String skuCode,
            int regularPrice,
            int currentPrice,
            int stockQuantity,
            int sortOrder,
            List<SelectedOption> selectedOptions) {}
}
