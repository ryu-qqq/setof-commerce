package com.ryuqq.setof.application.review.port.in.query;

import com.ryuqq.setof.application.review.dto.query.ProductGroupReviewSearchParams;
import com.ryuqq.setof.application.review.dto.response.ReviewPageResult;

/**
 * 상품그룹 리뷰 조회 UseCase (Offset 기반 페이징).
 *
 * <p>GET /api/v1/reviews 대응. Public 엔드포인트 - 인증 불필요.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface GetProductGroupReviewsUseCase {

    ReviewPageResult execute(ProductGroupReviewSearchParams params);
}
