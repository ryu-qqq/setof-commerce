package com.ryuqq.setof.application.payment.manager;

import com.ryuqq.setof.application.payment.port.out.command.PaymentCommandPort;
import com.ryuqq.setof.application.payment.port.out.command.PaymentCommandPort.PaymentCommandResult;
import com.ryuqq.setof.domain.payment.aggregate.Payment;
import org.springframework.stereotype.Component;

/**
 * PaymentCommandManager - 결제 명령 Manager.
 *
 * <p>PaymentCommandPort에 위임만 수행합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class PaymentCommandManager {

    private final PaymentCommandPort commandPort;

    public PaymentCommandManager(PaymentCommandPort commandPort) {
        this.commandPort = commandPort;
    }

    public PaymentCommandResult persist(Payment payment) {
        return commandPort.persist(payment);
    }
}
