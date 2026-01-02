package com.ryuqq.setof.adapter.in.rest.admin.v2.shipment.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * 운송장 상태 변경 요청 DTO
 *
 * @param status 새 배송 상태 (PENDING, IN_TRANSIT, OUT_FOR_DELIVERY, DELIVERED, CANCELLED)
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "운송장 상태 변경 요청")
public record ChangeStatusV2ApiRequest(
        @Schema(
                        description = "새 배송 상태",
                        example = "IN_TRANSIT",
                        requiredMode = Schema.RequiredMode.REQUIRED,
                        allowableValues = {
                            "PENDING",
                            "IN_TRANSIT",
                            "OUT_FOR_DELIVERY",
                            "DELIVERED",
                            "CANCELLED"
                        })
                @NotBlank(message = "상태는 필수입니다")
                @Pattern(
                        regexp = "^(PENDING|IN_TRANSIT|OUT_FOR_DELIVERY|DELIVERED|CANCELLED)$",
                        message = "유효하지 않은 상태입니다")
                String status) {}
