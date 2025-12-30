package com.connectly.partnerAdmin.module.payment.repository;


import com.connectly.partnerAdmin.module.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
