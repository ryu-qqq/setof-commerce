package com.connectly.partnerAdmin.module.external.enums;

import com.connectly.partnerAdmin.module.common.enums.EnumType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ExternalMallErrorCode implements EnumType {

    // 400 BAD REQUEST
    EXTERNAL_ORDER_INVALID_STATUS("EXTERNAL_ORDER_400", HttpStatus.BAD_REQUEST),
    EXTERNAL_ALREADY_CANCELED_ORDER_MSG("EXTERNAL_ORDER_400", HttpStatus.BAD_REQUEST),

    //401
    EXTERNAL_SITE_INVALID_AUTHORIZATION("EXTERNAL_SITE_401", HttpStatus.UNAUTHORIZED),

    //404 NOT FOUND
    EXTERNAL_ORDER_NOT_FOUND("EXTERNAL_ORDER_404", HttpStatus.NOT_FOUND),
    EXTERNAL_PRODUCT_NOT_FOUND("EXTERNAL_PRODUCT_404", HttpStatus.NOT_FOUND),



    ;

    private final String code;

    private final HttpStatus httpStatus;

    @Override
    public String getName() {
        return code;
    }

    @Override
    public String getDescription() {
        return name();
    }



}
