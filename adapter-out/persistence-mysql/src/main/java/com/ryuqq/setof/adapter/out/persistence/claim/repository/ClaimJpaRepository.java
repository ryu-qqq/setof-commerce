package com.ryuqq.setof.adapter.out.persistence.claim.repository;

import com.ryuqq.setof.adapter.out.persistence.claim.entity.ClaimJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * ClaimJpaRepository - 클레임 JPA Repository
 *
 * <p>Claim 엔티티의 기본 CRUD를 담당합니다.
 *
 * <p><strong>제공 메서드 (표준 JPA 메서드만 사용):</strong>
 *
 * <ul>
 *   <li>save(entity): 저장/수정 (INSERT/UPDATE)
 *   <li>delete(entity): 삭제 (DELETE)
 * </ul>
 *
 * <p><strong>커스텀 쿼리:</strong> ClaimQueryDslRepository 사용
 *
 * @author development-team
 * @since 2.0.0
 */
@Repository
public interface ClaimJpaRepository extends JpaRepository<ClaimJpaEntity, Long> {
    // 커스텀 메서드 금지 - QueryDslRepository 사용
}
