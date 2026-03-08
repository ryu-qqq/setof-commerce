package com.ryuqq.setof.application.review.manager;

import com.ryuqq.setof.application.review.port.out.query.ReviewImageQueryPort;
import com.ryuqq.setof.domain.reviewimage.aggregate.ReviewImages;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * ReviewImageReadManager - 리뷰 이미지 조회 Manager.
 *
 * <p>ReviewImageQueryPort에 위임합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class ReviewImageReadManager {

    private final ReviewImageQueryPort queryPort;

    public ReviewImageReadManager(ReviewImageQueryPort queryPort) {
        this.queryPort = queryPort;
    }

    @Transactional(readOnly = true)
    public ReviewImages fetchByReviewId(long reviewId) {
        return queryPort.fetchByReviewId(reviewId);
    }
}
