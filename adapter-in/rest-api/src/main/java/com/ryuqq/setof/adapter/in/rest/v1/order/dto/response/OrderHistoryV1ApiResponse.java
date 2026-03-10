package com.ryuqq.setof.adapter.in.rest.v1.order.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * OrderHistoryV1ApiResponse - 주문 상태 변경 이력 응답 DTO.
 *
 * <p>레거시 OrderHistoryResponse 기반 변환.
 *
 * <p>API-DTO-001: Record 타입 필수.
 *
 * <p>API-DTO-002: DTO 불변성 보장.
 *
 * <p>Response DTO는 Domain 객체 의존 금지 -> Result만 의존해야 하며, 변환은 Mapper에서 수행합니다.
 *
 * @param orderId 주문 ID
 * @param changeReason 변경 사유
 * @param changeDetailReason 상세 변경 사유
 * @param orderStatus 주문 상태
 * @param invoiceNo 송장 번호
 * @param shipmentCompanyCode 배송 회사명
 * @param updateDate 변경 일시
 * @author ryu-qqq
 * @since 1.0.0
 * @see com.setof.connectly.module.order.dto.fetch.OrderHistoryResponse
 */
@Schema(description = "주문 상태 변경 이력 응답")
public record OrderHistoryV1ApiResponse(
        @Schema(description = "주문 ID", example = "12345") long orderId,
        @Schema(description = "변경 사유", example = "고객 요청") String changeReason,
        @Schema(description = "상세 변경 사유", example = "단순 변심") String changeDetailReason,
        @Schema(
                        description = "주문 상태",
                        example = "DELIVERY_COMPLETED",
                        allowableValues = {
                            "ORDER_FAILED",
                            "ORDER_PROCESSING",
                            "ORDER_COMPLETED",
                            "DELIVERY_PENDING",
                            "DELIVERY_PROCESSING",
                            "DELIVERY_COMPLETED",
                            "CANCEL_REQUEST",
                            "CANCEL_REQUEST_RECANT",
                            "CANCEL_REQUEST_REJECTED",
                            "CANCEL_REQUEST_CONFIRMED",
                            "SALE_CANCELLED",
                            "RETURN_REQUEST",
                            "RETURN_DELIVERY_PROCESSING",
                            "RETURN_REQUEST_CONFIRMED",
                            "RETURN_REQUEST_RECANT",
                            "RETURN_REQUEST_REJECTED",
                            "CANCEL_REQUEST_COMPLETED",
                            "SALE_CANCELLED_COMPLETED",
                            "RETURN_REQUEST_COMPLETED",
                            "SETTLEMENT_PROCESSING",
                            "SETTLEMENT_COMPLETED"
                        })
                String orderStatus,
        @Schema(description = "송장 번호", example = "1234567890", nullable = true) String invoiceNo,
        @Schema(description = "배송 회사명", example = "CJ대한통운") String shipmentCompanyCode,
        @Schema(description = "변경 일시", example = "2024-01-15 14:30:00") String updateDate) {}
