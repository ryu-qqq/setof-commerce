package com.setof.connectly.module.exception.external;

import com.setof.connectly.module.user.enums.SiteName;
import org.springframework.http.HttpStatus;

public class ExMallProductNotFoundException extends ExternalServerException {

    public static final String CODE = "EXMALL_PRODUCT-404";
    public static final String MESSAGE = "해당 상품은 연동 상품이 아닙니다.";

    public ExMallProductNotFoundException(SiteName siteName, long externalIdx) {
        super(CODE, HttpStatus.BAD_REQUEST, MESSAGE + buildMessage(siteName, externalIdx));
    }

    private static String buildMessage(SiteName siteName, long externalIdx) {
        return String.format("사이트 명 %s, 외부 몰 pk  %s", siteName.getDisplayName(), externalIdx);
    }
}
