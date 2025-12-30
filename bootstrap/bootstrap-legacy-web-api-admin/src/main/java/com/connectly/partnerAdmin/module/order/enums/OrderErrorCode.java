package com.connectly.partnerAdmin.module.order.enums;

import com.connectly.partnerAdmin.module.common.enums.EnumType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum OrderErrorCode implements EnumType {

    // 400 BAD REQUEST
    INVALID_PRODUCT_GROUP("PRODUCT_001", HttpStatus.BAD_REQUEST),
    INVALID_ORDER_STATUS("ORDER_002",HttpStatus.BAD_REQUEST),

    //404 NOT FOUND
    ORDER_NOT_FOUND("ORDER_002", HttpStatus.NOT_FOUND),



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
