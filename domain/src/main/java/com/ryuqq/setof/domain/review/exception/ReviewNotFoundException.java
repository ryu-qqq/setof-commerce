package com.ryuqq.setof.domain.review.exception;

/**
 * 리뷰를 찾을 수 없는 경우 예외.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public class ReviewNotFoundException extends ReviewException {

    private static final ReviewErrorCode ERROR_CODE = ReviewErrorCode.REVIEW_NOT_FOUND;

    public ReviewNotFoundException() {
        super(ERROR_CODE);
    }

    public ReviewNotFoundException(Long reviewId) {
        super(ERROR_CODE, String.format("ID가 %d인 리뷰를 찾을 수 없습니다", reviewId));
    }
}
