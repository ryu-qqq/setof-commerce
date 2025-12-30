package com.ryuqq.setof.domain.qna.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;

public class InvalidReplyPathException extends DomainException {

    public InvalidReplyPathException() {
        super(QnaErrorCode.INVALID_REPLY_PATH);
    }

    public InvalidReplyPathException(String path) {
        super(QnaErrorCode.INVALID_REPLY_PATH, "경로: " + path);
    }
}
