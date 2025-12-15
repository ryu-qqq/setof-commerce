package com.setof.connectly.module.payment.repository.shipping.fetch;

import java.util.Optional;

public interface PaymentSnapShotShippingFindRepository {
    Optional<Long> fetchPaymentSnapShotShippingAddressId(long paymentId);
}
