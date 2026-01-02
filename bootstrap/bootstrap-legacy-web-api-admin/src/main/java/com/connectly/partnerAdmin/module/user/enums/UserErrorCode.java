package com.connectly.partnerAdmin.module.user.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum UserErrorCode {

    // 400 BAD REQUEST
    USER_INVALID_WITHDRAWAL_USER("USER_001", HttpStatus.BAD_REQUEST),
    USER_INVALID_JOIN_USER("USER_002",HttpStatus.BAD_REQUEST),
    USER_INVALID_PASSWORD("USER_003",HttpStatus.BAD_REQUEST),
    USER_INVALID_LOGIN_TYPE("USER_004",HttpStatus.BAD_REQUEST),
    USER_INVALID_EXCESS_SHIPPING_ADDRESS("USER_005",HttpStatus.BAD_REQUEST),
    USER_INVALID_ACCOUNT("USER_006",HttpStatus.BAD_REQUEST),
    //404 NOT FOUND
    USER_NOT_FOUND("USER_008", HttpStatus.NOT_FOUND),


    ;

    private final String code;

    private final HttpStatus httpStatus;
}
