package com.ryuqq.setof.adapter.in.rest.v1.order.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * V1 주문 상태별 개수 Response
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "주문 상태별 개수 응답")
public record OrderCountV1ApiResponse(
        @Schema(description = "주문 상태", example = "COMPLETED") String orderStatus,
        @Schema(description = "개수", example = "5") Long count) {
}
