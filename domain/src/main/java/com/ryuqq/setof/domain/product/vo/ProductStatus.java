package com.ryuqq.setof.domain.product.vo;

/**
 * 상품(SKU) 상태 Value Object
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java Record 사용
 *   <li>불변성 보장 - Java Record 특성
 * </ul>
 *
 * @param soldOut 품절 여부
 * @param displayYn 노출 여부
 */
public record ProductStatus(boolean soldOut, boolean displayYn) {

    /**
     * Static Factory Method - 상태 생성
     *
     * @param soldOut 품절 여부
     * @param displayYn 노출 여부
     * @return ProductStatus 인스턴스
     */
    public static ProductStatus of(boolean soldOut, boolean displayYn) {
        return new ProductStatus(soldOut, displayYn);
    }

    /**
     * Static Factory Method - 기본 상태 (재고 있음, 노출)
     *
     * @return 기본 ProductStatus 인스턴스
     */
    public static ProductStatus defaultStatus() {
        return new ProductStatus(false, true);
    }

    /**
     * Static Factory Method - 품절 상태
     *
     * @return 품절 ProductStatus 인스턴스
     */
    public static ProductStatus ofSoldOut() {
        return new ProductStatus(true, true);
    }

    /**
     * Static Factory Method - 숨김 상태
     *
     * @return 숨김 ProductStatus 인스턴스
     */
    public static ProductStatus hidden() {
        return new ProductStatus(false, false);
    }

    /**
     * 판매 가능 여부 확인
     *
     * @return 품절이 아니고 노출 중이면 true
     */
    public boolean isSellable() {
        return !soldOut && displayYn;
    }

    /**
     * 노출 여부 확인
     *
     * @return 노출 중이면 true
     */
    public boolean isDisplayed() {
        return displayYn;
    }

    /**
     * 품절 상태로 변경
     *
     * @return 품절 상태의 새로운 ProductStatus 인스턴스
     */
    public ProductStatus markSoldOut() {
        return new ProductStatus(true, this.displayYn);
    }

    /**
     * 재고 복원 상태로 변경
     *
     * @return 재고 있음 상태의 새로운 ProductStatus 인스턴스
     */
    public ProductStatus markInStock() {
        return new ProductStatus(false, this.displayYn);
    }

    /**
     * 노출 상태로 변경
     *
     * @return 노출 상태의 새로운 ProductStatus 인스턴스
     */
    public ProductStatus show() {
        return new ProductStatus(this.soldOut, true);
    }

    /**
     * 숨김 상태로 변경
     *
     * @return 숨김 상태의 새로운 ProductStatus 인스턴스
     */
    public ProductStatus hide() {
        return new ProductStatus(this.soldOut, false);
    }
}
