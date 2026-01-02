package com.ryuqq.setof.application.cart.dto.response;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * 장바구니 아이템 응답 DTO
 *
 * @param cartItemId 장바구니 아이템 ID
 * @param productStockId 상품 재고 ID (SKU)
 * @param productId 상품 ID
 * @param productGroupId 상품 그룹 ID
 * @param sellerId 판매자 ID
 * @param quantity 수량
 * @param unitPrice 단가
 * @param totalPrice 총 가격 (수량 * 단가)
 * @param selected 선택 여부
 * @param addedAt 추가 시각
 */
public record CartItemResponse(
        Long cartItemId,
        Long productStockId,
        Long productId,
        Long productGroupId,
        Long sellerId,
        int quantity,
        BigDecimal unitPrice,
        BigDecimal totalPrice,
        boolean selected,
        Instant addedAt) {}
