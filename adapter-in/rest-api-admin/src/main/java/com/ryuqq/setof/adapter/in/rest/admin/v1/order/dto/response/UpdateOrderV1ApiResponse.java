package com.ryuqq.setof.adapter.in.rest.admin.v1.order.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * V1 주문 수정 Response
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "주문 수정 응답")
public record UpdateOrderV1ApiResponse(@Schema(description = "주문 ID", example = "1") Long orderId,
        @Schema(description = "사용자 ID", example = "1") Long userId,
        @Schema(description = "변경될 주문 상태", example = "CANCELLED") String toBeOrderStatus,
        @Schema(description = "현재 주문 상태", example = "COMPLETED") String asIsOrderStatus,
        @Schema(description = "변경 사유",
                example = "고객 요청") @JsonInclude(JsonInclude.Include.NON_NULL) String changeReason,
        @Schema(description = "변경 상세 사유",
                example = "단순 변심") @JsonInclude(JsonInclude.Include.NON_NULL) String changeDetailReason) {
}
