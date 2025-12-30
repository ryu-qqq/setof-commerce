package com.connectly.partnerAdmin.module.common.exception;

import com.connectly.partnerAdmin.module.common.enums.CommonErrorCode;
import org.springframework.http.HttpStatus;

public class EmptyRequestBodyException extends CommonException{

    public EmptyRequestBodyException() {
        super(CommonErrorCode.BAD_REQUEST.getCode(), CommonErrorCode.BAD_REQUEST.getHttpStatus(), CommonErrorConstant.EMPTY_REQUEST_BODY_MSG);
    }
}
