package com.ryuqq.setof.storage.legacy.composite.web.review.adapter;

import com.ryuqq.setof.application.legacy.review.dto.response.LegacyReviewPageResult;
import com.ryuqq.setof.application.legacy.review.dto.response.LegacyReviewResult;
import com.ryuqq.setof.application.legacy.review.dto.response.LegacyReviewSliceResult;
import com.ryuqq.setof.domain.legacy.review.dto.query.LegacyReviewSearchCondition;
import com.ryuqq.setof.storage.legacy.composite.web.review.dto.LegacyWebReviewImageQueryDto;
import com.ryuqq.setof.storage.legacy.composite.web.review.dto.LegacyWebReviewQueryDto;
import com.ryuqq.setof.storage.legacy.composite.web.review.mapper.LegacyWebReviewMapper;
import com.ryuqq.setof.storage.legacy.composite.web.review.repository.LegacyWebReviewCompositeQueryDslRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * 레거시 Web Review Composite 조회 Adapter.
 *
 * <p>TODO: Application Layer의 LegacyReviewCompositeQueryPort implements 추가
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyWebReviewCompositeQueryAdapter {

    private final LegacyWebReviewCompositeQueryDslRepository repository;
    private final LegacyWebReviewMapper mapper;

    public LegacyWebReviewCompositeQueryAdapter(
            LegacyWebReviewCompositeQueryDslRepository repository, LegacyWebReviewMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    /**
     * 상품그룹 리뷰 페이지 조회 (offset 페이징).
     *
     * <p>fetchProductGroupReviews 대응.
     *
     * @param condition 검색 조건
     * @return 리뷰 페이지 결과
     */
    public LegacyReviewPageResult fetchReviews(LegacyReviewSearchCondition condition) {
        List<Long> reviewIds = repository.fetchReviewIds(condition);

        if (reviewIds.isEmpty()) {
            return LegacyReviewPageResult.of(
                    0.0, List.of(), condition.pageNumber(), condition.pageSize(), 0L);
        }

        List<LegacyWebReviewQueryDto> reviewDtos = repository.fetchReviewsByIds(reviewIds);
        List<LegacyWebReviewImageQueryDto> imageDtos =
                repository.fetchReviewImagesByReviewIds(reviewIds);

        List<LegacyReviewResult> results = mapper.toResults(reviewDtos, imageDtos, List.of());

        long totalElements = repository.countReviews(condition.productGroupId(), null);
        double averageRating =
                repository.fetchAverageRating(condition.productGroupId()).orElse(0.0);

        return LegacyReviewPageResult.of(
                averageRating,
                results,
                condition.pageNumber(),
                condition.pageSize(),
                totalElements);
    }

    /**
     * 내 리뷰 슬라이스 조회 (커서 페이징).
     *
     * <p>fetchMyReviews 대응.
     *
     * @param condition 검색 조건
     * @return 리뷰 슬라이스 결과
     */
    public LegacyReviewSliceResult<LegacyReviewResult> fetchMyReviews(
            LegacyReviewSearchCondition condition) {
        List<Long> reviewIds = repository.fetchMyReviewIds(condition);

        if (reviewIds.isEmpty()) {
            return LegacyReviewSliceResult.of(
                    List.of(), condition.pageSize(), 0L, result -> result.reviewId());
        }

        List<Long> pageReviewIds =
                reviewIds.size() > condition.pageSize()
                        ? reviewIds.subList(0, condition.pageSize())
                        : reviewIds;

        List<LegacyWebReviewQueryDto> reviewDtos = repository.fetchReviewsByIds(pageReviewIds);
        List<LegacyWebReviewImageQueryDto> imageDtos =
                repository.fetchReviewImagesByReviewIds(pageReviewIds);

        List<LegacyReviewResult> results = mapper.toResults(reviewDtos, imageDtos, List.of());

        long totalElements = repository.countReviews(null, condition.userId());
        boolean hasNext = reviewIds.size() > condition.pageSize();

        Long lastDomainId = results.isEmpty() ? null : results.get(results.size() - 1).reviewId();

        return new LegacyReviewSliceResult<>(results, hasNext, totalElements, lastDomainId);
    }

    /**
     * 평균 평점 조회.
     *
     * @param productGroupId 상품그룹 ID
     * @return 평균 평점 (Optional)
     */
    public Optional<Double> fetchAverageRating(Long productGroupId) {
        return repository.fetchAverageRating(productGroupId);
    }

    /**
     * 리뷰 개수 조회.
     *
     * @param productGroupId 상품그룹 ID (nullable)
     * @param userId 사용자 ID (nullable)
     * @return 리뷰 개수
     */
    public long countReviews(Long productGroupId, Long userId) {
        return repository.countReviews(productGroupId, userId);
    }
}
