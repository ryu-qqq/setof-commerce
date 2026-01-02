package com.connectly.partnerAdmin.module.qna.exception;

import com.connectly.partnerAdmin.module.qna.enums.QnaErrorCode;

public class InvalidQnaException extends QnaException{

    public InvalidQnaException(String message) {
        super(QnaErrorCode.INVALID_QNA.getCode(), QnaErrorCode.INVALID_QNA.getHttpStatus(), message);
    }
}
