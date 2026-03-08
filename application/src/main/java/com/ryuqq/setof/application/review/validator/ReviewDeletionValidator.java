package com.ryuqq.setof.application.review.validator;

import com.ryuqq.setof.application.review.manager.ReviewReadManager;
import com.ryuqq.setof.domain.review.aggregate.Review;
import org.springframework.stereotype.Component;

/**
 * ReviewDeletionValidator - 리뷰 삭제 검증.
 *
 * <p>리뷰 ID + 회원 ID로 활성 리뷰를 조회합니다. 존재하지 않으면 ReviewNotFoundException을 발생시킵니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class ReviewDeletionValidator {

    private final ReviewReadManager readManager;

    public ReviewDeletionValidator(ReviewReadManager readManager) {
        this.readManager = readManager;
    }

    /**
     * 활성 리뷰를 조회하여 반환합니다.
     *
     * @param reviewId 리뷰 ID
     * @param userId 레거시 회원 ID
     * @return 조회된 Review 도메인 객체
     * @throws com.ryuqq.setof.domain.review.exception.ReviewNotFoundException 리뷰가 없는 경우
     */
    public Review getExistingReview(long reviewId, long userId) {
        return readManager.getActiveReview(reviewId, userId);
    }
}
