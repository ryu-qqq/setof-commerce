package com.ryuqq.setof.adapter.out.persistence.review.repository;

import com.ryuqq.setof.adapter.out.persistence.review.entity.ReviewImageJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * ReviewImageJpaRepository - 리뷰 이미지 JPA Repository
 *
 * <p>Spring Data JPA Repository로서 ReviewImage Entity의 기본 CRUD를 담당합니다.
 *
 * <p><strong>제공 메서드 (Command 전용):</strong>
 *
 * <ul>
 *   <li>saveAll(entities): 일괄 저장
 *   <li>deleteAllByReviewId(reviewId): 리뷰별 이미지 일괄 삭제
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
public interface ReviewImageJpaRepository extends JpaRepository<ReviewImageJpaEntity, Long> {

    /**
     * 리뷰 ID로 모든 이미지 삭제
     *
     * @param reviewId 리뷰 ID
     */
    void deleteAllByReviewId(Long reviewId);
}
