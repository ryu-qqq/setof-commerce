package com.ryuqq.setof.adapter.in.rest.v1.payment;

/**
 * PaymentCodeV1Endpoints - 결제 코드 V1 Public API 엔드포인트 상수.
 *
 * <p>API-END-001: Endpoints final class + private 생성자.
 *
 * <p>API-END-002: static final 상수.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class PaymentCodeV1Endpoints {

    private PaymentCodeV1Endpoints() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /** V1 기본 경로 */
    public static final String BASE_V1 = "/api/v1";

    /** 결제 코드 기본 경로 */
    public static final String PAYMENT_CODES = BASE_V1 + "/payment/codes";

    /** 결제 수단 목록 (GET /api/v1/payment/codes/methods) */
    public static final String METHODS = PAYMENT_CODES + "/methods";

    /** 가상계좌 은행 목록 (GET /api/v1/payment/codes/vbank-refund-accounts) */
    public static final String VBANK_REFUND_ACCOUNTS = PAYMENT_CODES + "/vbank-refund-accounts";

    /** 환불 은행 목록 (GET /api/v1/payment/codes/refund-bank-accounts) */
    public static final String REFUND_BANK_ACCOUNTS = PAYMENT_CODES + "/refund-bank-accounts";
}
