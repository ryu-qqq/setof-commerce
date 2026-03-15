package com.ryuqq.setof.application.productgroup.dto.composite;

import java.time.Instant;
import java.util.List;

/**
 * 상품 그룹 목록용 Composite 결과 DTO.
 *
 * <p>ProductGroup + Brand + Category + Seller 크로스 도메인 JOIN 결과와 가격 정보, 배치 enrichment 데이터를 포함합니다.
 *
 * <p>Adapter 레이어에서 {@link #ofBase} 팩터리로 기본 데이터를 생성하고, ReadFacade에서 {@link #withEnrichment}로 가격/옵션
 * enrichment를 적용합니다.
 */
public record ProductGroupListCompositeResult(
        Long id,
        Long sellerId,
        String sellerName,
        Long brandId,
        String brandName,
        String displayKoreanName,
        String displayEnglishName,
        String brandIconImageUrl,
        Long categoryId,
        String categoryName,
        String categoryPath,
        int categoryDepth,
        String productGroupName,
        String optionType,
        String status,
        boolean soldOut,
        boolean displayed,
        String thumbnailUrl,
        int productCount,
        int regularPrice,
        int currentPrice,
        int salePrice,
        int discountRate,
        int minPrice,
        int maxPrice,
        int maxDiscountRate,
        List<OptionGroupSummaryResult> optionGroups,
        Instant createdAt,
        Instant updatedAt) {

    public ProductGroupListCompositeResult {
        optionGroups = optionGroups != null ? List.copyOf(optionGroups) : List.of();
    }

    /** 즉시할인가 (currentPrice - salePrice). salePrice가 currentPrice 이상이면 0. */
    public int directDiscountPrice() {
        if (salePrice >= currentPrice) {
            return 0;
        }
        return currentPrice - salePrice;
    }

    /** 즉시할인율 (currentPrice 대비). currentPrice가 0이면 0. */
    public int directDiscountRate() {
        if (currentPrice <= 0) {
            return 0;
        }
        return directDiscountPrice() * 100 / currentPrice;
    }

    /** Adapter 레이어에서 기본 Composition 쿼리 결과를 생성합니다. 가격/옵션 enrichment 필드는 기본값으로 설정됩니다. */
    public static ProductGroupListCompositeResult ofBase(
            Long id,
            Long sellerId,
            String sellerName,
            Long brandId,
            String brandName,
            String displayKoreanName,
            String displayEnglishName,
            String brandIconImageUrl,
            Long categoryId,
            String categoryName,
            String categoryPath,
            int categoryDepth,
            String productGroupName,
            String optionType,
            String status,
            String thumbnailUrl,
            int productCount,
            int regularPrice,
            int currentPrice,
            int salePrice,
            int discountRate,
            Instant createdAt,
            Instant updatedAt) {
        return new ProductGroupListCompositeResult(
                id,
                sellerId,
                sellerName,
                brandId,
                brandName,
                displayKoreanName,
                displayEnglishName,
                brandIconImageUrl,
                categoryId,
                categoryName,
                categoryPath,
                categoryDepth,
                productGroupName,
                optionType,
                status,
                "SOLD_OUT".equals(status),
                "ACTIVE".equals(status),
                thumbnailUrl,
                productCount,
                regularPrice,
                currentPrice,
                salePrice,
                discountRate,
                0,
                0,
                0,
                List.of(),
                createdAt,
                updatedAt);
    }

    /**
     * 가격/옵션 enrichment 데이터를 적용한 새 인스턴스를 생성합니다.
     *
     * @param minPrice 최저 실판매가
     * @param maxPrice 최고 실판매가
     * @param maxDiscountRate 최대 할인율
     * @param optionGroups 옵션 그룹 요약 목록
     */
    public ProductGroupListCompositeResult withEnrichment(
            int minPrice,
            int maxPrice,
            int maxDiscountRate,
            List<OptionGroupSummaryResult> optionGroups) {
        return new ProductGroupListCompositeResult(
                id,
                sellerId,
                sellerName,
                brandId,
                brandName,
                displayKoreanName,
                displayEnglishName,
                brandIconImageUrl,
                categoryId,
                categoryName,
                categoryPath,
                categoryDepth,
                productGroupName,
                optionType,
                status,
                soldOut,
                displayed,
                thumbnailUrl,
                productCount,
                regularPrice,
                currentPrice,
                salePrice,
                discountRate,
                minPrice,
                maxPrice,
                maxDiscountRate,
                optionGroups,
                createdAt,
                updatedAt);
    }
}
