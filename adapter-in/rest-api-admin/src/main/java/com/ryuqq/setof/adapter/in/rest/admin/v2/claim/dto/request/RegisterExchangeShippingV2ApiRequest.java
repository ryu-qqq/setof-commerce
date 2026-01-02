package com.ryuqq.setof.adapter.in.rest.admin.v2.claim.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 * RegisterExchangeShippingV2ApiRequest - 교환품 발송 등록 API Request DTO
 *
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "교환품 발송 등록 요청")
public record RegisterExchangeShippingV2ApiRequest(
        @Schema(
                        description = "송장 번호",
                        example = "1234567890123",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotBlank(message = "송장 번호는 필수입니다")
                String trackingNumber,
        @Schema(description = "배송사 코드", example = "CJ", requiredMode = Schema.RequiredMode.REQUIRED)
                @NotBlank(message = "배송사는 필수입니다")
                String carrier) {}
