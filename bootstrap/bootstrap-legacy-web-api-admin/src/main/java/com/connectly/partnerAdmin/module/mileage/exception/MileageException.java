package com.connectly.partnerAdmin.module.mileage.exception;

import com.connectly.partnerAdmin.module.common.exception.CommonErrorConstant;
import com.connectly.partnerAdmin.module.exception.HasTitleApplicationException;
import org.springframework.http.HttpStatus;

public class MileageException extends HasTitleApplicationException {

    public MileageException(String errorCode, HttpStatus httpStatus, String message) {
        super(errorCode, httpStatus, message, CommonErrorConstant.ERROR_TITLE);
    }
}
