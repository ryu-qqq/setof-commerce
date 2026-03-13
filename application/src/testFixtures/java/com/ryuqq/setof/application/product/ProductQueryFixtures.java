package com.ryuqq.setof.application.product;

import com.ryuqq.setof.application.product.dto.response.ProductOptionMappingResult;
import com.ryuqq.setof.application.product.dto.response.ProductResult;
import java.time.Instant;
import java.util.List;

/**
 * Product Application Query 테스트 Fixtures.
 *
 * <p>Product 조회 관련 Result 객체들을 생성하는 테스트 유틸리티입니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class ProductQueryFixtures {

    private ProductQueryFixtures() {}

    private static final Instant FIXED_NOW = Instant.parse("2024-01-01T00:00:00Z");

    // ===== ProductOptionMappingResult =====

    public static ProductOptionMappingResult optionMappingResult(Long id, Long productId) {
        return ProductOptionMappingResult.withOptionNames(id, productId, 100L, "색상", "블랙");
    }

    public static ProductOptionMappingResult optionMappingResultWithNames(
            Long id,
            Long productId,
            Long sellerOptionValueId,
            String optionGroupName,
            String optionValueName) {
        return ProductOptionMappingResult.withOptionNames(
                id, productId, sellerOptionValueId, optionGroupName, optionValueName);
    }

    public static List<ProductOptionMappingResult> optionMappingResults(Long productId) {
        return List.of(
                optionMappingResult(1L, productId),
                ProductOptionMappingResult.withOptionNames(2L, productId, 101L, "사이즈", "M"));
    }

    // ===== ProductResult =====

    public static ProductResult productResult(Long id, Long productGroupId) {
        return new ProductResult(
                id,
                productGroupId,
                "SKU-" + id,
                30000,
                25000,
                null,
                17,
                10,
                "ACTIVE",
                1,
                optionMappingResults(id),
                FIXED_NOW,
                FIXED_NOW);
    }

    public static ProductResult productResultWithSalePrice(Long id, Long productGroupId) {
        return new ProductResult(
                id,
                productGroupId,
                "SKU-SALE-" + id,
                30000,
                25000,
                20000,
                33,
                5,
                "ACTIVE",
                1,
                optionMappingResults(id),
                FIXED_NOW,
                FIXED_NOW);
    }

    public static ProductResult soldOutProductResult(Long id, Long productGroupId) {
        return new ProductResult(
                id,
                productGroupId,
                "SKU-OUT-" + id,
                30000,
                25000,
                null,
                17,
                0,
                "SOLD_OUT",
                2,
                List.of(),
                FIXED_NOW,
                FIXED_NOW);
    }

    public static List<ProductResult> productResults(Long productGroupId) {
        return List.of(productResult(1L, productGroupId), productResult(2L, productGroupId));
    }
}
