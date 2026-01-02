package com.connectly.partnerAdmin.module.external.exception;

import com.connectly.partnerAdmin.module.external.enums.ExternalMallErrorCode;
import com.connectly.partnerAdmin.module.order.enums.SiteName;

public class ExMallOrderCanceledException extends ExternalServerException{

    public ExMallOrderCanceledException(SiteName siteName, long externalIdx) {
        super(ExternalMallErrorCode.EXTERNAL_ALREADY_CANCELED_ORDER_MSG.getCode(),
                ExternalMallErrorCode.EXTERNAL_ALREADY_CANCELED_ORDER_MSG.getHttpStatus(),
                ExternalMallErrorConstant.EXTERNAL_ORDER_ALREADY_CANCELED + buildMessage(siteName, externalIdx));
    }

    private static String buildMessage(SiteName siteName, long externalIdx) {
        return String.format("Site %s, External Mall Order pk  %s", siteName.getDisplayName(), externalIdx);
    }


}
