package com.ryuqq.setof.domain.seller.exception;

/** 기본 주소 삭제 시도 시 예외. */
public class CannotDeleteDefaultAddressException extends SellerException {

    private static final SellerErrorCode ERROR_CODE = SellerErrorCode.DEFAULT_ADDRESS_REQUIRED;

    public CannotDeleteDefaultAddressException() {
        super(ERROR_CODE);
    }
}
