package com.ryuqq.setof.domain.claim.vo;

/**
 * ClaimStatus - 클레임 상태
 *
 * <p>클레임의 처리 상태를 나타냅니다.
 *
 * <p>상태 흐름:
 *
 * <pre>
 * REQUESTED → APPROVED → IN_PROGRESS → COMPLETED
 *     │
 *     └→ REJECTED
 *
 * REQUESTED, APPROVED 상태에서 고객이 CANCELLED 가능
 * </pre>
 *
 * @author development-team
 * @since 2.0.0
 */
public enum ClaimStatus {

    /** 클레임 요청됨 (처리 대기) */
    REQUESTED("요청됨"),

    /** 클레임 승인됨 (처리 예정) */
    APPROVED("승인됨"),

    /** 클레임 반려됨 */
    REJECTED("반려됨"),

    /** 처리 진행 중 (수거 중, 검수 중 등) */
    IN_PROGRESS("처리 중"),

    /** 처리 완료 (환불/교환 완료) */
    COMPLETED("완료됨"),

    /** 고객 취소 */
    CANCELLED("취소됨");

    private final String description;

    ClaimStatus(String description) {
        this.description = description;
    }

    /**
     * 상태 설명 반환
     *
     * @return 상태 설명
     */
    public String description() {
        return description;
    }

    /**
     * 기본 상태 반환
     *
     * @return REQUESTED
     */
    public static ClaimStatus defaultStatus() {
        return REQUESTED;
    }

    /**
     * 승인 가능 여부
     *
     * @return REQUESTED 상태이면 true
     */
    public boolean canApprove() {
        return this == REQUESTED;
    }

    /**
     * 반려 가능 여부
     *
     * @return REQUESTED 상태이면 true
     */
    public boolean canReject() {
        return this == REQUESTED;
    }

    /**
     * 처리 시작 가능 여부
     *
     * @return APPROVED 상태이면 true
     */
    public boolean canStartProcessing() {
        return this == APPROVED;
    }

    /**
     * 완료 처리 가능 여부
     *
     * @return IN_PROGRESS 또는 APPROVED 상태이면 true
     */
    public boolean canComplete() {
        return this == IN_PROGRESS || this == APPROVED;
    }

    /**
     * 고객 취소 가능 여부
     *
     * @return REQUESTED, APPROVED 상태이면 true
     */
    public boolean canCancel() {
        return this == REQUESTED || this == APPROVED;
    }

    /**
     * 최종 상태 여부 (더 이상 변경 불가)
     *
     * @return COMPLETED, REJECTED, CANCELLED이면 true
     */
    public boolean isFinal() {
        return this == COMPLETED || this == REJECTED || this == CANCELLED;
    }

    /**
     * 활성 상태 여부 (처리 중)
     *
     * @return REQUESTED, APPROVED, IN_PROGRESS이면 true
     */
    public boolean isActive() {
        return this == REQUESTED || this == APPROVED || this == IN_PROGRESS;
    }
}
