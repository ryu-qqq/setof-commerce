package com.ryuqq.setof.storage.legacy.composite.payment.dto;

import java.time.LocalDateTime;

/**
 * LegacyPaymentOverviewFlatDto - 결제 목록 조회용 Flat Projection DTO.
 *
 * <p>다중 JOIN 쿼리의 결과를 1행으로 받기 위한 flat DTO입니다. Projections.constructor()로 생성됩니다.
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
 * @param orderId 주문 ID (orders 테이블 - 개별 행마다 1개)
 * @param orderStatus 주문 상태 (orders 테이블)
 * @param vBankName 가상계좌 은행명 (nullable)
 * @param vBankNumber 가상계좌 번호 (nullable)
 * @param vBankPaymentAmount 가상계좌 결제금액
 * @param vBankDueDate 가상계좌 입금 기한 (nullable)
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyPaymentOverviewFlatDto(
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
        // payment (추가 필드)
        long userId,
        String siteName,
        // payment_bill (결제 수단 ID → paymentMethodEnum 변환용)
        long paymentMethodId,
        // orders
        long orderId,
        String orderStatus,
        // vbank (nullable)
        String vBankName,
        String vBankNumber,
        long vBankPaymentAmount,
        LocalDateTime vBankDueDate) {}
