package com.ryuqq.setof.storage.legacy.composite.review.adapter;

import com.ryuqq.setof.application.review.port.out.query.ReviewQueryPort;
import com.ryuqq.setof.domain.review.aggregate.Review;
import com.ryuqq.setof.domain.review.query.MyReviewSearchCriteria;
import com.ryuqq.setof.domain.review.query.ProductGroupReviewSearchCriteria;
import com.ryuqq.setof.domain.review.vo.WrittenReview;
import com.ryuqq.setof.storage.legacy.composite.review.dto.LegacyWebReviewImageQueryDto;
import com.ryuqq.setof.storage.legacy.composite.review.dto.LegacyWebReviewOptionQueryDto;
import com.ryuqq.setof.storage.legacy.composite.review.dto.LegacyWebReviewQueryDto;
import com.ryuqq.setof.storage.legacy.composite.review.mapper.LegacyWebReviewMapper;
import com.ryuqq.setof.storage.legacy.composite.review.repository.LegacyWebReviewCompositeQueryDslRepository;
import com.ryuqq.setof.storage.legacy.review.mapper.LegacyReviewEntityMapper;
import com.ryuqq.setof.storage.legacy.review.repository.LegacyReviewQueryDslRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * 레거시 Web Review Composite 조회 Adapter.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyWebReviewCompositeQueryAdapter implements ReviewQueryPort {

    private final LegacyWebReviewCompositeQueryDslRepository repository;
    private final LegacyReviewQueryDslRepository reviewQueryDslRepository;
    private final LegacyWebReviewMapper mapper;
    private final LegacyReviewEntityMapper entityMapper;

    public LegacyWebReviewCompositeQueryAdapter(
            LegacyWebReviewCompositeQueryDslRepository repository,
            LegacyReviewQueryDslRepository reviewQueryDslRepository,
            LegacyWebReviewMapper mapper,
            LegacyReviewEntityMapper entityMapper) {
        this.repository = repository;
        this.reviewQueryDslRepository = reviewQueryDslRepository;
        this.mapper = mapper;
        this.entityMapper = entityMapper;
    }

    @Override
    public List<WrittenReview> fetchProductGroupReviews(ProductGroupReviewSearchCriteria criteria) {
        List<Long> reviewIds =
                repository.fetchReviewIds(
                        criteria.productGroupId(),
                        criteria.sortKey(),
                        criteria.offset(),
                        criteria.size());

        if (reviewIds.isEmpty()) {
            return List.of();
        }

        return fetchWrittenReviews(reviewIds);
    }

    @Override
    public long countProductGroupReviews(long productGroupId) {
        return repository.countReviews(productGroupId, null);
    }

    @Override
    public Optional<Double> fetchAverageRating(long productGroupId) {
        return repository.fetchAverageRating(productGroupId);
    }

    @Override
    public List<WrittenReview> fetchMyReviews(MyReviewSearchCriteria criteria) {
        Long userId = resolveUserId(criteria);
        List<Long> reviewIds =
                repository.fetchMyReviewIds(userId, criteria.cursor(), criteria.fetchSize());

        if (reviewIds.isEmpty()) {
            return List.of();
        }

        return fetchWrittenReviews(reviewIds);
    }

    @Override
    public long countMyReviews(MyReviewSearchCriteria criteria) {
        Long userId = resolveUserId(criteria);
        return repository.countReviews(null, userId);
    }

    private List<WrittenReview> fetchWrittenReviews(List<Long> reviewIds) {
        List<LegacyWebReviewQueryDto> reviewDtos = repository.fetchReviewsByIds(reviewIds);
        List<LegacyWebReviewImageQueryDto> imageDtos =
                repository.fetchReviewImagesByReviewIds(reviewIds);
        List<LegacyWebReviewOptionQueryDto> optionDtos =
                repository.fetchReviewOptionsByReviewIds(reviewIds);
        return mapper.toDomainList(reviewDtos, imageDtos, optionDtos);
    }

    @Override
    public boolean existsActiveReviewByOrderAndUser(long orderId, long userId) {
        return reviewQueryDslRepository.existsActiveReviewByOrderAndUser(orderId, userId);
    }

    @Override
    public Optional<Review> fetchActiveReview(long reviewId, long userId) {
        return reviewQueryDslRepository
                .fetchActiveReview(reviewId, userId)
                .map(entityMapper::toDomain);
    }

    private Long resolveUserId(MyReviewSearchCriteria criteria) {
        if (criteria.legacyMemberIdValue() != null) {
            return criteria.legacyMemberIdValue();
        }
        throw new IllegalStateException(
                "레거시 어댑터에서는 legacyMemberId가 필수입니다. memberId만으로는 조회할 수 없습니다.");
    }
}
