package com.ryuqq.setof.domain.order.vo;

import com.ryuqq.setof.domain.order.exception.InvalidProductSnapshotException;

/**
 * ProductSnapshot Value Object
 *
 * <p>주문 시점의 상품 정보 스냅샷입니다. 주문 후 원본 상품 정보가 변경되어도 주문 상품 정보는 보존됩니다.
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java Record 사용
 *   <li>불변성 보장 - Java Record 특성
 * </ul>
 *
 * @param productName 상품명
 * @param productImage 상품 이미지 URL
 * @param optionName 옵션명 (예: "사이즈: L, 색상: 빨강")
 * @param brandName 브랜드명
 * @param sellerName 판매자명
 * @param originalPrice 원래 가격
 */
public record ProductSnapshot(
        String productName,
        String productImage,
        String optionName,
        String brandName,
        String sellerName,
        OrderMoney originalPrice) {

    /** Compact Constructor - 검증 로직 */
    public ProductSnapshot {
        validateRequired(productName, "상품명");
        validateNotNull(originalPrice, "원래 가격");
    }

    /**
     * Static Factory Method - 상품 스냅샷 생성
     *
     * @param productName 상품명
     * @param productImage 상품 이미지 URL
     * @param optionName 옵션명
     * @param brandName 브랜드명
     * @param sellerName 판매자명
     * @param originalPrice 원래 가격
     * @return ProductSnapshot 인스턴스
     */
    public static ProductSnapshot of(
            String productName,
            String productImage,
            String optionName,
            String brandName,
            String sellerName,
            OrderMoney originalPrice) {
        return new ProductSnapshot(
                productName,
                productImage != null ? productImage : "",
                optionName != null ? optionName : "",
                brandName != null ? brandName : "",
                sellerName != null ? sellerName : "",
                originalPrice);
    }

    /**
     * 표시용 상품명 (브랜드 포함)
     *
     * @return [브랜드명] 상품명 형태
     */
    public String displayName() {
        if (brandName == null || brandName.isBlank()) {
            return productName;
        }
        return "[" + brandName + "] " + productName;
    }

    /**
     * 옵션 포함 상품명
     *
     * @return 상품명 (옵션명) 형태
     */
    public String nameWithOption() {
        if (optionName == null || optionName.isBlank()) {
            return productName;
        }
        return productName + " (" + optionName + ")";
    }

    private static void validateRequired(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new InvalidProductSnapshotException(fieldName + "은(는) 필수입니다");
        }
    }

    private static void validateNotNull(Object value, String fieldName) {
        if (value == null) {
            throw new InvalidProductSnapshotException(fieldName + "은(는) 필수입니다");
        }
    }
}
