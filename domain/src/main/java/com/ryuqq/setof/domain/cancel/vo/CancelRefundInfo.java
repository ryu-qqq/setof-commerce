package com.ryuqq.setof.domain.cancel.vo;

import com.ryuqq.setof.domain.common.vo.Money;
import java.time.Instant;

/**
 * 취소 환불 정보 Value Object.
 *
 * <p>취소 완료 시 설정되는 환불 금액과 환불 시각 정보입니다.
 *
 * @param refundAmount 환불 금액 (필수)
 * @param refundedAt 환불 시각 (필수)
 */
public record CancelRefundInfo(Money refundAmount, Instant refundedAt) {

    public CancelRefundInfo {
        if (refundAmount == null) {
            throw new IllegalArgumentException("환불 금액은 필수입니다");
        }
        if (refundedAt == null) {
            throw new IllegalArgumentException("환불 시각은 필수입니다");
        }
    }

    public static CancelRefundInfo of(Money refundAmount, Instant refundedAt) {
        return new CancelRefundInfo(refundAmount, refundedAt);
    }

    public int refundAmountValue() {
        return refundAmount.value();
    }
}
