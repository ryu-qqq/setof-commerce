package com.ryuqq.setof.application.payment.port.out.query;

import java.util.Optional;

/**
 * PaymentBillQueryPort - 결제 청구서 조회 Port.
 *
 * <p>payment_bill 테이블에서 PG 거래 ID 등을 조회합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface PaymentBillQueryPort {

    /**
     * 결제 ID로 PG사 거래 ID 조회.
     *
     * @param paymentId 결제 ID
     * @return PG사 거래 ID (payment_agency_id). 없으면 empty.
     */
    Optional<String> findPgAgencyIdByPaymentId(long paymentId);
}
