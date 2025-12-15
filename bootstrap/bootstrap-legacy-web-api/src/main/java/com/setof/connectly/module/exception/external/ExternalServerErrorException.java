package com.setof.connectly.module.exception.external;

import org.springframework.http.HttpStatus;

public class ExternalServerErrorException extends ExternalServerException {

    private static final String CODE = "EXTERNAL-SERVER-500";
    private static final String BASE_MESSAGE = "외부 서버와 통신 중 에러가 발생했습니다.. ";

    public ExternalServerErrorException(String message) {
        super(CODE, HttpStatus.BAD_REQUEST, BASE_MESSAGE + message);
    }
}
