package com.setof.connectly.module.payment.service.snapshot;

import com.setof.connectly.module.payment.entity.snapshot.PaymentSnapShotShippingAddress;

public interface PaymentSnapShotShippingQueryService {

    void saveShippingAddress(PaymentSnapShotShippingAddress paymentSnapShotShippingAddress);
}
