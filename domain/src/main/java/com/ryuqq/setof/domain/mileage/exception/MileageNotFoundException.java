package com.ryuqq.setof.domain.mileage.exception;

/** 마일리지 원장을 찾을 수 없을 때 발생하는 예외. */
public class MileageNotFoundException extends MileageException {

    public MileageNotFoundException(long userId) {
        super(
                MileageErrorCode.MILEAGE_NOT_FOUND,
                String.format("마일리지 원장을 찾을 수 없습니다: userId=%d", userId));
    }
}
