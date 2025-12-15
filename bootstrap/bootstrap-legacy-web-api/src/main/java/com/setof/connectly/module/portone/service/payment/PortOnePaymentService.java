package com.setof.connectly.module.portone.service.payment;

import com.setof.connectly.module.exception.payment.PaymentNotFoundException;
import com.setof.connectly.module.order.dto.order.RefundOrderSheet;
import com.setof.connectly.module.portone.client.PortOneClient;
import com.setof.connectly.module.portone.mapper.PortOneMapper;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PortOnePaymentService implements PgPaymentService {
    private final PortOneClient portOneClient;
    private final PortOneMapper portOneMapper;

    @Override
    public Payment getPayment(String impUid) {
        return portOneClient.fetchPaymentPortOne(impUid).orElseThrow(PaymentNotFoundException::new);
    }

    @Override
    public void refundOrder(String pgPaymentId, long paymentId, RefundOrderSheet refundOrderSheet) {
        Payment payment = getPayment(pgPaymentId);
        CancelData cancelData =
                portOneMapper.toCancelData(pgPaymentId, refundOrderSheet, payment.getAmount());
        portOneClient.refundOrder(paymentId, cancelData);
    }
}
