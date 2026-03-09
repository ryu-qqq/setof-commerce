package com.ryuqq.setof.domain.order.vo;

import java.time.LocalDateTime;

/**
 * 주문 조회용 배송 정보 VO.
 *
 * <p>shipment 테이블의 배송 정보를 담는 불변 값 객체입니다.
 *
 * @param orderId 주문 ID
 * @param deliveryStatus 배송 상태
 * @param companyCode 배송사 코드
 * @param invoiceNo 송장 번호
 * @param insertDate 등록 일시
 */
public record OrderShipmentSummary(
        long orderId,
        String deliveryStatus,
        String companyCode,
        String invoiceNo,
        LocalDateTime insertDate) {

    public static OrderShipmentSummary of(
            long orderId,
            String deliveryStatus,
            String companyCode,
            String invoiceNo,
            LocalDateTime insertDate) {
        return new OrderShipmentSummary(
                orderId,
                deliveryStatus != null ? deliveryStatus : "",
                companyCode != null ? companyCode : "",
                invoiceNo != null ? invoiceNo : "",
                insertDate);
    }
}
