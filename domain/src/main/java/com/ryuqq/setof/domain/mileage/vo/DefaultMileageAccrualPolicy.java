package com.ryuqq.setof.domain.mileage.vo;

/**
 * 기본 마일리지 적립 정책.
 *
 * <p>결제 금액의 1% 적립, 30일 만료.
 */
public class DefaultMileageAccrualPolicy implements MileageAccrualPolicy {

    private static final double ACCRUAL_RATE = 0.01;
    private static final int EXPIRATION_DAYS = 30;

    @Override
    public long calculateAccrual(long paymentAmount) {
        return (long) (paymentAmount * ACCRUAL_RATE);
    }

    @Override
    public int expirationDays() {
        return EXPIRATION_DAYS;
    }
}
