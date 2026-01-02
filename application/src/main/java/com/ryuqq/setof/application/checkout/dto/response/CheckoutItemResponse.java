package com.ryuqq.setof.application.checkout.dto.response;

import java.math.BigDecimal;

/**
 * 체크아웃 아이템 응답 DTO
 *
 * @param productStockId 상품 재고 ID
 * @param productId 상품 ID
 * @param sellerId 판매자 ID
 * @param quantity 수량
 * @param unitPrice 단가
 * @param totalPrice 총 금액
 * @author development-team
 * @since 1.0.0
 */
public record CheckoutItemResponse(
        Long productStockId,
        Long productId,
        Long sellerId,
        int quantity,
        BigDecimal unitPrice,
        BigDecimal totalPrice) {}
