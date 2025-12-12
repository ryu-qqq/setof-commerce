package com.ryuqq.setof.adapter.out.persistence.shippingpolicy.repository;

import com.ryuqq.setof.adapter.out.persistence.shippingpolicy.entity.ShippingPolicyJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * ShippingPolicyJpaRepository - ShippingPolicy JPA Repository
 *
 * <p>Spring Data JPA Repository로서 ShippingPolicy Entity의 기본 CRUD를 담당합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
public interface ShippingPolicyJpaRepository extends JpaRepository<ShippingPolicyJpaEntity, Long> {
    // Query Method 추가 금지
    // @Query 추가 금지
}
