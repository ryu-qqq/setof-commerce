package com.ryuqq.setof.adapter.in.rest.admin.v2.claim.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * UpdateReturnShippingStatusV2ApiRequest - 반품 배송 상태 업데이트 API Request DTO
 *
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "반품 배송 상태 업데이트 요청")
public record UpdateReturnShippingStatusV2ApiRequest(
        @Schema(
                        description = "새로운 배송 상태",
                        example = "IN_TRANSIT",
                        allowableValues = {
                            "PENDING",
                            "PICKUP_SCHEDULED",
                            "PICKED_UP",
                            "IN_TRANSIT",
                            "RECEIVED"
                        },
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotNull(message = "배송 상태는 필수입니다")
                String status,
        @Schema(
                        description = "송장 번호 (수거 완료 시 업데이트 가능)",
                        example = "1234567890123",
                        requiredMode = Schema.RequiredMode.NOT_REQUIRED)
                String trackingNumber) {}
