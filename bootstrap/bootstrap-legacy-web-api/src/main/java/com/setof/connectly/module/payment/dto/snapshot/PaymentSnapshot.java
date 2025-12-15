package com.setof.connectly.module.payment.dto.snapshot;

import com.setof.connectly.module.order.dto.snapshot.OrderProductSnapShotQueryDto;
import com.setof.connectly.module.payment.entity.PaymentBill;
import java.util.Set;

public class PaymentSnapshot {

    private PaymentBill paymentBill;

    private Set<OrderProductSnapShotQueryDto> orderSnapShots;
}
