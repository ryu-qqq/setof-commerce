package com.ryuqq.setof.domain.payment.vo;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * 결제 단건 상세 도메인 VO.
 *
 * <p>레거시 fetchPayment 결과를 담습니다. 모든 정보(결제 + 구매자 + 수령인 + 환불 계좌)를 포함합니다.
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
 * @param buyerName 구매자명
 * @param buyerEmail 구매자 이메일
 * @param buyerPhoneNumber 구매자 전화번호
 * @param receiverName 수령인명
 * @param receiverPhoneNumber 수령인 전화번호
 * @param addressLine1 배송지 주소 1
 * @param addressLine2 배송지 주소 2
 * @param zipCode 우편번호
 * @param country 국가
 * @param deliveryRequest 배송 요청사항
 * @param phoneNumber 주문자 전화번호
 * @param refundBankName 환불 은행명 (nullable)
 * @param refundAccountNumber 환불 계좌번호 (nullable)
 * @param refundAccountId 환불 계좌 ID
 * @param refundAccountHolderName 환불 계좌 예금주명 (nullable)
 * @author ryu-qqq
 * @since 1.1.0
 */
public record PaymentFullDetail(
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
        LocalDateTime vBankDueDate,
        String buyerName,
        String buyerEmail,
        String buyerPhoneNumber,
        String receiverName,
        String receiverPhoneNumber,
        String addressLine1,
        String addressLine2,
        String zipCode,
        String country,
        String deliveryRequest,
        String phoneNumber,
        String refundBankName,
        String refundAccountNumber,
        long refundAccountId,
        String refundAccountHolderName) {

    public static PaymentFullDetail of(
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
            LocalDateTime vBankDueDate,
            String buyerName,
            String buyerEmail,
            String buyerPhoneNumber,
            String receiverName,
            String receiverPhoneNumber,
            String addressLine1,
            String addressLine2,
            String zipCode,
            String country,
            String deliveryRequest,
            String phoneNumber,
            String refundBankName,
            String refundAccountNumber,
            long refundAccountId,
            String refundAccountHolderName) {
        return new PaymentFullDetail(
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
                vBankDueDate,
                buyerName,
                buyerEmail,
                buyerPhoneNumber,
                receiverName,
                receiverPhoneNumber,
                addressLine1,
                addressLine2,
                zipCode,
                country,
                deliveryRequest,
                phoneNumber,
                refundBankName,
                refundAccountNumber,
                refundAccountId,
                refundAccountHolderName);
    }
}
