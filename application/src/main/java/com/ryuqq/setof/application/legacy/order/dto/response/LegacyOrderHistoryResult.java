package com.ryuqq.setof.application.legacy.order.dto.response;

import java.time.LocalDateTime;

/**
 * LegacyOrderHistoryResult - 레거시 주문 이력 결과 DTO.
 *
 * <p>APP-DTO-004: Response DTO는 *Result 네이밍.
 *
 * @param orderId 주문 ID
 * @param changeReason 변경 사유
 * @param changeDetailReason 상세 변경 사유
 * @param orderStatus 주문 상태
 * @param invoiceNo 송장 번호
 * @param shipmentCompanyCode 배송 회사 코드
 * @param updateDate 변경 일시
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyOrderHistoryResult(
        long orderId,
        String changeReason,
        String changeDetailReason,
        String orderStatus,
        String invoiceNo,
        String shipmentCompanyCode,
        LocalDateTime updateDate) {

    /**
     * 정적 팩토리 메서드.
     *
     * @param orderId 주문 ID
     * @param changeReason 변경 사유
     * @param changeDetailReason 상세 변경 사유
     * @param orderStatus 주문 상태
     * @param invoiceNo 송장 번호
     * @param shipmentCompanyCode 배송 회사 코드
     * @param updateDate 변경 일시
     * @return LegacyOrderHistoryResult
     */
    public static LegacyOrderHistoryResult of(
            long orderId,
            String changeReason,
            String changeDetailReason,
            String orderStatus,
            String invoiceNo,
            String shipmentCompanyCode,
            LocalDateTime updateDate) {
        return new LegacyOrderHistoryResult(
                orderId,
                changeReason,
                changeDetailReason,
                orderStatus,
                invoiceNo,
                shipmentCompanyCode,
                updateDate);
    }

    /**
     * 간단한 이력 생성 (상태 변경만).
     *
     * @param orderId 주문 ID
     * @param orderStatus 주문 상태
     * @param updateDate 변경 일시
     * @return LegacyOrderHistoryResult
     */
    public static LegacyOrderHistoryResult ofSimple(
            long orderId, String orderStatus, LocalDateTime updateDate) {
        return new LegacyOrderHistoryResult(orderId, "", "", orderStatus, "", "", updateDate);
    }
}
