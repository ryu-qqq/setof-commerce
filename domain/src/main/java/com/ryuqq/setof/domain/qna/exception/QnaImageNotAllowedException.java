package com.ryuqq.setof.domain.qna.exception;

/**
 * 이미지 첨부가 불가한 Q&A 유형에서 이미지를 첨부하려 할 때 발생하는 예외.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public class QnaImageNotAllowedException extends QnaException {

    private static final QnaErrorCode ERROR_CODE = QnaErrorCode.QNA_IMAGE_NOT_ALLOWED;

    public QnaImageNotAllowedException() {
        super(ERROR_CODE);
    }
}
