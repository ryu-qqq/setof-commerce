package com.ryuqq.setof.domain.payment.vo;

/** 카드 결제 정보 Value Object. */
public record CardPaymentInfo(String cardName, String cardNumber) {

    public static CardPaymentInfo of(String cardName, String cardNumber) {
        return new CardPaymentInfo(cardName, cardNumber);
    }
}
