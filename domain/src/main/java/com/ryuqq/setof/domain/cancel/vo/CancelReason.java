package com.ryuqq.setof.domain.cancel.vo;

/**
 * 취소 사유 Value Object.
 *
 * <p>취소 사유 유형과 상세 내용을 포함합니다.
 *
 * @param type 취소 사유 유형 (필수)
 * @param detail 취소 사유 상세 내용 (선택)
 */
public record CancelReason(CancelReasonType type, String detail) {

    public CancelReason {
        if (type == null) {
            throw new IllegalArgumentException("취소 사유 유형은 필수입니다");
        }
    }

    public static CancelReason of(CancelReasonType type, String detail) {
        return new CancelReason(type, detail);
    }

    public static CancelReason ofType(CancelReasonType type) {
        return new CancelReason(type, null);
    }
}
