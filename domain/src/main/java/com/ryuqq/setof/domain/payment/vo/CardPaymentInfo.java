package com.ryuqq.setof.domain.payment.vo;

/**
 * 카드 결제 정보 Value Object.
 *
 * @param cardName 카드명 (예: 삼성카드)
 * @param cardNumber 카드번호 (마스킹, 예: 1234-****-****-5678)
 * @param installmentMonths 할부 개월수 (0: 일시불, 2~12: 할부)
 */
public record CardPaymentInfo(String cardName, String cardNumber, int installmentMonths) {

    /** 기존 2파라미터 팩토리 (하위호환). */
    public static CardPaymentInfo of(String cardName, String cardNumber) {
        return new CardPaymentInfo(cardName, cardNumber, 0);
    }

    public static CardPaymentInfo of(String cardName, String cardNumber, int installmentMonths) {
        return new CardPaymentInfo(cardName, cardNumber, installmentMonths);
    }

    /** 할부 결제 여부. */
    public boolean isInstallment() {
        return installmentMonths > 0;
    }
}
