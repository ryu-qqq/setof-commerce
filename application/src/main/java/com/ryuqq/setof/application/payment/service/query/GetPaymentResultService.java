package com.ryuqq.setof.application.payment.service.query;

import com.ryuqq.setof.application.payment.facade.PaymentResultReadFacade;
import com.ryuqq.setof.application.payment.port.in.query.GetPaymentResultUseCase;
import org.springframework.stereotype.Service;

/**
 * GetPaymentResultService - 결제 성공 여부 조회 서비스.
 *
 * <p>PaymentResultReadFacade를 통해 DB 조회 + PG사 결제 상태를 확인합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Service
public class GetPaymentResultService implements GetPaymentResultUseCase {

    private final PaymentResultReadFacade paymentResultReadFacade;

    public GetPaymentResultService(PaymentResultReadFacade paymentResultReadFacade) {
        this.paymentResultReadFacade = paymentResultReadFacade;
    }

    @Override
    public boolean execute(long paymentId) {
        return paymentResultReadFacade.isPaymentSuccessful(paymentId);
    }
}
