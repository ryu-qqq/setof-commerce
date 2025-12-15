package com.setof.connectly.module.payment.repository.mileage;

import com.setof.connectly.module.payment.entity.snapshot.PaymentSnapShotMileage;
import java.util.List;

public interface PaymentSnapShotMileageJdbcRepository {

    void saveAll(List<PaymentSnapShotMileage> paymentSnapShotMileages);
}
