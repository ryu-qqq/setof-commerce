package com.ryuqq.setof.domain.qna.exception;

/**
 * 동일 대상에 미답변 질문이 존재할 때 발생하는 예외.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public class QnaDuplicatePendingException extends QnaException {

    private static final QnaErrorCode ERROR_CODE = QnaErrorCode.QNA_DUPLICATE_PENDING;

    public QnaDuplicatePendingException(long userId) {
        super(ERROR_CODE, "userId=" + userId);
    }
}
