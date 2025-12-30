package com.connectly.partnerAdmin.module.common.exception;

import com.connectly.partnerAdmin.module.common.enums.CommonErrorCode;


public class JsonSerializationException extends CommonException{

    public JsonSerializationException(String message) {
        super(CommonErrorCode.SYS_ERROR.getCode(),CommonErrorCode.SYS_ERROR.getHttpStatus(), CommonErrorConstant.FAIL_JSON_SERIALIZATION_MSG + message);
    }
}
