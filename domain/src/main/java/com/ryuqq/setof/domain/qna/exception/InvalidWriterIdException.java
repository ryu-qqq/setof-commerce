package com.ryuqq.setof.domain.qna.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;

public class InvalidWriterIdException extends DomainException {

    public InvalidWriterIdException() {
        super(QnaErrorCode.INVALID_WRITER_ID);
    }
}
