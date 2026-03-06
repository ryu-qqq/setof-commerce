package com.ryuqq.setof.domain.payment.vo;

/** 결제 채널 열거형. */
public enum PaymentChannel {
    PC("PC"),
    MOBILE("모바일");

    private final String description;

    PaymentChannel(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
