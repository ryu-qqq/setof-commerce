package com.ryuqq.setof.domain.claim.vo;

/**
 * InspectionResult - 반품 검수 결과
 *
 * <p>반품 상품 수령 후 검수 결과를 나타냅니다.
 *
 * <p>검수 결과에 따른 처리:
 *
 * <ul>
 *   <li>PASS: 전액 환불 진행
 *   <li>FAIL: 환불 거부, 상품 재발송 또는 폐기
 *   <li>PARTIAL: 감가 환불 (훼손 정도에 따라)
 * </ul>
 *
 * @author development-team
 * @since 2.0.0
 */
public enum InspectionResult {

    /**
     * 검수 합격
     *
     * <p>상품 상태가 양호하여 전액 환불이 가능합니다.
     */
    PASS("합격", true, false),

    /**
     * 검수 불합격
     *
     * <p>상품 상태 불량으로 환불이 불가합니다. 상품은 고객에게 재발송되거나 협의 후 폐기됩니다.
     */
    FAIL("불합격", false, false),

    /**
     * 부분 합격 (감가)
     *
     * <p>상품 일부 훼손으로 감가 환불이 진행됩니다. 훼손 정도에 따라 환불 금액이 조정됩니다.
     */
    PARTIAL("부분합격", true, true);

    private final String description;
    private final boolean refundable;
    private final boolean requiresDeduction;

    InspectionResult(String description, boolean refundable, boolean requiresDeduction) {
        this.description = description;
        this.refundable = refundable;
        this.requiresDeduction = requiresDeduction;
    }

    /**
     * 검수 결과 설명 반환
     *
     * @return 설명 (한글)
     */
    public String description() {
        return description;
    }

    /**
     * 환불 가능 여부
     *
     * @return PASS 또는 PARTIAL이면 true
     */
    public boolean isRefundable() {
        return refundable;
    }

    /**
     * 감가 필요 여부
     *
     * @return PARTIAL이면 true
     */
    public boolean requiresDeduction() {
        return requiresDeduction;
    }

    /**
     * 전액 환불 가능 여부
     *
     * @return PASS면 true
     */
    public boolean isFullRefund() {
        return this == PASS;
    }

    /**
     * 상품 재발송 필요 여부
     *
     * @return FAIL이면 true
     */
    public boolean requiresReshipment() {
        return this == FAIL;
    }
}
