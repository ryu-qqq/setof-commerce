package com.connectly.partnerAdmin.module.external.exception;

import com.connectly.partnerAdmin.module.external.enums.ExternalMallErrorCode;
import com.connectly.partnerAdmin.module.order.enums.SiteName;

public class InvalidExternalOrderStatusException extends ExternalServerException {

    public InvalidExternalOrderStatusException(SiteName siteName, String status) {
        super(
                ExternalMallErrorCode.EXTERNAL_ORDER_INVALID_STATUS.getCode(),
                ExternalMallErrorCode.EXTERNAL_ORDER_INVALID_STATUS.getHttpStatus(),
                ExternalMallErrorConstant.EXTERNAL_ORDER_INVALID_STATUS + buildMessage(siteName, status));
    }

    private static String buildMessage(SiteName siteName, String status) {
        return String.format("Site %s, External Mall Order Status  %s", siteName.getDisplayName(), status);
    }

}
