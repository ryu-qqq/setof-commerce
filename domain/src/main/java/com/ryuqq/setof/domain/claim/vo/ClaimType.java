package com.ryuqq.setof.domain.claim.vo;

/**
 * ClaimType - 클레임 유형
 *
 * <p>고객이 요청할 수 있는 클레임의 종류를 정의합니다.
 *
 * @author development-team
 * @since 2.0.0
 */
public enum ClaimType {

    /** 주문 취소 (배송 전) */
    CANCEL("취소", true, false),

    /** 반품 (배송 후 상품 반환) */
    RETURN("반품", true, true),

    /** 교환 (배송 후 상품 교체) */
    EXCHANGE("교환", false, true),

    /** 부분 환불 (일부 금액만 환불) */
    PARTIAL_REFUND("부분환불", true, false);

    private final String description;
    private final boolean requiresRefund;
    private final boolean requiresReturn;

    ClaimType(String description, boolean requiresRefund, boolean requiresReturn) {
        this.description = description;
        this.requiresRefund = requiresRefund;
        this.requiresReturn = requiresReturn;
    }

    /**
     * 클레임 유형 설명 반환
     *
     * @return 유형 설명
     */
    public String description() {
        return description;
    }

    /**
     * 환불이 필요한 유형인지 확인
     *
     * @return 환불 필요 여부
     */
    public boolean requiresRefund() {
        return requiresRefund;
    }

    /**
     * 반품 수거가 필요한 유형인지 확인
     *
     * @return 반품 수거 필요 여부
     */
    public boolean requiresReturn() {
        return requiresReturn;
    }

    /**
     * 배송 전에만 가능한 유형인지 확인
     *
     * @return CANCEL이면 true
     */
    public boolean isPreShippingOnly() {
        return this == CANCEL;
    }

    /**
     * 배송 후에만 가능한 유형인지 확인
     *
     * @return RETURN, EXCHANGE이면 true
     */
    public boolean isPostShippingOnly() {
        return this == RETURN || this == EXCHANGE;
    }
}
