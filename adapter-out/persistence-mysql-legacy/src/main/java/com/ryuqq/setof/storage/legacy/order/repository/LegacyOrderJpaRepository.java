package com.ryuqq.setof.storage.legacy.order.repository;

import com.ryuqq.setof.storage.legacy.order.entity.LegacyOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * LegacyOrderJpaRepository - Legacy orders 테이블 JPA Repository.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface LegacyOrderJpaRepository extends JpaRepository<LegacyOrderEntity, Long> {}
