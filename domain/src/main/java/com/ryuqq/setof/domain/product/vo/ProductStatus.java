package com.ryuqq.setof.domain.product.vo;

/**
 * 상품(SKU) 상태.
 *
 * <p>상태 전이 규칙:
 *
 * <ul>
 *   <li>ACTIVE → INACTIVE (비활성화)
 *   <li>ACTIVE → SOLD_OUT (품절)
 *   <li>INACTIVE → ACTIVE (재활성화)
 *   <li>SOLD_OUT → ACTIVE (재입고)
 *   <li>* → DELETED (삭제)
 * </ul>
 */
public enum ProductStatus {

    /** 활성 (판매 가능) */
    ACTIVE,

    /** 비활성 (판매 중지) */
    INACTIVE,

    /** 품절 */
    SOLD_OUT,

    /** 삭제됨 */
    DELETED;

    public boolean canActivate() {
        return this == INACTIVE || this == SOLD_OUT;
    }

    public boolean canDeactivate() {
        return this == ACTIVE;
    }

    public boolean canMarkSoldOut() {
        return this == ACTIVE;
    }

    public boolean canDelete() {
        return this != DELETED;
    }

    public boolean isActive() {
        return this == ACTIVE;
    }

    public boolean isSoldOut() {
        return this == SOLD_OUT;
    }

    public boolean isDeleted() {
        return this == DELETED;
    }

    /**
     * 레거시 플래그 조합에서 상태를 복원합니다.
     *
     * @param deleteYn 삭제 여부 (Y/N)
     * @param soldOutYn 품절 여부 (Y/N)
     * @param displayYn 노출 여부 (Y/N)
     * @return ProductStatus
     */
    public static ProductStatus fromLegacyFlags(
            String deleteYn, String soldOutYn, String displayYn) {
        if ("Y".equalsIgnoreCase(deleteYn)) {
            return DELETED;
        }
        if ("Y".equalsIgnoreCase(soldOutYn)) {
            return SOLD_OUT;
        }
        if ("N".equalsIgnoreCase(displayYn)) {
            return INACTIVE;
        }
        return ACTIVE;
    }
}
