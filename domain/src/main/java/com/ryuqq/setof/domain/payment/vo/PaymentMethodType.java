package com.ryuqq.setof.domain.payment.vo;

/** 결제 수단 유형 열거형. */
public enum PaymentMethodType {
    CARD("카드"),
    KAKAO_PAY("카카오페이"),
    NAVER_PAY("네이버페이"),
    VBANK("가상계좌"),
    VBANK_ESCROW("에스크로가상계좌"),
    MILEAGE("마일리지");

    private final String description;

    PaymentMethodType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
