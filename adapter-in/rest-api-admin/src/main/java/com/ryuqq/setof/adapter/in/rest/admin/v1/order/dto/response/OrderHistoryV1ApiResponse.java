package com.ryuqq.setof.adapter.in.rest.admin.v1.order.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

/**
 * OrderHistoryV1ApiResponse - 주문 이력 응답 DTO.
 *
 * <p>레거시 OrderHistoryResponse 기반 변환.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Schema(description = "주문 이력 응답")
public record OrderHistoryV1ApiResponse(
        @Schema(description = "주문 ID", example = "12345") long orderId,
        @Schema(description = "변경 사유", example = "배송 시작") String changeReason,
        @Schema(description = "상세 변경 사유", example = "택배사 픽업 완료") String changeDetailReason,
        @Schema(description = "주문 상태", example = "DELIVERY_PROCESSING") String orderStatus,
        @Schema(description = "송장 번호", example = "1234567890") String invoiceNo,
        @Schema(description = "택배사 코드", example = "CJ") String shipmentCompanyCode,
        @Schema(description = "변경 일시", example = "2025-01-16 14:30:00") LocalDateTime updateDate) {}
