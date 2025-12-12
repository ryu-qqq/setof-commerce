package com.ryuqq.setof.adapter.out.persistence.refundpolicy.repository;

import com.ryuqq.setof.adapter.out.persistence.refundpolicy.entity.RefundPolicyJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * RefundPolicyJpaRepository - RefundPolicy JPA Repository
 *
 * <p>Spring Data JPA Repository로서 RefundPolicy Entity의 기본 CRUD를 담당합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
public interface RefundPolicyJpaRepository extends JpaRepository<RefundPolicyJpaEntity, Long> {
    // Query Method 추가 금지
    // @Query 추가 금지
}
