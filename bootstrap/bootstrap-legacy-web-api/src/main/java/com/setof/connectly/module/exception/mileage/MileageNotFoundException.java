package com.setof.connectly.module.exception.mileage;

import org.springframework.http.HttpStatus;

public class MileageNotFoundException extends MileageException {

    public static final String CODE = "MILEAGE-404";
    public static final String MESSAGE = "해당 마일리지를 찾을 수 없습니다";

    public MileageNotFoundException() {
        super(CODE, HttpStatus.NOT_FOUND, MESSAGE);
    }
}
