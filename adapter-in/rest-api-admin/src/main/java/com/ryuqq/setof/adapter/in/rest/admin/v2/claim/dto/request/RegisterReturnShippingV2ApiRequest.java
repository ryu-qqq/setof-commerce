package com.ryuqq.setof.adapter.in.rest.admin.v2.claim.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * RegisterReturnShippingV2ApiRequest - 반품 배송 등록 API Request DTO
 *
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "반품 배송 등록 요청")
public record RegisterReturnShippingV2ApiRequest(
        @Schema(
                        description = "배송 방식",
                        example = "CUSTOMER_SHIP",
                        allowableValues = {
                            "SELLER_PICKUP",
                            "SELLER_PREPAID_LABEL",
                            "CUSTOMER_SHIP",
                            "CUSTOMER_VISIT"
                        },
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotNull(message = "배송 방식은 필수입니다")
                String shippingMethod,
        @Schema(
                        description = "송장 번호",
                        example = "1234567890123",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotBlank(message = "송장 번호는 필수입니다")
                String trackingNumber,
        @Schema(description = "배송사 코드", example = "CJ", requiredMode = Schema.RequiredMode.REQUIRED)
                @NotBlank(message = "배송사는 필수입니다")
                String carrier) {}
