package com.ryuqq.setof.domain.productgroup.vo;

/** 상품 그룹 상태. */
public enum ProductGroupStatus {
    ACTIVE("판매중"),
    SOLD_OUT("품절"),
    DELETED("삭제");

    private final String displayName;

    ProductGroupStatus(String displayName) {
        this.displayName = displayName;
    }

    public String displayName() {
        return displayName;
    }

    public boolean isActive() {
        return this == ACTIVE;
    }

    public boolean isDeleted() {
        return this == DELETED;
    }

    public boolean isSoldOut() {
        return this == SOLD_OUT;
    }

    /** ACTIVE로 전환 가능한지 확인. DELETED가 아니면 가능. */
    public boolean canActivate() {
        return this != DELETED;
    }

    /** 품절 처리 가능한지 확인. ACTIVE에서만 가능. */
    public boolean canMarkSoldOut() {
        return this == ACTIVE;
    }

    /** 삭제 가능한지 확인. 이미 삭제된 상태가 아니면 가능. */
    public boolean canDelete() {
        return this != DELETED;
    }

    /** 노출 가능 상태 (ACTIVE). */
    public boolean isDisplayed() {
        return this == ACTIVE;
    }

    /**
     * 레거시 플래그 조합에서 상태를 복원합니다.
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
            return SOLD_OUT;
        }
        return ACTIVE;
    }
}
