package com.connectly.partnerAdmin.module.common.exception;

import com.connectly.partnerAdmin.module.common.enums.CommonErrorCode;

public class InvalidDateRangeException extends CommonException{

    public InvalidDateRangeException(String message) {
        super(CommonErrorCode.INVALID_DATE_RANGE.getCode(), CommonErrorCode.INVALID_DATE_RANGE.getHttpStatus(), message);
    }
}
