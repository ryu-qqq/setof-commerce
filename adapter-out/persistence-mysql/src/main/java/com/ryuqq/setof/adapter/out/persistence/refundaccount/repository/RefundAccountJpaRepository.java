package com.ryuqq.setof.adapter.out.persistence.refundaccount.repository;

import com.ryuqq.setof.adapter.out.persistence.refundaccount.entity.RefundAccountJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * RefundAccountJpaRepository - RefundAccount JPA Repository
 *
 * <p>Spring Data JPA Repository로서 RefundAccount Entity의 기본 CRUD를 담당합니다.
 *
 * <p><strong>제공 메서드 (Command 전용):</strong>
 *
 * <ul>
 *   <li>save(entity): 저장/수정 (INSERT/UPDATE)
 * </ul>
 *
 * <p><strong>Query 작업:</strong>
 *
 * <ul>
 *   <li>모든 Query 작업은 RefundAccountQueryDslRepository 사용
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
public interface RefundAccountJpaRepository extends JpaRepository<RefundAccountJpaEntity, Long> {
    // Query Method 추가 금지
    // @Query 추가 금지
}
