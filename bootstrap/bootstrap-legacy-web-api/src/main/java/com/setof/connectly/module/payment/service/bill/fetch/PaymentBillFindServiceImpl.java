package com.setof.connectly.module.payment.service.bill.fetch;

import com.setof.connectly.module.exception.payment.PaymentBillNotFoundException;
import com.setof.connectly.module.payment.dto.payment.FailPaymentResponse;
import com.setof.connectly.module.payment.dto.payment.PaymentResult;
import com.setof.connectly.module.payment.entity.PaymentBill;
import com.setof.connectly.module.payment.repository.bill.fetch.PaymentBillFindRepository;
import com.setof.connectly.module.portone.client.PortOneClient;
import com.setof.connectly.module.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class PaymentBillFindServiceImpl implements PaymentBillFindService {

    private final PaymentBillFindRepository paymentBillFindRepository;

    private final PortOneClient portOneClient;

    @Override
    public PaymentBill fetchPaymentBillEntity(long paymentId) {
        return paymentBillFindRepository
                .fetchPaymentBillEntity(paymentId)
                .orElseThrow(() -> new PaymentBillNotFoundException(paymentId));
    }

    @Override
    public PaymentBill fetchPaymentBillEntityByUniqueId(String paymentUniqueId) {
        return paymentBillFindRepository
                .fetchPaymentBillEntityByUniqueId(paymentUniqueId)
                .orElseThrow(() -> new PaymentBillNotFoundException(paymentUniqueId));
    }

    @Override
    public FailPaymentResponse fetchPaymentMethod(String paymentUniqueId) {
        return paymentBillFindRepository
                .fetchPaymentMethod(paymentUniqueId, SecurityUtils.currentUserId())
                .orElseThrow(() -> new PaymentBillNotFoundException(paymentUniqueId));
    }

    @Override
    public PaymentResult fetchPaymentResult(long paymentId) {
        Boolean aBoolean =
                paymentBillFindRepository
                        .fetchPaymentAgencyId(paymentId)
                        .filter(StringUtils::hasText)
                        .flatMap(portOneClient::fetchPaymentPortOne)
                        .map(payment -> "paid".equals(payment.getStatus()))
                        .orElse(false);

        return new PaymentResult(aBoolean);
    }

    @Override
    public String fetchPaymentAgencyId(long paymentId) {
        return paymentBillFindRepository
                .fetchPaymentAgencyId(paymentId)
                .orElseThrow(() -> new PaymentBillNotFoundException(paymentId));
    }
}
