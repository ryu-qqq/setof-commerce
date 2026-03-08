package com.ryuqq.setof.storage.legacy.review.adapter;

import com.ryuqq.setof.application.review.port.out.query.ReviewImageQueryPort;
import com.ryuqq.setof.domain.reviewimage.aggregate.ReviewImage;
import com.ryuqq.setof.domain.reviewimage.aggregate.ReviewImages;
import com.ryuqq.setof.storage.legacy.review.entity.LegacyReviewImageEntity;
import com.ryuqq.setof.storage.legacy.review.mapper.LegacyReviewEntityMapper;
import com.ryuqq.setof.storage.legacy.review.repository.LegacyReviewImageQueryDslRepository;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * LegacyReviewImageQueryAdapter - 레거시 리뷰 이미지 Query Adapter.
 *
 * <p>ReviewImageQueryPort 구현체. 레거시 DB에서 리뷰 이미지를 조회합니다.
 *
 * <p>PER-ADP-003: QueryAdapter는 QueryDslRepository + Mapper에만 의존.
 *
 * <p>PER-ADP-005: Entity -> Domain 변환 (Mapper 사용).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyReviewImageQueryAdapter implements ReviewImageQueryPort {

    private final LegacyReviewImageQueryDslRepository queryDslRepository;
    private final LegacyReviewEntityMapper mapper;

    public LegacyReviewImageQueryAdapter(
            LegacyReviewImageQueryDslRepository queryDslRepository,
            LegacyReviewEntityMapper mapper) {
        this.queryDslRepository = queryDslRepository;
        this.mapper = mapper;
    }

    @Override
    public ReviewImages fetchByReviewId(long reviewId) {
        List<LegacyReviewImageEntity> entities = queryDslRepository.fetchByReviewId(reviewId);
        List<ReviewImage> images = entities.stream().map(mapper::toImageDomain).toList();
        return ReviewImages.of(images);
    }
}
