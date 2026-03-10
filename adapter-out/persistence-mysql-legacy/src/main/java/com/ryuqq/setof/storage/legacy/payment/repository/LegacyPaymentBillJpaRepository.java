package com.ryuqq.setof.storage.legacy.payment.repository;

import com.ryuqq.setof.storage.legacy.payment.entity.LegacyPaymentBillEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * LegacyPaymentBillJpaRepository - payment_bill 테이블 JPA 리포지토리.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface LegacyPaymentBillJpaRepository
        extends JpaRepository<LegacyPaymentBillEntity, Long> {}
