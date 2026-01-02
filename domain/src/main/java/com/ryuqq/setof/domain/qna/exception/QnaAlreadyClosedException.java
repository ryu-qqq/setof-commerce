package com.ryuqq.setof.domain.qna.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;

public class QnaAlreadyClosedException extends DomainException {

    public QnaAlreadyClosedException() {
        super(QnaErrorCode.QNA_ALREADY_CLOSED);
    }

    public QnaAlreadyClosedException(long qnaId) {
        super(QnaErrorCode.QNA_ALREADY_CLOSED, "QNA ID: " + qnaId);
    }
}
