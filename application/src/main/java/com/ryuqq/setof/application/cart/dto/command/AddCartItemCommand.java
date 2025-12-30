package com.ryuqq.setof.application.cart.dto.command;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * 장바구니 아이템 추가 Command
 *
 * @param memberId 회원 ID (UUID)
 * @param productStockId 상품 재고 ID (SKU)
 * @param productId 상품 ID
 * @param productGroupId 상품 그룹 ID
 * @param sellerId 판매자 ID
 * @param quantity 수량
 * @param unitPrice 단가
 */
public record AddCartItemCommand(
        UUID memberId,
        Long productStockId,
        Long productId,
        Long productGroupId,
        Long sellerId,
        int quantity,
        BigDecimal unitPrice) {}
