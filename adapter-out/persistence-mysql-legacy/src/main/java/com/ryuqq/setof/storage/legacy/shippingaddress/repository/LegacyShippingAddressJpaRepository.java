package com.ryuqq.setof.storage.legacy.shippingaddress.repository;

import com.ryuqq.setof.storage.legacy.shippingaddress.entity.LegacyShippingAddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * LegacyShippingAddressJpaRepository - 레거시 배송지 JPA 레포지토리.
 *
 * <p>PER-REP-001: JpaRepository는 CUD 작업만 담당.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface LegacyShippingAddressJpaRepository
        extends JpaRepository<LegacyShippingAddressEntity, Long> {}
