package com.ryuqq.setof.adapter.out.client.portone.dto;

/**
 * PortOne V2 결제 조회 응답.
 *
 * <p>GET /payments/{paymentId} 응답의 status 필드만 매핑합니다.
 *
 * <p>status 가능 값: READY, PENDING, VIRTUAL_ACCOUNT_ISSUED, PAID, PARTIALLY_CANCELLED, CANCELLED,
 * FAILED
 *
 * @param status 결제 상태
 * @author ryu-qqq
 * @since 1.1.0
 */
public record PortOnePaymentResponse(String status) {

    private static final String PAID_STATUS = "PAID";

    /**
     * 결제 완료 여부 확인.
     *
     * @return status가 PAID이면 true
     */
    public boolean isPaid() {
        return PAID_STATUS.equals(status);
    }
}
