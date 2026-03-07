package com.ryuqq.setof.storage.legacy.refundaccount.repository;

import com.ryuqq.setof.storage.legacy.refundaccount.entity.LegacyRefundAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * LegacyRefundAccountJpaRepository - 레거시 환불 계좌 JPA 레포지토리.
 *
 * <p>PER-REP-001: JpaRepository는 CUD 작업만 담당.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface LegacyRefundAccountJpaRepository
        extends JpaRepository<LegacyRefundAccountEntity, Long> {}
