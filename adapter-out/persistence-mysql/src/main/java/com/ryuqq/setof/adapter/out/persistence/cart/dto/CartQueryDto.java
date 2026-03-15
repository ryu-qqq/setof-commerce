package com.ryuqq.setof.adapter.out.persistence.cart.dto;

import java.util.Set;

/**
 * CartQueryDto - 장바구니 조회 결과 DTO (옵션 그룹핑 완료).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public record CartQueryDto(
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
        Set<CartOptionQueryDto> options) {}
