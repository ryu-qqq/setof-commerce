package com.ryuqq.setof.application.payment.manager;

import com.ryuqq.setof.application.payment.port.out.query.PaymentBillQueryPort;
import java.util.Optional;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * PaymentReadManager - 결제 조회 Manager.
 *
 * <p>DB 조회만 담당하며 트랜잭션 경계를 설정합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class PaymentReadManager {

    private final PaymentBillQueryPort paymentBillQueryPort;

    public PaymentReadManager(PaymentBillQueryPort paymentBillQueryPort) {
        this.paymentBillQueryPort = paymentBillQueryPort;
    }

    /**
     * 결제 ID로 PG사 거래 ID 조회.
     *
     * @param paymentId 결제 ID
     * @return PG사 거래 ID. 없으면 empty.
     */
    @Transactional(readOnly = true)
    public Optional<String> findPgAgencyId(long paymentId) {
        return paymentBillQueryPort.findPgAgencyIdByPaymentId(paymentId);
    }
}
