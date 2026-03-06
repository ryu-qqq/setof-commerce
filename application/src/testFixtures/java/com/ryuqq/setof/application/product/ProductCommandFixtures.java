package com.ryuqq.setof.application.product;

import com.ryuqq.setof.application.product.dto.command.ProductDiffUpdateEntry;
import com.ryuqq.setof.application.product.dto.command.RegisterProductsCommand;
import com.ryuqq.setof.application.product.dto.command.SelectedOption;
import com.ryuqq.setof.application.product.dto.command.UpdateProductPriceCommand;
import com.ryuqq.setof.application.product.dto.command.UpdateProductStockCommand;
import com.ryuqq.setof.application.product.dto.command.UpdateProductsCommand;
import java.util.List;

/**
 * Product Application Command 테스트 Fixtures.
 *
 * <p>Product 관련 Command 객체들을 생성하는 테스트 유틸리티입니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class ProductCommandFixtures {

    private ProductCommandFixtures() {}

    // ===== 상수 =====
    public static final long DEFAULT_PRODUCT_ID = 1L;
    public static final long DEFAULT_PRODUCT_GROUP_ID = 1L;
    public static final int DEFAULT_REGULAR_PRICE = 10000;
    public static final int DEFAULT_CURRENT_PRICE = 9000;
    public static final int DEFAULT_STOCK_QUANTITY = 100;
    public static final int DEFAULT_SORT_ORDER = 0;

    // ===== UpdateProductPriceCommand =====

    public static UpdateProductPriceCommand updateProductPriceCommand() {
        return new UpdateProductPriceCommand(
                DEFAULT_PRODUCT_ID, DEFAULT_REGULAR_PRICE, DEFAULT_CURRENT_PRICE);
    }

    public static UpdateProductPriceCommand updateProductPriceCommand(long productId) {
        return new UpdateProductPriceCommand(
                productId, DEFAULT_REGULAR_PRICE, DEFAULT_CURRENT_PRICE);
    }

    public static UpdateProductPriceCommand updateProductPriceCommand(
            long productId, int regularPrice, int currentPrice) {
        return new UpdateProductPriceCommand(productId, regularPrice, currentPrice);
    }

    // ===== UpdateProductStockCommand =====

    public static UpdateProductStockCommand updateProductStockCommand() {
        return new UpdateProductStockCommand(DEFAULT_PRODUCT_ID, DEFAULT_STOCK_QUANTITY);
    }

    public static UpdateProductStockCommand updateProductStockCommand(long productId) {
        return new UpdateProductStockCommand(productId, DEFAULT_STOCK_QUANTITY);
    }

    public static UpdateProductStockCommand updateProductStockCommand(
            long productId, int stockQuantity) {
        return new UpdateProductStockCommand(productId, stockQuantity);
    }

    // ===== UpdateProductsCommand =====

    public static UpdateProductsCommand updateProductsCommand() {
        return new UpdateProductsCommand(
                DEFAULT_PRODUCT_GROUP_ID,
                List.of(optionGroupData()),
                List.of(productDataExisting(), productDataNew()));
    }

    public static UpdateProductsCommand updateProductsCommand(long productGroupId) {
        return new UpdateProductsCommand(
                productGroupId, List.of(optionGroupData()), List.of(productDataExisting()));
    }

    public static UpdateProductsCommand.ProductData productDataExisting() {
        return new UpdateProductsCommand.ProductData(
                DEFAULT_PRODUCT_ID,
                "SKU-001",
                DEFAULT_REGULAR_PRICE,
                DEFAULT_CURRENT_PRICE,
                DEFAULT_STOCK_QUANTITY,
                DEFAULT_SORT_ORDER,
                List.of(selectedOption()));
    }

    public static UpdateProductsCommand.ProductData productDataNew() {
        return new UpdateProductsCommand.ProductData(
                null,
                "SKU-002",
                DEFAULT_REGULAR_PRICE,
                DEFAULT_CURRENT_PRICE,
                DEFAULT_STOCK_QUANTITY,
                1,
                List.of(selectedOption("색상", "블루")));
    }

    public static UpdateProductsCommand.OptionGroupData optionGroupData() {
        return new UpdateProductsCommand.OptionGroupData(
                1L,
                "색상",
                100L,
                "PREDEFINED",
                List.of(optionValueData("레드"), optionValueData("블루")));
    }

    public static UpdateProductsCommand.OptionValueData optionValueData(String name) {
        return new UpdateProductsCommand.OptionValueData(1L, name, 200L, 0);
    }

    // ===== RegisterProductsCommand =====

    public static RegisterProductsCommand registerProductsCommand() {
        return new RegisterProductsCommand(
                DEFAULT_PRODUCT_GROUP_ID, List.of(registerProductData(), registerProductData()));
    }

    public static RegisterProductsCommand registerProductsCommand(long productGroupId) {
        return new RegisterProductsCommand(productGroupId, List.of(registerProductData()));
    }

    public static RegisterProductsCommand.ProductData registerProductData() {
        return new RegisterProductsCommand.ProductData(
                0, DEFAULT_STOCK_QUANTITY, DEFAULT_SORT_ORDER, List.of(selectedOption()));
    }

    // ===== ProductDiffUpdateEntry =====

    public static ProductDiffUpdateEntry diffUpdateEntryExisting() {
        return new ProductDiffUpdateEntry(
                DEFAULT_PRODUCT_ID,
                "SKU-001",
                DEFAULT_REGULAR_PRICE,
                DEFAULT_CURRENT_PRICE,
                DEFAULT_STOCK_QUANTITY,
                DEFAULT_SORT_ORDER,
                List.of(selectedOption()));
    }

    public static ProductDiffUpdateEntry diffUpdateEntryNew() {
        return new ProductDiffUpdateEntry(
                null,
                "SKU-NEW",
                DEFAULT_REGULAR_PRICE,
                DEFAULT_CURRENT_PRICE,
                DEFAULT_STOCK_QUANTITY,
                1,
                List.of(selectedOption("색상", "블루")));
    }

    // ===== SelectedOption =====

    public static SelectedOption selectedOption() {
        return new SelectedOption("색상", "레드");
    }

    public static SelectedOption selectedOption(String groupName, String valueName) {
        return new SelectedOption(groupName, valueName);
    }
}
