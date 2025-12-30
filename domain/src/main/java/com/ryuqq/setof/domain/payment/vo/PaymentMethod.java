package com.ryuqq.setof.domain.payment.vo;

/**
 * PaymentMethod - 결제 수단
 *
 * <p>지원하는 결제 수단을 정의합니다.
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java enum 사용
 * </ul>
 */
public enum PaymentMethod {

    /** 신용/체크카드 */
    CARD("카드 결제"),

    /** 실시간 계좌이체 */
    BANK_TRANSFER("계좌이체"),

    /** 가상계좌 */
    VIRTUAL_ACCOUNT("가상계좌"),

    /** 카카오페이 */
    KAKAO_PAY("카카오페이"),

    /** 네이버페이 */
    NAVER_PAY("네이버페이"),

    /** 토스페이 */
    TOSS_PAY("토스페이"),

    /** 페이코 */
    PAYCO("페이코"),

    /** 휴대폰 결제 */
    PHONE("휴대폰 결제");

    private final String description;

    PaymentMethod(String description) {
        this.description = description;
    }

    /**
     * 결제 수단 설명 반환
     *
     * @return 결제 수단 설명
     */
    public String description() {
        return description;
    }

    /**
     * 간편결제 여부
     *
     * @return 간편결제이면 true
     */
    public boolean isEasyPay() {
        return this == KAKAO_PAY || this == NAVER_PAY || this == TOSS_PAY || this == PAYCO;
    }

    /**
     * 즉시 결제 여부 (가상계좌 제외)
     *
     * @return 즉시 결제 수단이면 true
     */
    public boolean isInstant() {
        return this != VIRTUAL_ACCOUNT;
    }
}
