package com.ryuqq.setof.domain.mileage.vo;

/**
 * 기본 마일리지 환불 정책.
 *
 * <p>전체 취소 시에만 사용한 마일리지 전액 반환. 부분 환불 시 마일리지 반환 없음.
 */
public class DefaultMileageRefundPolicy implements MileageRefundPolicy {

    @Override
    public long calculateRefundMileage(
            long usedMileageAmount,
            long totalOrderAmount,
            long refundAmount,
            boolean isFullCancel) {
        if (!isFullCancel) {
            return 0;
        }
        return usedMileageAmount;
    }
}
