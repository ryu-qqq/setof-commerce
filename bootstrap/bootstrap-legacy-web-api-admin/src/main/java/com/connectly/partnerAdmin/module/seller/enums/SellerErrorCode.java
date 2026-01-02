package com.connectly.partnerAdmin.module.seller.enums;

import com.connectly.partnerAdmin.module.common.enums.EnumType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SellerErrorCode implements EnumType {

    // 400 BAD REQUEST
    //404 NOT FOUND
    SELLER_NOT_FOUND("SELLER_001", HttpStatus.NOT_FOUND),
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
