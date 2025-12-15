package com.setof.connectly.module.payment.service.snapshot;

import com.setof.connectly.module.payment.entity.snapshot.PaymentSnapShotShippingAddress;
import com.setof.connectly.module.payment.repository.shipping.PaymentSnapShotShippingAddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class PaymentSnapShotShippingQueryServiceImpl
        implements PaymentSnapShotShippingQueryService {

    private final PaymentSnapShotShippingAddressRepository paymentSnapShotShippingAddressRepository;

    @Override
    public void saveShippingAddress(PaymentSnapShotShippingAddress paymentSnapShotShippingAddress) {
        paymentSnapShotShippingAddressRepository.save(paymentSnapShotShippingAddress);
    }
}
