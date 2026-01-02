package com.ryuqq.setof.application.review.port.in.query;

import com.ryuqq.setof.application.common.response.PageResponse;
import com.ryuqq.setof.application.review.dto.query.ReviewSearchQuery;
import com.ryuqq.setof.application.review.dto.response.ReviewSummaryResponse;

/**
 * Get Reviews UseCase (Query)
 *
 * <p>리뷰 목록 조회를 담당하는 Inbound Port
 *
 * @author development-team
 * @since 1.0.0
 */
public interface GetReviewsUseCase {

    /**
     * 조건으로 리뷰 목록 조회 (페이징)
     *
     * @param query 검색 조건
     * @return 리뷰 목록 (페이지 응답)
     */
    PageResponse<ReviewSummaryResponse> execute(ReviewSearchQuery query);
}
