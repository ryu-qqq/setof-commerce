package com.ryuqq.setof.domain.reviewimage.exception;

/**
 * 리뷰 이미지 개수 제한 초과 예외.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public class ReviewImageLimitExceededException extends ReviewImageException {

    private static final ReviewImageErrorCode ERROR_CODE =
            ReviewImageErrorCode.REVIEW_IMAGE_LIMIT_EXCEEDED;

    public ReviewImageLimitExceededException() {
        super(ERROR_CODE);
    }
}
