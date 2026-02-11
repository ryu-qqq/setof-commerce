package com.ryuqq.setof.domain.selleradmin.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;

/** 셀러 관리자 도메인 예외. */
public class SellerAdminException extends DomainException {

    public SellerAdminException(SellerAdminErrorCode errorCode) {
        super(errorCode);
    }

    public SellerAdminException(SellerAdminErrorCode errorCode, String customMessage) {
        super(errorCode, customMessage);
    }

    public SellerAdminException(SellerAdminErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
