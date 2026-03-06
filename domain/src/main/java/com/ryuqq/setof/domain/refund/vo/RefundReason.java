package com.ryuqq.setof.domain.refund.vo;

/**
 * 반품 사유 Value Object.
 *
 * <p>반품 사유 유형과 상세 설명을 포함합니다.
 *
 * @param type 반품 사유 유형 (필수)
 * @param detail 상세 설명 (선택)
 */
public record RefundReason(RefundReasonType type, String detail) {

    public RefundReason {
        if (type == null) {
            throw new IllegalArgumentException("반품 사유 유형은 필수입니다");
        }
    }

    /**
     * 사유 유형과 상세 설명으로 생성합니다.
     *
     * @param type 반품 사유 유형
     * @param detail 상세 설명
     * @return RefundReason 인스턴스
     */
    public static RefundReason of(RefundReasonType type, String detail) {
        return new RefundReason(type, detail);
    }

    /**
     * 사유 유형만으로 생성합니다.
     *
     * @param type 반품 사유 유형
     * @return RefundReason 인스턴스
     */
    public static RefundReason ofType(RefundReasonType type) {
        return new RefundReason(type, null);
    }
}
