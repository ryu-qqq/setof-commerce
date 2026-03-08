package com.ryuqq.setof.storage.legacy.review.repository;

import com.ryuqq.setof.storage.legacy.review.entity.LegacyReviewImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * LegacyReviewImageJpaRepository - 레거시 리뷰 이미지 JPA Repository.
 *
 * <p>PER-REP-001: JpaRepository는 save/saveAll만 사용.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface LegacyReviewImageJpaRepository
        extends JpaRepository<LegacyReviewImageEntity, Long> {}
