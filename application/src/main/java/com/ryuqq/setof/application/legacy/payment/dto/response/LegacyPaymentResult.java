package com.ryuqq.setof.application.legacy.payment.dto.response;

import java.time.LocalDateTime;

/**
 * LegacyPaymentResult - 레거시 결제 조회 결과 DTO.
 *
 * <p>APP-DTO-004: Response DTO는 *Result 네이밍.
 *
 * @param paymentId 결제 ID
 * @param userId 사용자 ID
 * @param paymentStatus 결제 상태
 * @param paymentAmount 결제 금액
 * @param usedMileageAmount 사용된 마일리지 금액
 * @param paymentAgencyId 결제 대행사 ID
 * @param paymentMethodEnum 결제 수단 Enum
 * @param paymentDate 결제 일시
 * @param canceledDate 취소 일시
 * @param siteName 사이트명
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyPaymentResult(
        long paymentId,
        long userId,
        String paymentStatus,
        long paymentAmount,
        double usedMileageAmount,
        String paymentAgencyId,
        String paymentMethodEnum,
        LocalDateTime paymentDate,
        LocalDateTime canceledDate,
        String siteName) {

    /**
     * 팩토리 메서드.
     *
     * @param paymentId 결제 ID
     * @param userId 사용자 ID
     * @param paymentStatus 결제 상태
     * @param paymentAmount 결제 금액
     * @param usedMileageAmount 사용된 마일리지 금액
     * @param paymentAgencyId 결제 대행사 ID
     * @param paymentMethodEnum 결제 수단 Enum
     * @param paymentDate 결제 일시
     * @param canceledDate 취소 일시
     * @param siteName 사이트명
     * @return LegacyPaymentResult
     */
    public static LegacyPaymentResult of(
            long paymentId,
            long userId,
            String paymentStatus,
            long paymentAmount,
            double usedMileageAmount,
            String paymentAgencyId,
            String paymentMethodEnum,
            LocalDateTime paymentDate,
            LocalDateTime canceledDate,
            String siteName) {
        return new LegacyPaymentResult(
                paymentId,
                userId,
                paymentStatus,
                paymentAmount,
                usedMileageAmount,
                paymentAgencyId,
                paymentMethodEnum,
                paymentDate,
                canceledDate,
                siteName);
    }
}
