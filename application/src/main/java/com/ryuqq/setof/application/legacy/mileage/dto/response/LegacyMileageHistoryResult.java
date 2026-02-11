package com.ryuqq.setof.application.legacy.mileage.dto.response;

import java.time.LocalDateTime;

/**
 * 레거시 MileageHistory 조회 결과 DTO.
 *
 * <p>APP-DTO-004: Response DTO는 *Result 네이밍.
 *
 * @param mileageHistoryId 이력 ID
 * @param mileageId 마일리지 ID
 * @param title 이력 제목
 * @param paymentId 결제 ID
 * @param orderId 주문 ID
 * @param changeAmount 변동 금액
 * @param reason 사유 (SAVE, USE, REFUND, EXPIRED)
 * @param usedDate 사용/적립 일시
 * @param expirationDate 만료일
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyMileageHistoryResult(
        long mileageHistoryId,
        long mileageId,
        String title,
        Long paymentId,
        Long orderId,
        double changeAmount,
        String reason,
        LocalDateTime usedDate,
        LocalDateTime expirationDate) {

    /**
     * 사유별 제목 설정.
     *
     * <p>SAVE는 원본 title 유지, 나머지는 고정 문구로 대체.
     *
     * @return 사유별 제목이 적용된 새 인스턴스
     */
    public LegacyMileageHistoryResult withReasonTitle() {
        String resolvedTitle = resolveTitle();
        return new LegacyMileageHistoryResult(
                mileageHistoryId,
                mileageId,
                resolvedTitle,
                paymentId,
                orderId,
                changeAmount,
                reason,
                usedDate,
                expirationDate);
    }

    private String resolveTitle() {
        if ("USE".equalsIgnoreCase(reason)) {
            return "주문 결제 시 사용";
        }
        if ("EXPIRED".equalsIgnoreCase(reason)) {
            return "적립금 유효기간 만료";
        }
        if ("REFUND".equalsIgnoreCase(reason)) {
            return "적립금 사용주문 취소";
        }
        return title;
    }
}
