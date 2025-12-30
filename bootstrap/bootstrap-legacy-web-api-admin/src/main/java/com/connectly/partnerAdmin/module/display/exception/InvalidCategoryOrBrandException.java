package com.connectly.partnerAdmin.module.display.exception;


import com.connectly.partnerAdmin.module.display.enums.ContentErrorCode;

public class InvalidCategoryOrBrandException extends ContentException {


    public static final org.springframework.http.HttpStatus HttpStatus = org.springframework.http.HttpStatus.BAD_REQUEST;

    public InvalidCategoryOrBrandException(String message) {
        super(ContentErrorCode.INVALID_CONTENT.getCode(), ContentErrorCode.INVALID_CONTENT.getHttpStatus(), message);
    }

}
