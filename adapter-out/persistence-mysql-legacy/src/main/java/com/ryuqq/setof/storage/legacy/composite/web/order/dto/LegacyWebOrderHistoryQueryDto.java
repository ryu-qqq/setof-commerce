package com.ryuqq.setof.storage.legacy.composite.web.order.dto;

import java.time.LocalDateTime;

/**
 * LegacyWebOrderHistoryQueryDto - 주문 이력 Projection DTO.
 *
 * <p>Projections.constructor()로 매핑 (@QueryProjection 금지).
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
public record LegacyWebOrderHistoryQueryDto(
        long orderId,
        String changeReason,
        String changeDetailReason,
        String orderStatus,
        String invoiceNo,
        String shipmentCompanyCode,
        LocalDateTime updateDate) {}
