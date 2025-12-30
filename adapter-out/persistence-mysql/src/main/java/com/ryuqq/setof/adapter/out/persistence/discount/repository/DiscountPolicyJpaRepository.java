package com.ryuqq.setof.adapter.out.persistence.discount.repository;

import com.ryuqq.setof.adapter.out.persistence.discount.entity.DiscountPolicyJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * DiscountPolicyJpaRepository - DiscountPolicy JPA Repository
 *
 * <p>Spring Data JPA Repository로서 DiscountPolicy Entity의 기본 CRUD를 담당합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
public interface DiscountPolicyJpaRepository extends JpaRepository<DiscountPolicyJpaEntity, Long> {
    // Query Method 추가 금지
    // @Query 추가 금지
}
