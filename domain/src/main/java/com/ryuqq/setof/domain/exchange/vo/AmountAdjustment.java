package com.ryuqq.setof.domain.exchange.vo;

import com.ryuqq.setof.domain.common.vo.Money;

/**
 * 교환 금액 조정 정보 Value Object.
 *
 * <p>교환 시 원래 상품과 교환 상품 간의 가격 차이 정보를 나타냅니다.
 *
 * @param originalAmount 원래 상품 금액 (필수)
 * @param exchangeAmount 교환 상품 금액 (필수)
 * @param additionalPayment 추가 결제 금액 (교환 상품이 더 비싼 경우)
 * @param partialRefund 부분 환불 금액 (교환 상품이 더 저렴한 경우)
 */
public record AmountAdjustment(
        Money originalAmount, Money exchangeAmount, Money additionalPayment, Money partialRefund) {

    public AmountAdjustment {
        if (originalAmount == null) {
            throw new IllegalArgumentException("원래 금액은 필수입니다");
        }
        if (exchangeAmount == null) {
            throw new IllegalArgumentException("교환 상품 금액은 필수입니다");
        }
        if (additionalPayment == null) {
            additionalPayment = Money.zero();
        }
        if (partialRefund == null) {
            partialRefund = Money.zero();
        }
    }

    /**
     * 원래 금액과 교환 금액으로 금액 조정 정보를 생성합니다.
     *
     * <p>교환 상품이 더 비싸면 추가 결제, 더 저렴하면 부분 환불이 설정됩니다.
     *
     * @param originalAmount 원래 상품 금액
     * @param exchangeAmount 교환 상품 금액
     * @return 금액 조정 정보
     */
    public static AmountAdjustment of(Money originalAmount, Money exchangeAmount) {
        int diff = exchangeAmount.value() - originalAmount.value();
        if (diff > 0) {
            return new AmountAdjustment(
                    originalAmount, exchangeAmount, Money.of(diff), Money.zero());
        } else if (diff < 0) {
            return new AmountAdjustment(
                    originalAmount, exchangeAmount, Money.zero(), Money.of(-diff));
        } else {
            return new AmountAdjustment(originalAmount, exchangeAmount, Money.zero(), Money.zero());
        }
    }

    /**
     * 추가 결제가 필요한지 확인합니다.
     *
     * @return 추가 결제 필요 여부
     */
    public boolean requiresAdditionalPayment() {
        return !additionalPayment.isZero();
    }

    /**
     * 부분 환불이 필요한지 확인합니다.
     *
     * @return 부분 환불 필요 여부
     */
    public boolean requiresPartialRefund() {
        return !partialRefund.isZero();
    }

    /**
     * 동일 금액 교환인지 확인합니다.
     *
     * @return 동일 금액 교환 여부
     */
    public boolean isEvenExchange() {
        return additionalPayment.isZero() && partialRefund.isZero();
    }

    public int originalAmountValue() {
        return originalAmount.value();
    }

    public int exchangeAmountValue() {
        return exchangeAmount.value();
    }

    public int additionalPaymentValue() {
        return additionalPayment.value();
    }

    public int partialRefundValue() {
        return partialRefund.value();
    }
}
