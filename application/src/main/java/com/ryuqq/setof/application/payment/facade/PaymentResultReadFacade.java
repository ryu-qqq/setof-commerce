package com.ryuqq.setof.application.payment.facade;

import com.ryuqq.setof.application.payment.manager.PaymentGatewayManager;
import com.ryuqq.setof.application.payment.manager.PaymentReadManager;
import org.springframework.stereotype.Component;

/**
 * PaymentResultReadFacade - 결제 결과 조회 Facade.
 *
 * <p>PaymentReadManager(DB 조회)와 PaymentGatewayManager(외부 PG 호출)를 조합합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class PaymentResultReadFacade {

    private final PaymentReadManager paymentReadManager;
    private final PaymentGatewayManager paymentGatewayManager;

    public PaymentResultReadFacade(
            PaymentReadManager paymentReadManager, PaymentGatewayManager paymentGatewayManager) {
        this.paymentReadManager = paymentReadManager;
        this.paymentGatewayManager = paymentGatewayManager;
    }

    /**
     * 결제 성공 여부 확인.
     *
     * <p>1. DB에서 PG 거래 ID 조회 (트랜잭션 내)
     *
     * <p>2. PG사 API로 실제 결제 상태 확인 (트랜잭션 밖)
     *
     * <p>3. PG 거래 ID 없으면 false
     *
     * @param paymentId 결제 ID
     * @return 결제 성공 여부
     */
    public boolean isPaymentSuccessful(long paymentId) {
        return paymentReadManager
                .findPgAgencyId(paymentId)
                .map(paymentGatewayManager::isPaid)
                .orElse(false);
    }
}
