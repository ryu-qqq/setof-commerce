package com.setof.connectly.module.payment.service.pay;

import java.util.List;

public interface PaymentLockService {

    boolean tryLock(List<Long> productIds);
}
