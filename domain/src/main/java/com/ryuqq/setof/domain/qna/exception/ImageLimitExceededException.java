package com.ryuqq.setof.domain.qna.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;

public class ImageLimitExceededException extends DomainException {

    public ImageLimitExceededException() {
        super(QnaErrorCode.IMAGE_LIMIT_EXCEEDED);
    }

    public ImageLimitExceededException(int imageCount) {
        super(QnaErrorCode.IMAGE_LIMIT_EXCEEDED, "요청된 이미지 수: " + imageCount);
    }
}
