package com.ryuqq.setof.application.review.port.in.query;

import com.ryuqq.setof.application.review.dto.response.ProductRatingStatsResponse;

/**
 * Get Product Rating Stats UseCase (Query)
 *
 * <p>상품 평점 통계 조회를 담당하는 Inbound Port
 *
 * @author development-team
 * @since 1.0.0
 */
public interface GetProductRatingStatsUseCase {

    /**
     * 상품 그룹 ID로 평점 통계 조회
     *
     * @param productGroupId 상품 그룹 ID
     * @return 평점 통계 정보 (리뷰가 없으면 빈 통계 반환)
     */
    ProductRatingStatsResponse execute(Long productGroupId);
}
