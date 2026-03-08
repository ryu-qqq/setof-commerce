package com.ryuqq.setof.application.review.port.in.query;

import com.ryuqq.setof.application.review.dto.query.AvailableReviewSearchParams;
import com.ryuqq.setof.application.review.dto.response.AvailableReviewSliceResult;

/**
 * 작성 가능한 리뷰 주문 조회 UseCase (커서 기반 페이징).
 *
 * <p>인증 필요 - 로그인 사용자 전용. Order 도메인 데이터 기반 조회.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface GetAvailableReviewsUseCase {

    AvailableReviewSliceResult execute(AvailableReviewSearchParams params);
}
