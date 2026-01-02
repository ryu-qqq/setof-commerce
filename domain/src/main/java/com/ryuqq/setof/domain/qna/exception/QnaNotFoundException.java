package com.ryuqq.setof.domain.qna.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;

public class QnaNotFoundException extends DomainException {

    public QnaNotFoundException() {
        super(QnaErrorCode.QNA_NOT_FOUND);
    }

    public QnaNotFoundException(long qnaId) {
        super(QnaErrorCode.QNA_NOT_FOUND, "QNA ID: " + qnaId);
    }
}
