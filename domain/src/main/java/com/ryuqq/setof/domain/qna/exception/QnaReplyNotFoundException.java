package com.ryuqq.setof.domain.qna.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;

public class QnaReplyNotFoundException extends DomainException {

    public QnaReplyNotFoundException() {
        super(QnaErrorCode.QNA_REPLY_NOT_FOUND);
    }

    public QnaReplyNotFoundException(long replyId) {
        super(QnaErrorCode.QNA_REPLY_NOT_FOUND, "Reply ID: " + replyId);
    }
}
