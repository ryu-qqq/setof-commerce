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
 * <p>Product 등록/수정 Command 객체들을 생성하는 테스트 유틸리티입니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class ProductCommandFixtures {

    private ProductCommandFixtures() {}

    // ===== UpdateProductPriceCommand =====

    public static UpdateProductPriceCommand updatePriceCommand(long productId) {
        return new UpdateProductPriceCommand(productId, 50000, 45000);
    }

    public static UpdateProductPriceCommand updatePriceCommand(
            long productId, int regularPrice, int currentPrice) {
        return new UpdateProductPriceCommand(productId, regularPrice, currentPrice);
    }

    // ===== UpdateProductStockCommand =====

    public static UpdateProductStockCommand updateStockCommand(long productId) {
        return new UpdateProductStockCommand(productId, 20);
    }

    public static UpdateProductStockCommand updateStockCommand(long productId, int stockQuantity) {
        return new UpdateProductStockCommand(productId, stockQuantity);
    }

    // ===== UpdateProductsCommand =====

    public static UpdateProductsCommand updateProductsCommand(long productGroupId) {
        return new UpdateProductsCommand(
                productGroupId,
                List.of(
                        new UpdateProductsCommand.OptionGroupData(
                                10L,
                                "색상",
                                1,
                                List.of(
                                        new UpdateProductsCommand.OptionValueData(100L, "블랙", 1),
                                        new UpdateProductsCommand.OptionValueData(
                                                101L, "화이트", 2)))),
                List.of(
                        new UpdateProductsCommand.ProductData(
                                1L,
                                "SKU-001",
                                50000,
                                45000,
                                10,
                                1,
                                List.of(new SelectedOption("색상", "블랙")))));
    }

    // ===== RegisterProductsCommand =====

    public static RegisterProductsCommand.ProductData productData(
            String skuCode, String optionGroupName, String optionValueName) {
        return new RegisterProductsCommand.ProductData(
                null,
                skuCode,
                50000,
                45000,
                10,
                1,
                List.of(new SelectedOption(optionGroupName, optionValueName)));
    }

    // ===== ProductDiffUpdateEntry =====

    public static ProductDiffUpdateEntry existingProductEntry(Long productId) {
        return new ProductDiffUpdateEntry(
                productId, "SKU-001", 50000, 45000, 10, 1, List.of(new SelectedOption("색상", "블랙")));
    }

    public static ProductDiffUpdateEntry newProductEntry() {
        return new ProductDiffUpdateEntry(
                null, "SKU-NEW-001", 50000, 45000, 5, 2, List.of(new SelectedOption("색상", "화이트")));
    }

    public static List<ProductDiffUpdateEntry> defaultEntries(Long existingProductId) {
        return List.of(existingProductEntry(existingProductId));
    }
}
