package com.connectly.partnerAdmin.module.external.exception;

import com.connectly.partnerAdmin.module.external.enums.ExternalMallErrorCode;

public class ExternalMallProductNotFoundException extends ExternalServerException {

    public ExternalMallProductNotFoundException(String siteName, String externalIdx) {
        super(ExternalMallErrorCode.EXTERNAL_PRODUCT_NOT_FOUND.getCode(),
                ExternalMallErrorCode.EXTERNAL_PRODUCT_NOT_FOUND.getHttpStatus(),
                ExternalMallErrorConstant.EXTERNAL_PRODUCT_NOT_FOUND_MSG + siteName +" "+  externalIdx);
    }

}
