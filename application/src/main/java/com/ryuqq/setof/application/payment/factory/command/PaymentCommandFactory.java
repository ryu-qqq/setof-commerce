package com.ryuqq.setof.application.payment.factory.command;

import com.ryuqq.setof.domain.checkout.aggregate.Checkout;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import com.ryuqq.setof.domain.payment.aggregate.Payment;
import com.ryuqq.setof.domain.payment.vo.PaymentMethod;
import com.ryuqq.setof.domain.payment.vo.PaymentMoney;
import com.ryuqq.setof.domain.payment.vo.PgProvider;
import java.time.Instant;
import org.springframework.stereotype.Component;

/**
 * Payment Command Factory
 *
 * <p>Command를 Domain Aggregate로 변환하는 Factory
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class PaymentCommandFactory {

    private final ClockHolder clockHolder;

    public PaymentCommandFactory(ClockHolder clockHolder) {
        this.clockHolder = clockHolder;
    }

    /**
     * Checkout으로부터 Payment Aggregate 생성
     *
     * <p>Checkout 생성 시 함께 PENDING 상태의 Payment를 생성합니다.
     *
     * @param checkout Checkout Aggregate
     * @param pgProvider PG사
     * @param method 결제 수단
     * @return Payment Aggregate (PENDING 상태)
     */
    public Payment createFromCheckout(
            Checkout checkout, PgProvider pgProvider, PaymentMethod method) {
        PaymentMoney requestedAmount = PaymentMoney.of(checkout.finalAmount().value());
        Instant now = Instant.now(clockHolder.getClock());

        return Payment.forNewRequest(checkout.id(), pgProvider, method, requestedAmount, now);
    }
}
