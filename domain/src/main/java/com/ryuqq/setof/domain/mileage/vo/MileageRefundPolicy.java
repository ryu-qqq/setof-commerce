package com.ryuqq.setof.domain.mileage.vo;

/**
 * 마일리지 환불 정책 인터페이스.
 *
 * <p>현재: 전체 취소 시만 반환, 부분 환불 시 미반환.
 *
 * <p>확장: 부분 환불 비율 계산, 만료 마일리지 복원 등.
 */
public interface MileageRefundPolicy {

    /**
     * 환불 시 반환할 마일리지 금액을 계산합니다.
     *
     * @param usedMileageAmount 원래 사용했던 마일리지 금액
     * @param totalOrderAmount 원래 주문 총액
     * @param refundAmount 실제 환불 금액
     * @param isFullCancel 전체 취소 여부
     * @return 반환할 마일리지 금액 (0이면 미반환)
     */
    long calculateRefundMileage(
            long usedMileageAmount, long totalOrderAmount, long refundAmount, boolean isFullCancel);
}
