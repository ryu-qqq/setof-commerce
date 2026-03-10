package com.ryuqq.setof.domain.mileage.exception;

/** 마일리지 잔액이 부족할 때 발생하는 예외. */
public class InsufficientMileageException extends MileageException {

    public InsufficientMileageException(long requested, long available) {
        super(
                MileageErrorCode.INSUFFICIENT_BALANCE,
                String.format("마일리지 잔액 부족: 요청=%d, 사용가능=%d", requested, available));
    }
}
