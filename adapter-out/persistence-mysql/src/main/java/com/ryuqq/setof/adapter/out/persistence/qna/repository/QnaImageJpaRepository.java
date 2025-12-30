package com.ryuqq.setof.adapter.out.persistence.qna.repository;

import com.ryuqq.setof.adapter.out.persistence.qna.entity.QnaImageJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * QnaImageJpaRepository - QnA 이미지 JPA Repository
 *
 * <p>Spring Data JPA Repository로서 QnA Image Entity의 기본 CRUD를 담당합니다.
 *
 * <p><strong>제공 메서드 (Command 전용):</strong>
 *
 * <ul>
 *   <li>save(entity): 저장/수정 (INSERT/UPDATE)
 *   <li>saveAll(entities): 다건 저장
 *   <li>delete(entity): 삭제 (DELETE)
 *   <li>deleteById(id): ID로 삭제
 * </ul>
 *
 * <p><strong>Query 작업:</strong>
 *
 * <ul>
 *   <li>모든 Query 작업은 QnaQueryDslRepository 사용
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
public interface QnaImageJpaRepository extends JpaRepository<QnaImageJpaEntity, Long> {
    // Query Method 추가 금지
    // @Query 추가 금지
}
