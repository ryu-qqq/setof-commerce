package com.ryuqq.setof.domain.refund.vo;

import com.ryuqq.setof.domain.common.vo.Money;

/**
 * 환불 금액 상세 Value Object.
 *
 * <p>원래 금액, 차감 금액, 차감 사유, 최종 환불 금액을 포함합니다.
 *
 * @param originalAmount 원래 금액 (필수)
 * @param deductionAmount 차감 금액 (null이면 0원으로 설정)
 * @param deductionReason 차감 사유 (선택)
 * @param finalAmount 최종 환불 금액 (필수)
 */
public record RefundInfo(
        Money originalAmount, Money deductionAmount, String deductionReason, Money finalAmount) {

    public RefundInfo {
        if (originalAmount == null) {
            throw new IllegalArgumentException("원래 금액은 필수입니다");
        }
        if (finalAmount == null) {
            throw new IllegalArgumentException("최종 환불 금액은 필수입니다");
        }
        if (deductionAmount == null) {
            deductionAmount = Money.zero();
        }
    }

    /**
     * 환불 금액 상세 생성.
     *
     * @param originalAmount 원래 금액
     * @param deductionAmount 차감 금액
     * @param deductionReason 차감 사유
     * @param finalAmount 최종 환불 금액
     * @return RefundInfo 인스턴스
     */
    public static RefundInfo of(
            Money originalAmount,
            Money deductionAmount,
            String deductionReason,
            Money finalAmount) {
        return new RefundInfo(originalAmount, deductionAmount, deductionReason, finalAmount);
    }

    /**
     * 차감 없는 전액 환불 생성.
     *
     * @param amount 환불 금액
     * @return RefundInfo 인스턴스
     */
    public static RefundInfo withoutDeduction(Money amount) {
        return new RefundInfo(amount, Money.zero(), null, amount);
    }

    /**
     * 차감 금액이 존재하는지 확인합니다.
     *
     * @return 차감 존재 여부
     */
    public boolean hasDeduction() {
        return !deductionAmount.isZero();
    }

    public int originalAmountValue() {
        return originalAmount.value();
    }

    public int deductionAmountValue() {
        return deductionAmount.value();
    }

    public int finalAmountValue() {
        return finalAmount.value();
    }
}
