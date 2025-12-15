package com.setof.connectly.module.exception.common;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import com.setof.connectly.module.exception.ApplicationException;

public class ProviderMappingException extends ApplicationException {

    public static final String CODE = "PROVIDER_PATTERN-400";
    public static final String MESSAGE = "해당 프로바이더를 찾을 수 없습니다.";
    public static final org.springframework.http.HttpStatus HttpStatus = INTERNAL_SERVER_ERROR;

    public ProviderMappingException(String message) {
        super(CODE, HttpStatus, MESSAGE + ":::" + message);
    }
}
