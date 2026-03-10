package com.ryuqq.setof.adapter.in.rest.v1.order.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * UpdateOrderV1ApiResponse - 주문 상태 변경 응답 DTO.
 *
 * <p>Legacy UpdateOrderResponse와 동일한 구조.
 *
 * @param orderId 주문 ID
 * @param userId 사용자 ID
 * @param toBeOrderStatus 변경 후 상태
 * @param asIsOrderStatus 변경 전 상태
 * @param changeReason 변경 사유 (null이면 JSON 미포함)
 * @param changeDetailReason 변경 상세 사유 (null이면 JSON 미포함)
 * @author ryu-qqq
 * @since 1.1.0
 */
@Schema(description = "주문 상태 변경 응답")
public record UpdateOrderV1ApiResponse(
        @Schema(description = "주문 ID") long orderId,
        @Schema(description = "사용자 ID") long userId,
        @Schema(description = "변경 후 상태") String toBeOrderStatus,
        @Schema(description = "변경 전 상태") String asIsOrderStatus,
        @JsonInclude(JsonInclude.Include.NON_NULL) @Schema(description = "변경 사유")
                String changeReason,
        @JsonInclude(JsonInclude.Include.NON_NULL) @Schema(description = "변경 상세 사유")
                String changeDetailReason) {}
