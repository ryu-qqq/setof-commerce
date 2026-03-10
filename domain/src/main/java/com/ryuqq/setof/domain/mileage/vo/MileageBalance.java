package com.ryuqq.setof.domain.mileage.vo;

/**
 * 마일리지 잔액 요약 Value Object.
 *
 * @param totalEarned 누적 적립 금액
 * @param totalUsed 누적 사용 금액
 * @param totalExpired 누적 만료 금액
 * @param totalRevoked 누적 회수 금액
 */
public record MileageBalance(
        long totalEarned, long totalUsed, long totalExpired, long totalRevoked) {

    public static MileageBalance zero() {
        return new MileageBalance(0, 0, 0, 0);
    }

    /** 현재 사용 가능 잔액. */
    public long available() {
        return totalEarned - totalUsed - totalExpired - totalRevoked;
    }
}
