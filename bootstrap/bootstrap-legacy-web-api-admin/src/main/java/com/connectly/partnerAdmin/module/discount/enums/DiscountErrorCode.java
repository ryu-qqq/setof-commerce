package com.connectly.partnerAdmin.module.discount.enums;

import com.connectly.partnerAdmin.module.common.enums.EnumType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum DiscountErrorCode implements EnumType {

    //404 NOT FOUND
    DISCOUNT_NOT_FOUND("DISCOUNT_001", HttpStatus.NOT_FOUND);

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
