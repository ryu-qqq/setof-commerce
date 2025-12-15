package com.setof.connectly.module.payment.repository.account;

import com.setof.connectly.module.payment.entity.snapshot.PaymentSnapShotRefundAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentSnapShotRefundAccountRepository
        extends JpaRepository<PaymentSnapShotRefundAccount, Long> {}
