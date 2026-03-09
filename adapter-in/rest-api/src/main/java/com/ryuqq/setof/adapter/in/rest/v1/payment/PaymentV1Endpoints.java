package com.ryuqq.setof.adapter.in.rest.v1.payment;

/**
 * PaymentV1Endpoints - 결제 V1 Public API 엔드포인트 상수.
 *
 * <p>API-END-001: Endpoints final class + private 생성자.
 *
 * <p>API-END-002: static final 상수.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class PaymentV1Endpoints {

    private PaymentV1Endpoints() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /** V1 기본 경로 */
    public static final String BASE_V1 = "/api/v1";

    /** 결제 상태 조회 (GET /api/v1/payment/{paymentId}/status) */
    public static final String PAYMENT_STATUS = BASE_V1 + "/payment/{paymentId}/status";

    /** 결제 목록 조회 (GET /api/v1/payments) */
    public static final String PAYMENTS = BASE_V1 + "/payments";

    /** 결제 단건 상세 조회 (GET /api/v1/payment/{paymentId}) */
    public static final String PAYMENT_DETAIL = BASE_V1 + "/payment/{paymentId}";
}
