package com.setof.connectly.module.exception.mileage;

import org.springframework.http.HttpStatus;

public class ExceedUserMileageException extends MileageException {

    public static final String CODE = "USER-MILEAGE-400";
    public static final String MESSAGE = "마일리지 사용량을 초과하였습니다.";

    public ExceedUserMileageException() {
        super(CODE, HttpStatus.BAD_REQUEST, MESSAGE);
    }
}
