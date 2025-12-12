package com.ryuqq.setof.domain.seller.vo;

/**
 * 셀러 승인 상태 Enum
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java Enum 사용
 *   <li>불변성 보장 - Enum 특성
 * </ul>
 */
public enum ApprovalStatus {

    /** 승인 대기 */
    PENDING("승인 대기"),

    /** 승인 완료 */
    APPROVED("승인 완료"),

    /** 승인 거부 */
    REJECTED("승인 거부"),

    /** 활동 정지 */
    SUSPENDED("활동 정지");

    private final String description;

    ApprovalStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    /**
     * 활성 상태 여부 확인
     *
     * @return APPROVED이면 true
     */
    public boolean isActive() {
        return this == APPROVED;
    }

    /**
     * 승인 가능 여부 확인
     *
     * @return PENDING이면 true
     */
    public boolean canBeApproved() {
        return this == PENDING;
    }

    /**
     * 정지 가능 여부 확인
     *
     * @return APPROVED이면 true
     */
    public boolean canBeSuspended() {
        return this == APPROVED;
    }
}
