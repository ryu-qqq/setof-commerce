package com.ryuqq.setof.adapter.in.rest.admin.v2.shippingpolicy.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 배송 정책 수정 API 요청 DTO
 *
 * @param policyName 정책명
 * @param defaultDeliveryCost 기본 배송비
 * @param freeShippingThreshold 무료 배송 기준 금액 (nullable)
 * @param deliveryGuide 배송 안내 (nullable)
 * @param displayOrder 표시 순서
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "배송 정책 수정 요청")
public record UpdateShippingPolicyV2ApiRequest(
        @Schema(
                        description = "정책명",
                        example = "기본 배송 정책",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotBlank(message = "정책명은 필수입니다")
                @Size(max = 100, message = "정책명은 100자를 초과할 수 없습니다")
                String policyName,
        @Schema(
                        description = "기본 배송비",
                        example = "3000",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotNull(message = "기본 배송비는 필수입니다")
                @Min(value = 0, message = "기본 배송비는 0 이상이어야 합니다")
                Integer defaultDeliveryCost,
        @Schema(description = "무료 배송 기준 금액", example = "50000")
                @Min(value = 0, message = "무료 배송 기준 금액은 0 이상이어야 합니다")
                Integer freeShippingThreshold,
        @Schema(description = "배송 안내", example = "주문 후 2-3일 내 배송")
                @Size(max = 2000, message = "배송 안내는 2000자를 초과할 수 없습니다")
                String deliveryGuide,
        @Schema(description = "표시 순서", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
                @NotNull(message = "표시 순서는 필수입니다")
                @Min(value = 0, message = "표시 순서는 0 이상이어야 합니다")
                Integer displayOrder) {}
