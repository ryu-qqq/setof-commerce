package com.ryuqq.setof.application.legacy.order.dto.response;

import java.time.LocalDateTime;

/**
 * LegacyShipmentInfoResult - 레거시 배송 정보 결과 DTO.
 *
 * @param orderId 주문 ID
 * @param deliveryStatus 배송 상태
 * @param companyCode 배송 회사 코드
 * @param invoiceNo 송장 번호
 * @param insertDate 등록 일시
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyShipmentInfoResult(
        long orderId,
        String deliveryStatus,
        String companyCode,
        String invoiceNo,
        LocalDateTime insertDate) {

    /**
     * 정적 팩토리 메서드.
     *
     * @param orderId 주문 ID
     * @param deliveryStatus 배송 상태
     * @param companyCode 배송 회사 코드
     * @param invoiceNo 송장 번호
     * @param insertDate 등록 일시
     * @return LegacyShipmentInfoResult
     */
    public static LegacyShipmentInfoResult of(
            long orderId,
            String deliveryStatus,
            String companyCode,
            String invoiceNo,
            LocalDateTime insertDate) {
        return new LegacyShipmentInfoResult(
                orderId, deliveryStatus, companyCode, invoiceNo, insertDate);
    }
}
