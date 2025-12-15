package com.setof.connectly.module.exception.common;

import com.setof.connectly.module.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class TokenTypeException extends ApplicationException {

    public static final String CODE = "TOEKEN-401";
    public static final String MESSAGE = "잘못된 토큰 형식입니다.";

    public TokenTypeException() {
        super(CODE, HttpStatus.UNAUTHORIZED, MESSAGE);
    }
}
