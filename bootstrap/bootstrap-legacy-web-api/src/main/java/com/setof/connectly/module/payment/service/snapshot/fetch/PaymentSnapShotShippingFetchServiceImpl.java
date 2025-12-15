package com.setof.connectly.module.payment.service.snapshot.fetch;

import com.setof.connectly.module.exception.payment.PaymentBillNotFoundException;
import com.setof.connectly.module.payment.repository.shipping.fetch.PaymentSnapShotShippingFindRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class PaymentSnapShotShippingFetchServiceImpl
        implements PaymentSnapShotShippingFetchService {

    private final PaymentSnapShotShippingFindRepository paymentSnapShotShippingFindRepository;

    @Override
    public long fetchPaymentSnapShotShippingId(long paymentId) {
        return paymentSnapShotShippingFindRepository
                .fetchPaymentSnapShotShippingAddressId(paymentId)
                .orElseThrow(() -> new PaymentBillNotFoundException(paymentId));
    }
}
