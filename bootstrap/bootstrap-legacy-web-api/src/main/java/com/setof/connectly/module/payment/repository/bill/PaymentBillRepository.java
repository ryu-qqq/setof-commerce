package com.setof.connectly.module.payment.repository.bill;

import com.setof.connectly.module.payment.entity.PaymentBill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentBillRepository extends JpaRepository<PaymentBill, Long> {}
