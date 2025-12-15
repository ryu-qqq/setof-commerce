package com.setof.connectly.module.payment.repository.shipping;

import com.setof.connectly.module.payment.entity.snapshot.PaymentSnapShotShippingAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentSnapShotShippingAddressRepository
        extends JpaRepository<PaymentSnapShotShippingAddress, Long> {}
