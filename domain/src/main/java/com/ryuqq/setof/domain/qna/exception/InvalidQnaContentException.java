package com.ryuqq.setof.domain.qna.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;

public class InvalidQnaContentException extends DomainException {

    public InvalidQnaContentException() {
        super(QnaErrorCode.INVALID_QNA_CONTENT);
    }

    public InvalidQnaContentException(String reason) {
        super(QnaErrorCode.INVALID_QNA_CONTENT, reason);
    }
}
