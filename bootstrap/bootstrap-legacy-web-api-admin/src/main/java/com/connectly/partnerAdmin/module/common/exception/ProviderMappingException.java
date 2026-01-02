package com.connectly.partnerAdmin.module.common.exception;


import com.connectly.partnerAdmin.module.common.enums.CommonErrorCode;
import com.connectly.partnerAdmin.module.exception.ApplicationException;

public class ProviderMappingException extends ApplicationException {


    public ProviderMappingException(String message) {
        super(CommonErrorCode.INVALID_PROVIDER_TYPE.getCode(), CommonErrorCode.INVALID_PROVIDER_TYPE.getHttpStatus(), CommonErrorConstant.INVALID_PROVIDER_TYPE + message);
    }
}

