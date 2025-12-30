package com.connectly.partnerAdmin.module.qna.exception;


import com.connectly.partnerAdmin.module.qna.enums.QnaErrorCode;

public class QnaNotFoundException extends QnaException{

    public QnaNotFoundException() {
        super(QnaErrorCode.QNA_NOT_FOUND.getCode(), QnaErrorCode.QNA_NOT_FOUND.getHttpStatus(), QnaErrorConstant.QNA_NOT_FOUND_MSG);
    }

}
