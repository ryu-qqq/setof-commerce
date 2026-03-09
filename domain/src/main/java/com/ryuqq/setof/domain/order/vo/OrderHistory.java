package com.ryuqq.setof.domain.order.vo;

import java.time.LocalDateTime;

/**
 * 주문 이력 VO.
 *
 * <p>주문 상태 변경 이력과 배송 정보를 담는 불변 값 객체입니다.
 *
 * @param orderId 주문 ID
 * @param changeReason 변경 사유
 * @param changeDetailReason 상세 변경 사유
 * @param orderStatus 주문 상태 (레거시 OrderStatus 문자열)
 * @param invoiceNo 송장 번호
 * @param shipmentCompanyCode 배송 회사 코드
 * @param updateDate 변경 일시
 */
public record OrderHistory(
        long orderId,
        String changeReason,
        String changeDetailReason,
        String orderStatus,
        String invoiceNo,
        String shipmentCompanyCode,
        LocalDateTime updateDate) {

    public static OrderHistory of(
            long orderId,
            String changeReason,
            String changeDetailReason,
            String orderStatus,
            String invoiceNo,
            String shipmentCompanyCode,
            LocalDateTime updateDate) {
        return new OrderHistory(
                orderId,
                changeReason != null ? changeReason : "",
                changeDetailReason != null ? changeDetailReason : "",
                orderStatus,
                invoiceNo != null ? invoiceNo : "",
                shipmentCompanyCode != null ? shipmentCompanyCode : "",
                updateDate);
    }
}
