package com.ryuqq.setof.adapter.out.persistence.composite.content.dto;

import java.time.Instant;

/**
 * FixedProductThumbnailDto - FIXED 상품 JOIN 조회 결과 DTO.
 *
 * <p>component_fixed_product LEFT JOIN product_groups LEFT JOIN brand 결과를 담는다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public record FixedProductThumbnailDto(
        long componentId,
        Long tabId,
        long productGroupId,
        String displayName,
        String displayImageUrl,
        long sellerId,
        String productGroupName,
        long brandId,
        String brandName,
        int regularPrice,
        int currentPrice,
        int salePrice,
        Instant createdAt,
        String imageUrl) {}
