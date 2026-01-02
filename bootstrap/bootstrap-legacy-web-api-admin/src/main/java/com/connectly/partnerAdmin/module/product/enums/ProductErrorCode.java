package com.connectly.partnerAdmin.module.product.enums;

import com.connectly.partnerAdmin.module.common.enums.EnumType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ProductErrorCode implements EnumType {

    // 400 BAD REQUEST
    INVALID_PRODUCT_GROUP("PRODUCT_001",HttpStatus.BAD_REQUEST),
    INVALID_OPTION("PRODUCT_002",HttpStatus.BAD_REQUEST),
    PRODUCT_GROUP_NOT_ENOUGH_STOCK("PRODUCT_003", HttpStatus.BAD_REQUEST),

    //404 NOT FOUND
    PRODUCT_GROUP_NOT_FOUND("PRODUCT_004", HttpStatus.NOT_FOUND),



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
