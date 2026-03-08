package com.ryuqq.setof.application.review.port.in.query;

import com.ryuqq.setof.application.review.dto.query.MyReviewSearchParams;
import com.ryuqq.setof.application.review.dto.response.ReviewSliceResult;

/**
 * 내 리뷰 조회 UseCase (Cursor 기반 페이징).
 *
 * <p>GET /api/v1/reviews/my-page/written 대응. 인증 필요 - 로그인 사용자 전용.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface GetMyReviewsUseCase {

    ReviewSliceResult execute(MyReviewSearchParams params);
}
