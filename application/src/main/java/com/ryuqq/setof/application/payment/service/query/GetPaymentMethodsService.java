package com.ryuqq.setof.application.payment.service.query;

import com.ryuqq.setof.application.payment.dto.response.PaymentMethodResult;
import com.ryuqq.setof.application.payment.port.in.query.GetPaymentMethodsUseCase;
import com.ryuqq.setof.application.payment.port.out.query.PaymentCodeQueryPort;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * GetPaymentMethodsService - 활성 결제 수단 목록 조회 서비스.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Service
public class GetPaymentMethodsService implements GetPaymentMethodsUseCase {

    private final PaymentCodeQueryPort paymentCodeQueryPort;

    public GetPaymentMethodsService(PaymentCodeQueryPort paymentCodeQueryPort) {
        this.paymentCodeQueryPort = paymentCodeQueryPort;
    }

    @Override
    public List<PaymentMethodResult> execute() {
        return paymentCodeQueryPort.findActivePaymentMethods();
    }
}
