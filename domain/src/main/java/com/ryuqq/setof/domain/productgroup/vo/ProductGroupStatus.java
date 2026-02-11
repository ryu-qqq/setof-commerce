package com.ryuqq.setof.domain.productgroup.vo;

/**
 * 상품그룹 상태.
 *
 * <p>상태 전이 규칙:
 *
 * <ul>
 *   <li>DRAFT → ACTIVE (활성화)
 *   <li>ACTIVE → INACTIVE (비활성화)
 *   <li>ACTIVE → SOLDOUT (품절)
 *   <li>INACTIVE → ACTIVE (재활성화)
 *   <li>SOLDOUT → ACTIVE (재입고)
 *   <li>* → DELETED (삭제, 어떤 상태에서든 가능)
 * </ul>
 */
public enum ProductGroupStatus {

    /** 초안 (신규 생성 시 기본 상태) */
    DRAFT,

    /** 활성 (판매 중) */
    ACTIVE,

    /** 비활성 (판매 중지) */
    INACTIVE,

    /** 품절 */
    SOLDOUT,

    /** 삭제됨 */
    DELETED;

    public boolean canActivate() {
        return this == DRAFT || this == INACTIVE || this == SOLDOUT;
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

    public boolean isDisplayed() {
        return this == ACTIVE;
    }

    public boolean isSoldOut() {
        return this == SOLDOUT;
    }

    public boolean isDeleted() {
        return this == DELETED;
    }

    /**
     * 레거시 플래그 조합에서 상태를 복원합니다.
     *
     * <p>매핑 로직:
     *
     * <ul>
     *   <li>delete_yn=Y → DELETED
     *   <li>sold_out_yn=Y → SOLDOUT
     *   <li>display_yn=N → INACTIVE
     *   <li>그 외 → ACTIVE
     * </ul>
     *
     * @param deleteYn 삭제 여부 (Y/N)
     * @param soldOutYn 품절 여부 (Y/N)
     * @param displayYn 노출 여부 (Y/N)
     * @return ProductGroupStatus
     */
    public static ProductGroupStatus fromLegacyFlags(
            String deleteYn, String soldOutYn, String displayYn) {
        if ("Y".equalsIgnoreCase(deleteYn)) {
            return DELETED;
        }
        if ("Y".equalsIgnoreCase(soldOutYn)) {
            return SOLDOUT;
        }
        if ("N".equalsIgnoreCase(displayYn)) {
            return INACTIVE;
        }
        return ACTIVE;
    }
}
