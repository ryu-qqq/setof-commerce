package com.ryuqq.setof.domain.qna.exception;

/**
 * Q&A 이미지 첨부 한도 초과 예외.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public class QnaImageLimitExceededException extends QnaException {

    private static final QnaErrorCode ERROR_CODE = QnaErrorCode.QNA_IMAGE_LIMIT_EXCEEDED;

    public QnaImageLimitExceededException() {
        super(ERROR_CODE);
    }
}
