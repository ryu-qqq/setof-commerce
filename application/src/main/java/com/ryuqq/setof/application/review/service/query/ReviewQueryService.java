package com.ryuqq.setof.application.review.service.query;

import com.ryuqq.setof.application.common.response.PageResponse;
import com.ryuqq.setof.application.review.assembler.ReviewAssembler;
import com.ryuqq.setof.application.review.dto.query.ReviewSearchQuery;
import com.ryuqq.setof.application.review.dto.response.ReviewResponse;
import com.ryuqq.setof.application.review.dto.response.ReviewSummaryResponse;
import com.ryuqq.setof.application.review.factory.query.ReviewQueryFactory;
import com.ryuqq.setof.application.review.manager.query.ReviewReadManager;
import com.ryuqq.setof.application.review.port.in.query.GetReviewUseCase;
import com.ryuqq.setof.application.review.port.in.query.GetReviewsUseCase;
import com.ryuqq.setof.domain.review.aggregate.Review;
import com.ryuqq.setof.domain.review.query.criteria.ReviewSearchCriteria;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * 리뷰 조회 서비스
 *
 * <p>처리 순서:
 *
 * <ol>
 *   <li>ReviewQueryFactory로 Criteria 생성
 *   <li>ReviewReadManager로 리뷰 조회
 *   <li>ReviewAssembler로 Response DTO 변환
 * </ol>
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class ReviewQueryService implements GetReviewUseCase, GetReviewsUseCase {

    private final ReviewReadManager reviewReadManager;
    private final ReviewQueryFactory reviewQueryFactory;
    private final ReviewAssembler reviewAssembler;

    public ReviewQueryService(
            ReviewReadManager reviewReadManager,
            ReviewQueryFactory reviewQueryFactory,
            ReviewAssembler reviewAssembler) {
        this.reviewReadManager = reviewReadManager;
        this.reviewQueryFactory = reviewQueryFactory;
        this.reviewAssembler = reviewAssembler;
    }

    @Override
    public ReviewResponse execute(Long reviewId) {
        Review review = reviewReadManager.findById(reviewId);
        return reviewAssembler.toReviewResponse(review);
    }

    @Override
    public PageResponse<ReviewSummaryResponse> execute(ReviewSearchQuery query) {
        ReviewSearchCriteria criteria = reviewQueryFactory.create(query);

        List<Review> reviews = reviewReadManager.findByCriteria(criteria);
        long totalCount = reviewReadManager.countByCriteria(criteria);

        List<ReviewSummaryResponse> content = reviewAssembler.toReviewSummaryResponses(reviews);
        int totalPages = calculateTotalPages(totalCount, criteria.pageSize());
        boolean isFirst = criteria.page() == 0;
        boolean isLast = criteria.page() >= totalPages - 1;

        return PageResponse.of(
                content,
                criteria.page(),
                criteria.pageSize(),
                totalCount,
                totalPages,
                isFirst,
                isLast);
    }

    private int calculateTotalPages(long totalElements, int size) {
        return (int) Math.ceil((double) totalElements / size);
    }
}
