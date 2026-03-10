package com.ryuqq.setof.domain.mileage.vo;

/**
 * 기본 마일리지 사용 정책.
 *
 * <p>최소 1,000원 단위, 결제 금액의 최대 10%.
 */
public class DefaultMileageUsagePolicy implements MileageUsagePolicy {

    private static final long MIN_USAGE = 1_000;
    private static final double MAX_USAGE_RATIO = 0.10;

    @Override
    public long minUsageAmount() {
        return MIN_USAGE;
    }

    @Override
    public long maxUsableAmount(long paymentAmount) {
        return (long) (paymentAmount * MAX_USAGE_RATIO);
    }
}
