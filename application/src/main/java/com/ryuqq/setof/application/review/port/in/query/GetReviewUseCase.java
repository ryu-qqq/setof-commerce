package com.ryuqq.setof.application.review.port.in.query;

import com.ryuqq.setof.application.review.dto.response.ReviewResponse;

/**
 * Get Review UseCase (Query)
 *
 * <p>리뷰 단건 조회를 담당하는 Inbound Port
 *
 * @author development-team
 * @since 1.0.0
 */
public interface GetReviewUseCase {

    /**
     * 리뷰 ID로 단건 조회
     *
     * @param reviewId 리뷰 ID
     * @return 리뷰 상세 정보
     */
    ReviewResponse execute(Long reviewId);
}
