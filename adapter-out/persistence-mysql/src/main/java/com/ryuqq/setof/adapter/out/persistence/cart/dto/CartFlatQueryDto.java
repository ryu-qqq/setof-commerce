package com.ryuqq.setof.adapter.out.persistence.cart.dto;

/**
 * CartFlatQueryDto - 장바구니 flat 조회용 Projection DTO.
 *
 * <p>Flat 조회 후 Java에서 cartId 기준 그룹핑합니다. Hibernate 6 호환성 문제(ScrollableResults.get) 회피.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public record CartFlatQueryDto(
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
        int discountRate,
        int quantity,
        int stockQuantity,
        String imageUrl,
        String productStatus,
        String categoryPath,
        Long optionGroupId,
        Long optionValueId,
        String optionGroupName,
        String optionValueName) {}
