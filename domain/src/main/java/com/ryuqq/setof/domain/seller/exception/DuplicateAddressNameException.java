package com.ryuqq.setof.domain.seller.exception;

/** 주소명 중복 시 예외. */
public class DuplicateAddressNameException extends SellerException {

    private static final SellerErrorCode ERROR_CODE = SellerErrorCode.ADDRESS_ALREADY_EXISTS;

    public DuplicateAddressNameException() {
        super(ERROR_CODE);
    }
}
