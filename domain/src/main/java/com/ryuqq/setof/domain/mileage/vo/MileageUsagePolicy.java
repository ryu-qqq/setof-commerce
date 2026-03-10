package com.ryuqq.setof.domain.mileage.vo;

/**
 * 마일리지 사용 정책 인터페이스.
 *
 * <p>현재: 최소 1,000원, 결제금액의 최대 10%.
 *
 * <p>확장: 등급별 최대 사용 비율 변경 등.
 */
public interface MileageUsagePolicy {

    /** 최소 사용 금액. */
    long minUsageAmount();

    /**
     * 결제 금액 대비 최대 사용 가능 마일리지.
     *
     * @param paymentAmount 결제 금액
     * @return 최대 사용 가능 금액
     */
    long maxUsableAmount(long paymentAmount);
}
