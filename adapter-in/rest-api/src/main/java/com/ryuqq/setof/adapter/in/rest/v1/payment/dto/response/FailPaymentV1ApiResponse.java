package com.ryuqq.setof.adapter.in.rest.v1.payment.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * V1 결제 실패 Response
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "결제 실패 응답")
public record FailPaymentV1ApiResponse(
        @Schema(description = "결제 ID", example = "1") Long paymentId,
        @Schema(description = "결제 방법", example = "CARD") String paymentMethod,
        @Schema(description = "결제 금액", example = "90000") Long payAmount) {}
