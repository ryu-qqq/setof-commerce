package com.ryuqq.setof.application.order.dto.response;

import com.ryuqq.setof.domain.order.vo.OrderHistory;
import java.time.LocalDateTime;

/**
 * OrderHistoryResult - 주문 이력 결과 DTO.
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
public record OrderHistoryResult(
        long orderId,
        String changeReason,
        String changeDetailReason,
        String orderStatus,
        String invoiceNo,
        String shipmentCompanyCode,
        LocalDateTime updateDate) {

    /**
     * Domain VO → Result 변환.
     *
     * @param vo OrderHistory 도메인 VO
     * @return OrderHistoryResult
     */
    public static OrderHistoryResult from(OrderHistory vo) {
        return new OrderHistoryResult(
                vo.orderId(),
                vo.changeReason(),
                vo.changeDetailReason(),
                vo.orderStatus(),
                vo.invoiceNo(),
                vo.shipmentCompanyCode(),
                vo.updateDate());
    }
}
