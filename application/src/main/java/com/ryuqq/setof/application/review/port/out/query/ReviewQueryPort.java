package com.ryuqq.setof.application.review.port.out.query;

import com.ryuqq.setof.domain.review.aggregate.Review;
import com.ryuqq.setof.domain.review.query.MyReviewSearchCriteria;
import com.ryuqq.setof.domain.review.query.ProductGroupReviewSearchCriteria;
import com.ryuqq.setof.domain.review.vo.WrittenReview;
import java.util.List;
import java.util.Optional;

/**
 * ReviewQueryPort - 리뷰 조회 Port.
 *
 * <p>리뷰 테이블 기반 조회를 담당합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface ReviewQueryPort {

    List<WrittenReview> fetchMyReviews(MyReviewSearchCriteria criteria);

    long countMyReviews(MyReviewSearchCriteria criteria);

    List<WrittenReview> fetchProductGroupReviews(ProductGroupReviewSearchCriteria criteria);

    long countProductGroupReviews(long productGroupId);

    Optional<Double> fetchAverageRating(long productGroupId);

    /**
     * 해당 주문에 대해 활성 리뷰가 존재하는지 확인합니다.
     *
     * @param orderId 레거시 주문 ID
     * @param userId 레거시 회원 ID
     * @return 리뷰 존재 여부
     */
    boolean existsActiveReviewByOrderAndUser(long orderId, long userId);

    /**
     * 리뷰 ID + 회원 ID로 활성 리뷰를 조회합니다.
     *
     * @param reviewId 리뷰 ID
     * @param userId 레거시 회원 ID
     * @return 리뷰 도메인 객체 (없으면 empty)
     */
    Optional<Review> fetchActiveReview(long reviewId, long userId);
}
