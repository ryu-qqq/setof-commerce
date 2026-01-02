package com.connectly.partnerAdmin.module.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CommonErrorCode implements EnumType {

    // 400 BAD REQUEST
    INVALID_DATE_RANGE("COMMON_400", HttpStatus.BAD_REQUEST),
    INVALID_FILE_NAME("COMMON_400", HttpStatus.BAD_REQUEST),
    INVALID_PROVIDER_TYPE("COMMON_400", HttpStatus.BAD_REQUEST),
    BAD_REQUEST("COMMON_400", HttpStatus.BAD_REQUEST),

    // 500 SYS ERROR
    SYS_ERROR("COMMON_099", HttpStatus.INTERNAL_SERVER_ERROR),
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
