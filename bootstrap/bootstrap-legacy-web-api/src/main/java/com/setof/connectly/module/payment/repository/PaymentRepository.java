package com.setof.connectly.module.payment.repository;

import com.setof.connectly.module.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {}
