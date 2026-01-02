package com.ryuqq.setof.application.review.manager.query;

import com.ryuqq.setof.application.review.port.out.query.ReviewQueryPort;
import com.ryuqq.setof.domain.review.aggregate.Review;
import com.ryuqq.setof.domain.review.exception.ReviewNotFoundException;
import com.ryuqq.setof.domain.review.query.criteria.ReviewSearchCriteria;
import com.ryuqq.setof.domain.review.vo.ReviewId;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * Review Read Manager
 *
 * <p>Review 조회를 담당하는 Manager
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class ReviewReadManager {

    private final ReviewQueryPort reviewQueryPort;

    public ReviewReadManager(ReviewQueryPort reviewQueryPort) {
        this.reviewQueryPort = reviewQueryPort;
    }

    /**
     * ID로 Review 조회 (없으면 예외)
     *
     * @param reviewId Review ID
     * @return Review
     * @throws ReviewNotFoundException 리뷰가 존재하지 않으면
     */
    public Review findById(Long reviewId) {
        ReviewId id = ReviewId.of(reviewId);
        return reviewQueryPort
                .findById(id)
                .orElseThrow(() -> new ReviewNotFoundException(reviewId));
    }

    /**
     * 검색 조건으로 리뷰 목록 조회
     *
     * @param criteria 검색 조건 (Domain Criteria)
     * @return Review 목록
     */
    public List<Review> findByCriteria(ReviewSearchCriteria criteria) {
        return reviewQueryPort.findByCriteria(criteria);
    }

    /**
     * 검색 조건에 맞는 리뷰 총 개수 조회
     *
     * @param criteria 검색 조건 (Domain Criteria)
     * @return 리뷰 총 개수
     */
    public long countByCriteria(ReviewSearchCriteria criteria) {
        return reviewQueryPort.countByCriteria(criteria);
    }

    /**
     * 주문 ID와 상품 그룹 ID로 리뷰 존재 여부 확인
     *
     * @param orderId 주문 ID
     * @param productGroupId 상품 그룹 ID
     * @return 존재 여부
     */
    public boolean existsByOrderIdAndProductGroupId(Long orderId, Long productGroupId) {
        return reviewQueryPort.existsByOrderIdAndProductGroupId(orderId, productGroupId);
    }
}
