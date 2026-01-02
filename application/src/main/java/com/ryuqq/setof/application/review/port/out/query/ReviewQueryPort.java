package com.ryuqq.setof.application.review.port.out.query;

import com.ryuqq.setof.domain.review.aggregate.Review;
import com.ryuqq.setof.domain.review.query.criteria.ReviewSearchCriteria;
import com.ryuqq.setof.domain.review.vo.ReviewId;
import java.util.List;
import java.util.Optional;

/**
 * Review Query Port (Query)
 *
 * <p>Review Aggregate를 조회하는 읽기 전용 Port
 *
 * @author development-team
 * @since 1.0.0
 */
public interface ReviewQueryPort {

    /**
     * ID로 Review 단건 조회
     *
     * @param id Review ID (Value Object)
     * @return Review Domain (Optional)
     */
    Optional<Review> findById(ReviewId id);

    /**
     * 검색 조건으로 리뷰 목록 조회
     *
     * @param criteria 검색 조건 (Domain Criteria)
     * @return Review 목록
     */
    List<Review> findByCriteria(ReviewSearchCriteria criteria);

    /**
     * 검색 조건에 맞는 리뷰 총 개수 조회
     *
     * @param criteria 검색 조건 (Domain Criteria)
     * @return 리뷰 총 개수
     */
    long countByCriteria(ReviewSearchCriteria criteria);

    /**
     * 주문 ID와 상품 그룹 ID로 리뷰 존재 여부 확인
     *
     * @param orderId 주문 ID
     * @param productGroupId 상품 그룹 ID
     * @return 존재 여부
     */
    boolean existsByOrderIdAndProductGroupId(Long orderId, Long productGroupId);

    /**
     * Review ID 존재 여부 확인
     *
     * @param id Review ID
     * @return 존재 여부
     */
    boolean existsById(ReviewId id);
}
