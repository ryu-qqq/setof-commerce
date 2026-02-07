package com.ryuqq.setof.application.legacy.order.dto.response;

import java.time.LocalDateTime;

/**
 * LegacyPaymentDetailResult - 레거시 결제 상세 정보 결과 DTO.
 *
 * @param paymentId 결제 ID
 * @param paymentAgencyId 결제 대행사 ID
 * @param paymentStatus 결제 상태
 * @param paymentMethodEnum 결제 수단 코드
 * @param paymentDate 결제 일시
 * @param canceledDate 취소 일시
 * @param userId 사용자 ID
 * @param siteName 사이트명
 * @param paymentAmount 결제 금액
 * @param usedMileageAmount 사용 마일리지
 * @param cardName 카드사명
 * @param cardNumber 카드 번호
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyPaymentDetailResult(
        long paymentId,
        String paymentAgencyId,
        String paymentStatus,
        String paymentMethodEnum,
        LocalDateTime paymentDate,
        LocalDateTime canceledDate,
        long userId,
        String siteName,
        long paymentAmount,
        double usedMileageAmount,
        String cardName,
        String cardNumber) {

    /** 정적 팩토리 메서드. */
    public static LegacyPaymentDetailResult of(
            long paymentId,
            String paymentAgencyId,
            String paymentStatus,
            String paymentMethodEnum,
            LocalDateTime paymentDate,
            LocalDateTime canceledDate,
            long userId,
            String siteName,
            long paymentAmount,
            double usedMileageAmount,
            String cardName,
            String cardNumber) {
        return new LegacyPaymentDetailResult(
                paymentId,
                paymentAgencyId,
                paymentStatus,
                paymentMethodEnum,
                paymentDate,
                canceledDate,
                userId,
                siteName,
                paymentAmount,
                usedMileageAmount,
                cardName,
                cardNumber);
    }
}
