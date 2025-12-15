package com.setof.connectly.module.payment.repository.mileage;

import com.setof.connectly.module.payment.entity.snapshot.PaymentSnapShotMileage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentSnapShotMileageRepository
        extends JpaRepository<PaymentSnapShotMileage, Long> {}
