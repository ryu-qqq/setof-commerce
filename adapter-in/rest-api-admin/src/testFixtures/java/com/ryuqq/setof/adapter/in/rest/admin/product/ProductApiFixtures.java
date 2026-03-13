package com.ryuqq.setof.adapter.in.rest.admin.product;

import com.ryuqq.setof.adapter.in.rest.admin.v2.product.dto.command.UpdateProductPriceApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.product.dto.command.UpdateProductStockApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.product.dto.command.UpdateProductsApiRequest;
import java.util.List;

/**
 * Product API 테스트 Fixtures.
 *
 * <p>상품(SKU) API 테스트에서 사용되는 Request 객체들을 생성합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class ProductApiFixtures {

    private ProductApiFixtures() {}

    // ===== 기본 상수 =====
    public static final Long DEFAULT_PRODUCT_ID = 1L;
    public static final Long DEFAULT_PRODUCT_GROUP_ID = 10L;
    public static final int DEFAULT_REGULAR_PRICE = 100_000;
    public static final int DEFAULT_CURRENT_PRICE = 90_000;
    public static final int DEFAULT_STOCK_QUANTITY = 100;

    // ===== UpdateProductPriceApiRequest Fixtures =====

    public static UpdateProductPriceApiRequest updatePriceRequest() {
        return new UpdateProductPriceApiRequest(DEFAULT_REGULAR_PRICE, DEFAULT_CURRENT_PRICE);
    }

    public static UpdateProductPriceApiRequest updatePriceRequest(
            int regularPrice, int currentPrice) {
        return new UpdateProductPriceApiRequest(regularPrice, currentPrice);
    }

    // ===== UpdateProductStockApiRequest Fixtures =====

    public static UpdateProductStockApiRequest updateStockRequest() {
        return new UpdateProductStockApiRequest(DEFAULT_STOCK_QUANTITY);
    }

    public static UpdateProductStockApiRequest updateStockRequest(int stockQuantity) {
        return new UpdateProductStockApiRequest(stockQuantity);
    }

    // ===== UpdateProductsApiRequest Fixtures =====

    public static UpdateProductsApiRequest updateProductsRequest() {
        return new UpdateProductsApiRequest(
                List.of(optionGroupRequest()), List.of(productApiRequest()));
    }

    public static UpdateProductsApiRequest updateProductsRequest(
            List<UpdateProductsApiRequest.OptionGroupApiRequest> optionGroups,
            List<UpdateProductsApiRequest.ProductApiRequest> products) {
        return new UpdateProductsApiRequest(optionGroups, products);
    }

    public static UpdateProductsApiRequest.OptionGroupApiRequest optionGroupRequest() {
        return new UpdateProductsApiRequest.OptionGroupApiRequest(
                1L, "색상", 0, List.of(optionValueRequest()));
    }

    public static UpdateProductsApiRequest.OptionGroupApiRequest optionGroupRequest(
            Long sellerOptionGroupId,
            String groupName,
            int sortOrder,
            List<UpdateProductsApiRequest.OptionValueApiRequest> values) {
        return new UpdateProductsApiRequest.OptionGroupApiRequest(
                sellerOptionGroupId, groupName, sortOrder, values);
    }

    public static UpdateProductsApiRequest.OptionValueApiRequest optionValueRequest() {
        return new UpdateProductsApiRequest.OptionValueApiRequest(1L, "빨강", 0);
    }

    public static UpdateProductsApiRequest.OptionValueApiRequest optionValueRequest(
            Long sellerOptionValueId, String valueName, int sortOrder) {
        return new UpdateProductsApiRequest.OptionValueApiRequest(
                sellerOptionValueId, valueName, sortOrder);
    }

    public static UpdateProductsApiRequest.ProductApiRequest productApiRequest() {
        return new UpdateProductsApiRequest.ProductApiRequest(
                DEFAULT_PRODUCT_ID,
                "SKU-001",
                DEFAULT_REGULAR_PRICE,
                DEFAULT_CURRENT_PRICE,
                DEFAULT_STOCK_QUANTITY,
                1,
                List.of(selectedOptionRequest()));
    }

    public static UpdateProductsApiRequest.SelectedOptionApiRequest selectedOptionRequest() {
        return new UpdateProductsApiRequest.SelectedOptionApiRequest("색상", "빨강");
    }

    public static UpdateProductsApiRequest.SelectedOptionApiRequest selectedOptionRequest(
            String groupName, String valueName) {
        return new UpdateProductsApiRequest.SelectedOptionApiRequest(groupName, valueName);
    }
}
