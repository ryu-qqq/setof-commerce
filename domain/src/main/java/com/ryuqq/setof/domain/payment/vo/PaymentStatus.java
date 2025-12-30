package com.ryuqq.setof.domain.payment.vo;

/**
 * PaymentStatus - 결제 상태
 *
 * <p>결제의 생명주기를 나타내는 상태값입니다.
 *
 * <p>상태 흐름:
 *
 * <pre>
 * PENDING → APPROVED → PARTIAL_REFUNDED → FULLY_REFUNDED
 *    │          │
 *    └→ FAILED  └→ CANCELLED
 * </pre>
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java enum 사용
 *   <li>상태 전이 검증 메서드 제공
 * </ul>
 */
public enum PaymentStatus {

    /** 결제 대기 (PG 요청 전) */
    PENDING("결제 대기"),

    /** 결제 승인 완료 */
    APPROVED("결제 승인"),

    /** 부분 환불 (일부 금액 환불됨) */
    PARTIAL_REFUNDED("부분 환불"),

    /** 전액 환불 */
    FULLY_REFUNDED("전액 환불"),

    /** 결제 취소 */
    CANCELLED("결제 취소"),

    /** 결제 실패 */
    FAILED("결제 실패");

    private final String description;

    PaymentStatus(String description) {
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
     * @return PENDING 상태
     */
    public static PaymentStatus defaultStatus() {
        return PENDING;
    }

    /**
     * 승인 가능 여부
     *
     * @return PENDING 상태이면 true
     */
    public boolean canApprove() {
        return this == PENDING;
    }

    /**
     * 환불 가능 여부
     *
     * @return APPROVED 또는 PARTIAL_REFUNDED 상태이면 true
     */
    public boolean canRefund() {
        return this == APPROVED || this == PARTIAL_REFUNDED;
    }

    /**
     * 취소 가능 여부
     *
     * @return PENDING 또는 APPROVED 상태이면 true
     */
    public boolean canCancel() {
        return this == PENDING || this == APPROVED;
    }

    /**
     * 최종 상태 여부 (더 이상 상태 변경 불가)
     *
     * @return FULLY_REFUNDED, CANCELLED, FAILED이면 true
     */
    public boolean isFinal() {
        return this == FULLY_REFUNDED || this == CANCELLED || this == FAILED;
    }

    /**
     * 성공 상태 여부
     *
     * @return APPROVED 또는 PARTIAL_REFUNDED이면 true
     */
    public boolean isSuccess() {
        return this == APPROVED || this == PARTIAL_REFUNDED;
    }
}
