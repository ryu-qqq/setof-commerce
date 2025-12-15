package com.setof.connectly.module.exception.communication;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import org.springframework.http.HttpStatus;

public class ExternalCommunicationException extends CommunicationException {

    public static final String CODE = "EXTERNAL_SERVER-500";
    public static final String MESSAGE = "외부 통신에 실패했습니다.";

    public ExternalCommunicationException(String message) {
        super(CODE, INTERNAL_SERVER_ERROR, MESSAGE + ":::" + message);
    }

    public ExternalCommunicationException(HttpStatus status, String message) {
        super(CODE, status, MESSAGE + ":::" + message);
    }
}
