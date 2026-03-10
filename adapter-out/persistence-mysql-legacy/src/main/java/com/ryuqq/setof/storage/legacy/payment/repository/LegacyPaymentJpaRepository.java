package com.ryuqq.setof.storage.legacy.payment.repository;

import com.ryuqq.setof.storage.legacy.payment.entity.LegacyPaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * LegacyPaymentJpaRepository - payment 테이블 JPA 리포지토리.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface LegacyPaymentJpaRepository extends JpaRepository<LegacyPaymentEntity, Long> {}
