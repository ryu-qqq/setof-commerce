package com.ryuqq.setof.storage.legacy.payment.dto;

import java.time.LocalDateTime;

/**
 * LegacyPaymentDetailFlatDto - 결제 단건 상세 조회용 Flat Projection DTO.
 *
 * <p>다중 JOIN 쿼리의 결과를 1행으로 받기 위한 flat DTO입니다. Projections.constructor()로 생성됩니다.
 *
 * <p>주문 ID 목록은 별도 쿼리(fetchOrderIdsByPaymentId)로 조회합니다.
 *
 * <p>PER-DTO-001: Persistence DTO는 Record로 정의.
 *
 * @param paymentId 결제 ID
 * @param paymentStatus 결제 상태 (payment 테이블)
 * @param paymentMethod 결제 수단명 (payment_method 테이블)
 * @param paymentDate 결제 일시 (payment 테이블)
 * @param canceledDate 취소 일시 (payment 테이블)
 * @param paymentAmount 결제 금액 (payment_bill 테이블)
 * @param usedMileageAmount 사용 마일리지 금액 (payment_bill 테이블)
 * @param paymentAgencyId PG사 거래 ID (payment_bill 테이블)
 * @param cardName 카드사명 (payment_bill 테이블)
 * @param cardNumber 카드번호 (payment_bill 테이블)
 * @param vBankName 가상계좌 은행명 (nullable)
 * @param vBankNumber 가상계좌 번호 (nullable)
 * @param vBankPaymentAmount 가상계좌 결제금액
 * @param vBankDueDate 가상계좌 입금 기한 (nullable)
 * @param buyerName 구매자명 (payment_bill 테이블)
 * @param buyerEmail 구매자 이메일 (payment_bill 테이블)
 * @param buyerPhoneNumber 구매자 전화번호 (payment_bill 테이블)
 * @param receiverName 수령인명 (payment_snapshot_shipping_address 테이블)
 * @param receiverPhoneNumber 수령인 전화번호 (payment_snapshot_shipping_address 테이블)
 * @param addressLine1 배송지 주소 1 (payment_snapshot_shipping_address 테이블)
 * @param addressLine2 배송지 주소 2 (payment_snapshot_shipping_address 테이블)
 * @param zipCode 우편번호 (payment_snapshot_shipping_address 테이블)
 * @param country 국가 (payment_snapshot_shipping_address 테이블)
 * @param deliveryRequest 배송 요청사항 (payment_snapshot_shipping_address 테이블)
 * @param phoneNumber 주문자 전화번호 (users 테이블)
 * @param refundBankName 환불 은행명 (refund_account 테이블, nullable)
 * @param refundAccountNumber 환불 계좌번호 (refund_account 테이블, nullable)
 * @param refundAccountId 환불 계좌 ID (refund_account 테이블)
 * @param refundAccountHolderName 환불 계좌 예금주명 (refund_account 테이블, nullable)
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyPaymentDetailFlatDto(
        // payment
        long paymentId,
        String paymentStatus,
        // payment_method
        String paymentMethod,
        // payment
        LocalDateTime paymentDate,
        LocalDateTime canceledDate,
        // payment_bill
        long paymentAmount,
        long usedMileageAmount,
        String paymentAgencyId,
        String cardName,
        String cardNumber,
        // vbank (nullable)
        String vBankName,
        String vBankNumber,
        long vBankPaymentAmount,
        LocalDateTime vBankDueDate,
        // payment_bill - 구매자 정보
        String buyerName,
        String buyerEmail,
        String buyerPhoneNumber,
        // payment_snapshot_shipping_address - 수령인 정보
        String receiverName,
        String receiverPhoneNumber,
        String addressLine1,
        String addressLine2,
        String zipCode,
        String country,
        String deliveryRequest,
        // users - 주문자 전화번호
        String phoneNumber,
        // refund_account - 환불 계좌 정보 (nullable)
        String refundBankName,
        String refundAccountNumber,
        long refundAccountId,
        String refundAccountHolderName) {}
