package com.connectly.partnerAdmin.auth.enums;

import com.connectly.partnerAdmin.module.common.enums.EnumType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AuthErrorCode implements EnumType {

    // 400 BAD REQUEST
    AUTH_INVALID_PASSWORD("AUTH_001", HttpStatus.BAD_REQUEST),
    AUTH_INVALID_ROLE_TYPE("AUTH_002", HttpStatus.BAD_REQUEST),
    AUTH_INVALID_TOKEN_TYPE("AUTH_003", HttpStatus.BAD_REQUEST),

    // 401 UNAUTHORIZED
    TOKEN_EXPIRED("AUTH_004", HttpStatus.UNAUTHORIZED),
    AUTH_UNAUTHORIZED_USER("AUTH_005", HttpStatus.UNAUTHORIZED),

    // 404 NOT FOUND
    AUTH_USER_NOT_FOUND("AUTH_400", HttpStatus.NOT_FOUND);


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
