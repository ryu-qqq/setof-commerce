package com.ryuqq.setof.storage.legacy.payment.adapter;

import com.ryuqq.setof.application.payment.port.out.query.PaymentBillQueryPort;
import com.ryuqq.setof.storage.legacy.payment.repository.LegacyPaymentBillQueryDslRepository;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * LegacyPaymentBillQueryAdapter - 레거시 결제 청구서 조회 Adapter.
 *
 * <p>PaymentBillQueryPort를 구현하여 레거시 payment_bill 테이블에서 PG 거래 ID를 조회합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyPaymentBillQueryAdapter implements PaymentBillQueryPort {

    private final LegacyPaymentBillQueryDslRepository repository;

    public LegacyPaymentBillQueryAdapter(LegacyPaymentBillQueryDslRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<String> findPgAgencyIdByPaymentId(long paymentId) {
        return repository.findPaymentAgencyIdByPaymentId(paymentId);
    }
}
