package com.ryuqq.setof.application.checkout.dto.command;

import java.math.BigDecimal;

/**
 * 체크아웃 아이템 생성 Command
 *
 * @param productStockId 상품 재고 ID
 * @param productId 상품 ID
 * @param sellerId 판매자 ID
 * @param quantity 수량
 * @param unitPrice 단가
 * @param productName 상품명
 * @param productImage 상품 이미지 URL
 * @param optionName 옵션명
 * @param brandName 브랜드명
 * @param sellerName 판매자명
 * @author development-team
 * @since 1.0.0
 */
public record CreateCheckoutItemCommand(
        Long productStockId,
        Long productId,
        Long sellerId,
        int quantity,
        BigDecimal unitPrice,
        String productName,
        String productImage,
        String optionName,
        String brandName,
        String sellerName) {

    public CreateCheckoutItemCommand {
        if (productStockId == null || productStockId <= 0) {
            throw new IllegalArgumentException("productStockId는 필수입니다");
        }
        if (productId == null || productId <= 0) {
            throw new IllegalArgumentException("productId는 필수입니다");
        }
        if (sellerId == null || sellerId <= 0) {
            throw new IllegalArgumentException("sellerId는 필수입니다");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("수량은 1 이상이어야 합니다");
        }
        if (unitPrice == null || unitPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("단가는 0보다 커야 합니다");
        }
        if (productName == null || productName.isBlank()) {
            throw new IllegalArgumentException("상품명은 필수입니다");
        }
    }
}
