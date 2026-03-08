package com.ryuqq.setof.storage.legacy.composite.web.cart.dto;

/**
 * LegacyWebCartFlatQueryDto - 장바구니 flat 조회용 Projection DTO.
 *
 * <p>GroupBy.transform() 대신 flat 조회 후 Java에서 그룹핑하기 위한 DTO. Hibernate 6 호환성
 * 문제(ScrollableResults.get) 회피.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyWebCartFlatQueryDto(
        long cartId,
        long brandId,
        String brandName,
        long productGroupId,
        String productGroupName,
        long sellerId,
        String sellerName,
        long productId,
        int regularPrice,
        int currentPrice,
        int salePrice,
        int directDiscountRate,
        int directDiscountPrice,
        int discountRate,
        int quantity,
        int stockQuantity,
        String imageUrl,
        String soldOutYn,
        String displayYn,
        String categoryPath,
        Long optionGroupId,
        Long optionDetailId,
        String optionName,
        String optionValue) {}
