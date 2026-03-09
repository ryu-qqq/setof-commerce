package com.ryuqq.setof.domain.payment.vo;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * 결제 목록 조회용 도메인 VO.
 *
 * <p>레거시 fetchPayments 결과를 담습니다. 목록 조회이므로 buyerInfo/receiverInfo는 포함하지 않습니다.
 *
 * @param paymentId 결제 ID
 * @param paymentStatus 결제 상태 (문자열)
 * @param paymentMethod 결제수단 표시명
 * @param paymentDate 결제일시
 * @param canceledDate 취소일시
 * @param paymentAmount 결제금액
 * @param usedMileageAmount 사용 마일리지
 * @param paymentAgencyId PG사 거래 ID
 * @param cardName 카드사명
 * @param cardNumber 카드번호
 * @param orderIds 해당 결제에 묶인 주문 ID 목록 (GROUP BY 결과)
 * @param vBankName 가상계좌 은행명 (nullable)
 * @param vBankNumber 가상계좌 번호 (nullable)
 * @param vBankPaymentAmount 가상계좌 결제금액
 * @param vBankDueDate 가상계좌 입금 기한 (nullable)
 * @author ryu-qqq
 * @since 1.1.0
 */
public record PaymentOverview(
        long paymentId,
        String paymentStatus,
        String paymentMethod,
        LocalDateTime paymentDate,
        LocalDateTime canceledDate,
        long paymentAmount,
        double usedMileageAmount,
        String paymentAgencyId,
        String cardName,
        String cardNumber,
        Set<Long> orderIds,
        String vBankName,
        String vBankNumber,
        long vBankPaymentAmount,
        LocalDateTime vBankDueDate) {

    public static PaymentOverview of(
            long paymentId,
            String paymentStatus,
            String paymentMethod,
            LocalDateTime paymentDate,
            LocalDateTime canceledDate,
            long paymentAmount,
            double usedMileageAmount,
            String paymentAgencyId,
            String cardName,
            String cardNumber,
            Set<Long> orderIds,
            String vBankName,
            String vBankNumber,
            long vBankPaymentAmount,
            LocalDateTime vBankDueDate) {
        return new PaymentOverview(
                paymentId,
                paymentStatus,
                paymentMethod,
                paymentDate,
                canceledDate,
                paymentAmount,
                usedMileageAmount,
                paymentAgencyId,
                cardName,
                cardNumber,
                orderIds,
                vBankName,
                vBankNumber,
                vBankPaymentAmount,
                vBankDueDate);
    }
}
