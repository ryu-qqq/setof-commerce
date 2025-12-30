package com.connectly.partnerAdmin.module.common.exception;


import com.connectly.partnerAdmin.module.common.enums.CommonErrorCode;

public class UnExpectedException extends CommonException {

    public UnExpectedException(String message) {
        super(CommonErrorCode.SYS_ERROR.getCode(), CommonErrorCode.SYS_ERROR.getHttpStatus(), message);
    }

}
