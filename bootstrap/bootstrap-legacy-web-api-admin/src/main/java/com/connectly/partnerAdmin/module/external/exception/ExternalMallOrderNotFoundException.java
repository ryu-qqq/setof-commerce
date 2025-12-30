package com.connectly.partnerAdmin.module.external.exception;

import com.connectly.partnerAdmin.module.external.enums.ExternalMallErrorCode;

public class ExternalMallOrderNotFoundException extends ExternalServerException {

    public ExternalMallOrderNotFoundException(String siteName, long externalIdx) {
        super(ExternalMallErrorCode.EXTERNAL_ORDER_NOT_FOUND.getCode(),
                ExternalMallErrorCode.EXTERNAL_ORDER_NOT_FOUND.getHttpStatus(),
                ExternalMallErrorConstant.EXTERNAL_ORDER_NOT_FOUND_MSG + siteName +" "+  externalIdx);
    }

}
