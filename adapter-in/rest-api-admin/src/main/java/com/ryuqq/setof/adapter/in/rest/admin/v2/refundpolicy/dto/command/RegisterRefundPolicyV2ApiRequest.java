package com.ryuqq.setof.adapter.in.rest.admin.v2.refundpolicy.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 환불 정책 등록 API 요청 DTO
 *
 * @param sellerId 셀러 ID
 * @param policyName 정책명
 * @param returnAddressLine1 반품 주소 1
 * @param returnAddressLine2 반품 주소 2
 * @param returnZipCode 반품 우편번호
 * @param refundPeriodDays 환불 가능 기간 (일)
 * @param refundDeliveryCost 환불 배송비
 * @param refundGuide 환불 안내 (nullable)
 * @param isDefault 기본 정책 여부
 * @param displayOrder 표시 순서
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "환불 정책 등록 요청")
public record RegisterRefundPolicyV2ApiRequest(
        @Schema(description = "셀러 ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
                @NotNull(message = "셀러 ID는 필수입니다")
                Long sellerId,
        @Schema(
                        description = "정책명",
                        example = "기본 환불 정책",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotBlank(message = "정책명은 필수입니다")
                @Size(max = 100, message = "정책명은 100자를 초과할 수 없습니다")
                String policyName,
        @Schema(
                        description = "반품 주소 1",
                        example = "서울시 강남구",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotBlank(message = "반품 주소 1은 필수입니다")
                @Size(max = 200, message = "반품 주소 1은 200자를 초과할 수 없습니다")
                String returnAddressLine1,
        @Schema(description = "반품 주소 2", example = "역삼동 123-45")
                @Size(max = 200, message = "반품 주소 2는 200자를 초과할 수 없습니다")
                String returnAddressLine2,
        @Schema(
                        description = "반품 우편번호",
                        example = "06234",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotBlank(message = "반품 우편번호는 필수입니다")
                @Size(max = 10, message = "반품 우편번호는 10자를 초과할 수 없습니다")
                String returnZipCode,
        @Schema(
                        description = "환불 가능 기간 (일)",
                        example = "7",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotNull(message = "환불 가능 기간은 필수입니다")
                @Min(value = 1, message = "환불 가능 기간은 1일 이상이어야 합니다")
                Integer refundPeriodDays,
        @Schema(
                        description = "환불 배송비",
                        example = "3000",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotNull(message = "환불 배송비는 필수입니다")
                @Min(value = 0, message = "환불 배송비는 0 이상이어야 합니다")
                Integer refundDeliveryCost,
        @Schema(description = "환불 안내", example = "상품 수령 후 7일 이내 교환/환불 가능")
                @Size(max = 2000, message = "환불 안내는 2000자를 초과할 수 없습니다")
                String refundGuide,
        @Schema(
                        description = "기본 정책 여부",
                        example = "true",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotNull(message = "기본 정책 여부는 필수입니다")
                Boolean isDefault,
        @Schema(description = "표시 순서", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
                @NotNull(message = "표시 순서는 필수입니다")
                @Min(value = 0, message = "표시 순서는 0 이상이어야 합니다")
                Integer displayOrder) {}
