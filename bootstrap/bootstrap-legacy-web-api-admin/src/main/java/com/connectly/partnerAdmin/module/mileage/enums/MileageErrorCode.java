package com.connectly.partnerAdmin.module.mileage.enums;

import com.connectly.partnerAdmin.module.common.enums.EnumType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MileageErrorCode implements EnumType {

    //404 NOT FOUND
    MILEAGE_NOT_FOUND("MILEAGE_003", HttpStatus.NOT_FOUND),

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
