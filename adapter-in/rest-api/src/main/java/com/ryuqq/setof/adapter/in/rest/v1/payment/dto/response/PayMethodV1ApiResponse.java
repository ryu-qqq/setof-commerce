package com.ryuqq.setof.adapter.in.rest.v1.payment.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * V1 결제수단 Response
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "결제수단 응답")
public record PayMethodV1ApiResponse(
        @Schema(description = "표시명", example = "카드") String displayName,
        @Schema(
                        description = "결제 방법",
                        example = "CARD",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotNull(message = "결제 방법은 필수입니다.")
                String payMethod,
        @Schema(
                        description = "결제 방법 상점 키",
                        example = "merchant_key_123",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotNull(message = "결제 방법 상점 키는 필수입니다.")
                String paymentMethodMerchantKey) {}
