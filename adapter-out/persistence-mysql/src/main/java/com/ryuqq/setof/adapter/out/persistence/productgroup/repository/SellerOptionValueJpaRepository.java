package com.ryuqq.setof.adapter.out.persistence.productgroup.repository;

import com.ryuqq.setof.adapter.out.persistence.productgroup.entity.SellerOptionValueJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * SellerOptionValueJpaRepository - 셀러 옵션 값 JPA 레포지토리.
 *
 * <p>PER-REP-001: CommandAdapter는 JpaRepository만 사용.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
public interface SellerOptionValueJpaRepository
        extends JpaRepository<SellerOptionValueJpaEntity, Long> {}
