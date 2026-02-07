package com.ryuqq.setof.storage.legacy.composite.web.payment.dto;

import java.time.LocalDateTime;

/**
 * LegacyWebPaymentQueryDto - 레거시 웹 결제 조회 Projection DTO.
 *
 * <p>Projections.constructor()로 매핑 (Q클래스 생성 불필요).
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
public record LegacyWebPaymentQueryDto(
        long paymentId,
        long userId,
        String paymentStatus,
        long paymentAmount,
        double usedMileageAmount,
        String paymentAgencyId,
        String paymentMethodEnum,
        LocalDateTime paymentDate,
        LocalDateTime canceledDate,
        String siteName) {}
