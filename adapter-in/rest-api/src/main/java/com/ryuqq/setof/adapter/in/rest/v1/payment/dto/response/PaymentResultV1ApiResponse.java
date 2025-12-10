package com.ryuqq.setof.adapter.in.rest.v1.payment.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * V1 결제 결과 Response
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "결제 결과 응답")
public record PaymentResultV1ApiResponse(
        @Schema(description = "결제 성공 여부", example = "true") Boolean isSuccess) {
}
