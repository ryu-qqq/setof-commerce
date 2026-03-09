package com.ryuqq.setof.application.payment.port.out.query;

import com.ryuqq.setof.domain.payment.vo.PaymentFullDetail;

/**
 * PaymentDetailQueryPort - 결제 단건 상세 조회 Port-Out.
 *
 * <p>구매자/수령인/환불 계좌를 포함한 결제 전체 상세 조회를 담당합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface PaymentDetailQueryPort {

    /**
     * 결제 ID와 사용자 ID로 결제 전체 상세 조회.
     *
     * @param paymentId 결제 ID
     * @param userId 사용자 ID (본인 확인용)
     * @return 결제 전체 상세 (도메인 VO)
     */
    PaymentFullDetail fetchPaymentDetail(long paymentId, long userId);
}
