package com.ryuqq.setof.application.payment.port.in.query;

/**
 * GetPaymentResultUseCase - 결제 성공 여부 조회 UseCase.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface GetPaymentResultUseCase {

    /**
     * 결제 성공 여부 조회.
     *
     * @param paymentId 결제 ID
     * @return 결제 성공 여부
     */
    boolean execute(long paymentId);
}
