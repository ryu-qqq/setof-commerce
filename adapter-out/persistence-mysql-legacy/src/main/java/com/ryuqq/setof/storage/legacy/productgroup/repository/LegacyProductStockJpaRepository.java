package com.ryuqq.setof.storage.legacy.productgroup.repository;

import com.ryuqq.setof.storage.legacy.productgroup.entity.LegacyProductStockEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * LegacyProductStockJpaRepository - product_stock 테이블 JPA 리포지토리.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface LegacyProductStockJpaRepository
        extends JpaRepository<LegacyProductStockEntity, Long> {}
