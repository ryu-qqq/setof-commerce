package com.ryuqq.setof.storage.legacy.payment.repository;

import com.ryuqq.setof.storage.legacy.payment.entity.LegacyPaymentSnapshotShippingAddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * LegacyPaymentSnapshotShippingAddressJpaRepository - 배송지 스냅샷 JPA 리포지토리.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface LegacyPaymentSnapshotShippingAddressJpaRepository
        extends JpaRepository<LegacyPaymentSnapshotShippingAddressEntity, Long> {}
