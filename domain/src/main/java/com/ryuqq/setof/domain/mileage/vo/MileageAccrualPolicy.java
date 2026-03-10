package com.ryuqq.setof.domain.mileage.vo;

/**
 * 마일리지 적립 정책 인터페이스.
 *
 * <p>현재: 결제금액의 고정 1%.
 *
 * <p>확장: 등급별 차등 적립률, 프로모션 적립 등.
 */
public interface MileageAccrualPolicy {

    /**
     * 적립할 마일리지 금액을 계산합니다.
     *
     * @param paymentAmount 결제 금액
     * @return 적립 마일리지 금액
     */
    long calculateAccrual(long paymentAmount);

    /** 적립 만료까지의 일수. */
    int expirationDays();
}
