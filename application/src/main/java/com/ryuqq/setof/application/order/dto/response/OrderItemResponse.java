package com.ryuqq.setof.application.order.dto.response;

import java.math.BigDecimal;

/**
 * 주문 상품 응답 DTO
 *
 * @param orderItemId 주문 상품 ID (UUID String)
 * @param productId 상품 ID
 * @param productStockId 상품 재고 ID
 * @param orderedQuantity 주문 수량
 * @param cancelledQuantity 취소 수량
 * @param refundedQuantity 환불 수량
 * @param effectiveQuantity 유효 수량
 * @param unitPrice 개당 가격
 * @param totalPrice 총 가격
 * @param status 상품 상태
 * @param productName 상품명
 * @param productImage 상품 이미지 URL
 * @param optionName 옵션명
 * @param brandName 브랜드명
 * @param sellerName 판매자명
 * @author development-team
 * @since 1.0.0
 */
public record OrderItemResponse(
        String orderItemId,
        Long productId,
        Long productStockId,
        int orderedQuantity,
        int cancelledQuantity,
        int refundedQuantity,
        int effectiveQuantity,
        BigDecimal unitPrice,
        BigDecimal totalPrice,
        String status,
        String productName,
        String productImage,
        String optionName,
        String brandName,
        String sellerName) {}
