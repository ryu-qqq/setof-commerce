package com.ryuqq.setof.adapter.in.rest.v1.payment.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * V1 은행 Response
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "은행 응답")
public record BankV1ApiResponse(
        @Schema(description = "은행 코드", example = "SHINHAN") String bankCode,
        @Schema(description = "은행명", example = "신한은행") String bankName) {}
