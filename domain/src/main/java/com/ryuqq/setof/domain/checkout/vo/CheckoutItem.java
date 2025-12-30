package com.ryuqq.setof.domain.checkout.vo;

import com.ryuqq.setof.domain.checkout.exception.InvalidCheckoutItemException;

/**
 * CheckoutItem Value Object
 *
 * <p>결제 세션에 포함된 상품 항목입니다.
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java Record 사용
 *   <li>불변성 보장 - Java Record 특성
 *   <li>Long FK 전략 - JPA 관계 어노테이션 사용 안함
 * </ul>
 *
 * @param productStockId 상품 재고 ID (SKU)
 * @param productId 상품 ID
 * @param sellerId 판매자 ID
 * @param quantity 주문 수량
 * @param unitPrice 개당 가격
 * @param productName 상품명
 * @param productImage 상품 이미지 URL
 * @param optionName 옵션명
 * @param brandName 브랜드명
 * @param sellerName 판매자명
 */
public record CheckoutItem(
        Long productStockId,
        Long productId,
        Long sellerId,
        int quantity,
        CheckoutMoney unitPrice,
        String productName,
        String productImage,
        String optionName,
        String brandName,
        String sellerName) {

    /** Compact Constructor - 검증 로직 */
    public CheckoutItem {
        validatePositive(productStockId, "productStockId");
        validatePositive(productId, "productId");
        validatePositive(sellerId, "sellerId");
        validateQuantity(quantity);
        validateNotNull(unitPrice, "unitPrice");
        validateNotNullOrBlank(productName, "productName");
    }

    /**
     * Static Factory Method - 체크아웃 항목 생성
     *
     * @param productStockId 상품 재고 ID
     * @param productId 상품 ID
     * @param sellerId 판매자 ID
     * @param quantity 주문 수량
     * @param unitPrice 개당 가격
     * @param productName 상품명
     * @param productImage 상품 이미지 URL
     * @param optionName 옵션명
     * @param brandName 브랜드명
     * @param sellerName 판매자명
     * @return CheckoutItem 인스턴스
     */
    public static CheckoutItem of(
            Long productStockId,
            Long productId,
            Long sellerId,
            int quantity,
            CheckoutMoney unitPrice,
            String productName,
            String productImage,
            String optionName,
            String brandName,
            String sellerName) {
        return new CheckoutItem(
                productStockId,
                productId,
                sellerId,
                quantity,
                unitPrice,
                productName,
                productImage,
                optionName,
                brandName,
                sellerName);
    }

    /**
     * 항목 총 금액 계산
     *
     * @return 개당 가격 * 수량
     */
    public CheckoutMoney totalPrice() {
        return unitPrice.multiply(quantity);
    }

    /**
     * 동일 판매자 여부 확인
     *
     * @param otherSellerId 비교할 판매자 ID
     * @return 동일 판매자이면 true
     */
    public boolean isSameSeller(Long otherSellerId) {
        return this.sellerId.equals(otherSellerId);
    }

    private static void validatePositive(Long value, String fieldName) {
        if (value == null || value <= 0) {
            throw new InvalidCheckoutItemException(fieldName + "은(는) 양수여야 합니다");
        }
    }

    private static void validateQuantity(int quantity) {
        if (quantity <= 0) {
            throw new InvalidCheckoutItemException("수량은 1 이상이어야 합니다");
        }
    }

    private static void validateNotNull(Object value, String fieldName) {
        if (value == null) {
            throw new InvalidCheckoutItemException(fieldName + "은(는) 필수입니다");
        }
    }

    private static void validateNotNullOrBlank(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new InvalidCheckoutItemException(fieldName + "은(는) 필수입니다");
        }
    }
}
