package com.ryuqq.setof.adapter.out.persistence.brand.repository;

import com.ryuqq.setof.adapter.out.persistence.brand.entity.BrandJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * BrandJpaRepository - Brand JPA Repository
 *
 * <p>Spring Data JPA Repository로서 Brand Entity의 기본 CRUD를 담당합니다.
 *
 * <p><strong>제공 메서드 (Command 전용):</strong>
 *
 * <ul>
 *   <li>save(entity): 저장/수정 (INSERT/UPDATE)
 *   <li>delete(entity): 삭제 (DELETE)
 *   <li>deleteById(id): ID로 삭제
 * </ul>
 *
 * <p><strong>Query 작업:</strong>
 *
 * <ul>
 *   <li>모든 Query 작업은 BrandQueryDslRepository 사용
 * </ul>
 *
 * <p><strong>참고:</strong> 이 프로젝트에서 Brand는 읽기 전용이므로 Command 메서드는 배치 서비스에서만 사용됩니다.
 *
 * @author development-team
 * @since 1.0.0
 */
public interface BrandJpaRepository extends JpaRepository<BrandJpaEntity, Long> {
    // Query Method 추가 금지
    // @Query 추가 금지
}
