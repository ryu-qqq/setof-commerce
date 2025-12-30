package com.ryuqq.setof.adapter.out.persistence.review.repository;

import com.ryuqq.setof.adapter.out.persistence.review.entity.ReviewJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * ReviewJpaRepository - 리뷰 JPA Repository
 *
 * <p>Spring Data JPA Repository로서 Review Entity의 기본 CRUD를 담당합니다.
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
 *   <li>모든 Query 작업은 ReviewQueryDslRepository 사용
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
public interface ReviewJpaRepository extends JpaRepository<ReviewJpaEntity, Long> {
    // Query Method 추가 금지
    // @Query 추가 금지
}
