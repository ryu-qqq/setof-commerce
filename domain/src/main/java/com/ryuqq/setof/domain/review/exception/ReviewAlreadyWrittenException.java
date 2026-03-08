package com.ryuqq.setof.domain.review.exception;

/**
 * 이미 리뷰를 작성한 주문에 대해 중복 리뷰 작성 시도 시 발생하는 예외.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public class ReviewAlreadyWrittenException extends ReviewException {

    private static final ReviewErrorCode ERROR_CODE = ReviewErrorCode.REVIEW_ALREADY_WRITTEN;

    public ReviewAlreadyWrittenException() {
        super(ERROR_CODE);
    }

    public ReviewAlreadyWrittenException(long orderId) {
        super(ERROR_CODE, String.format("주문 %d에 대한 리뷰가 이미 존재합니다", orderId));
    }
}
