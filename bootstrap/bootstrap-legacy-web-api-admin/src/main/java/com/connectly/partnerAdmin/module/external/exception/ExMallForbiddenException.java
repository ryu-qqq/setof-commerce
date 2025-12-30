package com.connectly.partnerAdmin.module.external.exception;

import com.connectly.partnerAdmin.module.external.enums.ExternalMallErrorCode;

public class ExMallForbiddenException extends ExternalServerException{

    public ExMallForbiddenException() {
        super(ExternalMallErrorCode.EXTERNAL_SITE_INVALID_AUTHORIZATION.getCode(),
                ExternalMallErrorCode.EXTERNAL_SITE_INVALID_AUTHORIZATION.getHttpStatus(),
                ExternalMallErrorConstant.EXTERNAL_SITE_NOT_AUTHORIZATION);
    }

}
