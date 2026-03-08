package com.ryuqq.setof.application.review.port.out.query;

import com.ryuqq.setof.domain.reviewimage.aggregate.ReviewImages;

/**
 * ReviewImageQueryPort - 리뷰 이미지 조회 Port.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface ReviewImageQueryPort {

    /**
     * 리뷰 ID로 활성 이미지 목록을 조회합니다.
     *
     * @param reviewId 리뷰 ID
     * @return ReviewImages 일급 컬렉션
     */
    ReviewImages fetchByReviewId(long reviewId);
}
