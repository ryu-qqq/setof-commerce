package com.ryuqq.setof.application.review.manager;

import com.ryuqq.setof.application.review.port.out.query.ReviewQueryPort;
import com.ryuqq.setof.domain.review.aggregate.Review;
import com.ryuqq.setof.domain.review.exception.ReviewNotFoundException;
import com.ryuqq.setof.domain.review.query.MyReviewSearchCriteria;
import com.ryuqq.setof.domain.review.query.ProductGroupReviewSearchCriteria;
import com.ryuqq.setof.domain.review.vo.WrittenReview;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * ReviewReadManager - 리뷰 조회 Manager.
 *
 * <p>ReviewQueryPort에 위임합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
@Transactional(readOnly = true)
public class ReviewReadManager {

    private final ReviewQueryPort reviewQueryPort;

    public ReviewReadManager(ReviewQueryPort reviewQueryPort) {
        this.reviewQueryPort = reviewQueryPort;
    }

    public List<WrittenReview> fetchProductGroupReviews(ProductGroupReviewSearchCriteria criteria) {
        return reviewQueryPort.fetchProductGroupReviews(criteria);
    }

    public long countProductGroupReviews(long productGroupId) {
        return reviewQueryPort.countProductGroupReviews(productGroupId);
    }

    public Optional<Double> fetchAverageRating(long productGroupId) {
        return reviewQueryPort.fetchAverageRating(productGroupId);
    }

    public List<WrittenReview> fetchMyReviews(MyReviewSearchCriteria criteria) {
        return reviewQueryPort.fetchMyReviews(criteria);
    }

    public long countMyReviews(MyReviewSearchCriteria criteria) {
        return reviewQueryPort.countMyReviews(criteria);
    }

    public boolean existsActiveReviewByOrderAndUser(long orderId, long userId) {
        return reviewQueryPort.existsActiveReviewByOrderAndUser(orderId, userId);
    }

    public Review getActiveReview(long reviewId, long userId) {
        return reviewQueryPort
                .fetchActiveReview(reviewId, userId)
                .orElseThrow(() -> new ReviewNotFoundException(reviewId));
    }
}
