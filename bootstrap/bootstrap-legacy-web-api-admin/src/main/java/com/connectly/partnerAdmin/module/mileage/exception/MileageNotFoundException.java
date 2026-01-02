package com.connectly.partnerAdmin.module.mileage.exception;


import com.connectly.partnerAdmin.module.mileage.enums.MileageErrorCode;

public class MileageNotFoundException extends MileageException {

    public MileageNotFoundException() {
        super(MileageErrorCode.MILEAGE_NOT_FOUND.getCode(), MileageErrorCode.MILEAGE_NOT_FOUND.getHttpStatus(), MileageErrorConstant.MILEAGE_NOT_FOUND_MSG);
    }

}
