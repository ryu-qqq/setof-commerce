package com.ryuqq.setof.storage.legacy.review.adapter;

import com.ryuqq.setof.application.review.port.out.command.ReviewCommandPort;
import com.ryuqq.setof.domain.review.aggregate.Review;
import com.ryuqq.setof.storage.legacy.review.entity.LegacyReviewEntity;
import com.ryuqq.setof.storage.legacy.review.mapper.LegacyReviewEntityMapper;
import com.ryuqq.setof.storage.legacy.review.repository.LegacyReviewJpaRepository;
import org.springframework.stereotype.Component;

/**
 * LegacyReviewCommandAdapter - 레거시 리뷰 Command Adapter.
 *
 * <p>ReviewCommandPort 구현체. 레거시 DB의 review 테이블만 영속합니다.
 *
 * <p>PER-ADP-001: CommandAdapter는 JpaRepository + Mapper에만 의존.
 *
 * <p>PER-ADP-002: Adapter에서 @Transactional 금지.
 *
 * <p>PER-ADP-005: Domain -> Entity 변환 (Mapper 사용).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyReviewCommandAdapter implements ReviewCommandPort {

    private final LegacyReviewJpaRepository jpaRepository;
    private final LegacyReviewEntityMapper mapper;

    public LegacyReviewCommandAdapter(
            LegacyReviewJpaRepository jpaRepository, LegacyReviewEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Long persist(Review review) {
        LegacyReviewEntity entity = mapper.toEntity(review);
        LegacyReviewEntity saved = jpaRepository.save(entity);
        return saved.getId();
    }
}
