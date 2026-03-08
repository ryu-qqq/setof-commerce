package com.ryuqq.setof.storage.legacy.review.adapter;

import com.ryuqq.setof.application.review.port.out.command.ReviewImageCommandPort;
import com.ryuqq.setof.domain.reviewimage.aggregate.ReviewImage;
import com.ryuqq.setof.storage.legacy.review.entity.LegacyReviewImageEntity;
import com.ryuqq.setof.storage.legacy.review.mapper.LegacyReviewEntityMapper;
import com.ryuqq.setof.storage.legacy.review.repository.LegacyReviewImageJpaRepository;
import com.ryuqq.setof.storage.legacy.review.repository.LegacyReviewImageQueryDslRepository;
import java.util.List;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 * LegacyReviewImageCommandAdapter - 레거시 리뷰 이미지 Command Adapter.
 *
 * <p>ReviewImageCommandPort 구현체. 레거시 DB의 review_image 테이블만 영속합니다.
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
@Primary
@Component
public class LegacyReviewImageCommandAdapter implements ReviewImageCommandPort {

    private final LegacyReviewImageJpaRepository jpaRepository;
    private final LegacyReviewImageQueryDslRepository queryDslRepository;
    private final LegacyReviewEntityMapper mapper;

    public LegacyReviewImageCommandAdapter(
            LegacyReviewImageJpaRepository jpaRepository,
            LegacyReviewImageQueryDslRepository queryDslRepository,
            LegacyReviewEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.queryDslRepository = queryDslRepository;
        this.mapper = mapper;
    }

    @Override
    public Long persist(ReviewImage reviewImage) {
        LegacyReviewImageEntity entity =
                mapper.toImageEntity(reviewImage.reviewIdValue(), reviewImage);
        LegacyReviewImageEntity saved = jpaRepository.save(entity);
        return saved.getId();
    }

    @Override
    public void persistAll(List<ReviewImage> reviewImages) {
        List<LegacyReviewImageEntity> entities =
                reviewImages.stream()
                        .map(img -> mapper.toImageEntity(img.reviewIdValue(), img))
                        .toList();
        jpaRepository.saveAll(entities);
    }

    @Override
    public void deleteByReviewId(long reviewId) {
        queryDslRepository.deleteByReviewId(reviewId);
    }
}
