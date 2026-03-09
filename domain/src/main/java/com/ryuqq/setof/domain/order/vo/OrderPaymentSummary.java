package com.ryuqq.setof.domain.order.vo;

import java.time.LocalDateTime;

/**
 * 주문 조회용 결제 요약 VO.
 *
 * <p>주문 목록에서 표시할 결제 정보를 담는 불변 값 객체입니다. payment + payment_bill + payment_method 테이블의 정보를 조합합니다.
 *
 * @param paymentId 결제 ID
 * @param paymentAgencyId PG사 주문번호
 * @param paymentStatus 결제 상태
 * @param paymentMethod 결제 수단 표시명
 * @param paymentDate 결제 일시
 * @param canceledDate 취소 일시
 * @param paymentAmount 결제 금액
 * @param usedMileageAmount 사용 마일리지
 * @param cardName 카드명
 * @param cardNumber 카드번호 (마스킹)
 */
public record OrderPaymentSummary(
        long paymentId,
        String paymentAgencyId,
        String paymentStatus,
        String paymentMethod,
        LocalDateTime paymentDate,
        LocalDateTime canceledDate,
        long paymentAmount,
        long usedMileageAmount,
        String cardName,
        String cardNumber) {

    public static OrderPaymentSummary of(
            long paymentId,
            String paymentAgencyId,
            String paymentStatus,
            String paymentMethod,
            LocalDateTime paymentDate,
            LocalDateTime canceledDate,
            long paymentAmount,
            long usedMileageAmount,
            String cardName,
            String cardNumber) {
        return new OrderPaymentSummary(
                paymentId,
                paymentAgencyId != null ? paymentAgencyId : "",
                paymentStatus != null ? paymentStatus : "",
                paymentMethod != null ? paymentMethod : "",
                paymentDate,
                canceledDate,
                paymentAmount,
                usedMileageAmount,
                cardName != null ? cardName : "",
                cardNumber != null ? cardNumber : "");
    }
}
