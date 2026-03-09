package com.ryuqq.setof.storage.legacy.composite.mileage.dto;

import java.time.LocalDateTime;

/**
 * 레거시 Web MileageHistory 조회 Projection DTO.
 *
 * <p>Projections.constructor()로 매핑 (Q클래스 생성 불필요).
 *
 * @param mileageHistoryId 이력 ID
 * @param mileageId 마일리지 ID
 * @param title 마일리지 제목
 * @param paymentId 결제 ID (nullable)
 * @param orderId 주문 ID (nullable)
 * @param changeAmount 변동 금액
 * @param reason 사유 (SAVE, USE, REFUND, EXPIRED)
 * @param insertDate 적립/사용 일시
 * @param expirationDate 만료일
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyWebMileageHistoryQueryDto(
        long mileageHistoryId,
        long mileageId,
        String title,
        Long paymentId,
        Long orderId,
        double changeAmount,
        String reason,
        LocalDateTime insertDate,
        LocalDateTime expirationDate) {}
