package com.ryuqq.setof.adapter.in.rest.admin.v2.discount.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * 할인 정책 등록 API 요청 DTO
 *
 * <p>sellerId는 URL PathVariable로 전달됩니다.
 *
 * @param policyName 정책명
 * @param discountGroup 할인 그룹 (CART, PRODUCT, CATEGORY, BRAND, MEMBER, EVENT, SHIPPING, PAYMENT,
 *     BUNDLE)
 * @param discountType 할인 타입 (RATE, FIXED_PRICE)
 * @param discountTargetType 적용 대상 타입 (ALL, PRODUCT, CATEGORY, BRAND, MEMBER_GRADE)
 * @param targetId 적용 대상 ID (nullable, ALL인 경우 null)
 * @param discountRate 할인율 (정률 할인 시 사용, 1-100)
 * @param fixedDiscountAmount 정액 할인 금액 (정액 할인 시 사용)
 * @param maximumDiscountAmount 최대 할인 금액 (정률 할인 시 상한, nullable)
 * @param minimumOrderAmount 최소 주문 금액 (nullable, null이면 제한 없음)
 * @param validStartAt 유효 기간 시작일
 * @param validEndAt 유효 기간 종료일
 * @param maxUsagePerCustomer 고객당 최대 사용 횟수 (nullable, null이면 무제한)
 * @param maxTotalUsage 전체 최대 사용 횟수 (nullable, null이면 무제한)
 * @param platformCostShareRatio 플랫폼 비용 분담 비율 (0-100)
 * @param sellerCostShareRatio 셀러 비용 분담 비율 (0-100)
 * @param priority 우선순위 (1-1000, 낮을수록 높은 우선순위)
 * @param isActive 활성화 여부
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "할인 정책 등록 요청")
public record RegisterDiscountPolicyV2ApiRequest(
        @Schema(
                        description = "정책명",
                        example = "여름 시즌 할인",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotBlank(message = "정책명은 필수입니다")
                @Size(max = 100, message = "정책명은 100자를 초과할 수 없습니다")
                String policyName,
        @Schema(
                        description =
                                "할인 그룹 (CART, PRODUCT, CATEGORY, BRAND, MEMBER, EVENT, SHIPPING,"
                                        + " PAYMENT, BUNDLE)",
                        example = "PRODUCT",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotBlank(message = "할인 그룹은 필수입니다")
                String discountGroup,
        @Schema(
                        description = "할인 타입 (RATE: 정률, FIXED_PRICE: 정액)",
                        example = "RATE",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotBlank(message = "할인 타입은 필수입니다")
                String discountType,
        @Schema(
                        description = "적용 대상 타입 (ALL, PRODUCT, CATEGORY, BRAND, MEMBER_GRADE)",
                        example = "ALL",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotBlank(message = "적용 대상 타입은 필수입니다")
                String discountTargetType,
        @Schema(description = "적용 대상 ID (ALL인 경우 null)", example = "1") Long targetId,
        @Schema(description = "할인율 (정률 할인 시 사용, 1-100)", example = "10")
                @Min(value = 1, message = "할인율은 1 이상이어야 합니다")
                @Max(value = 100, message = "할인율은 100을 초과할 수 없습니다")
                Integer discountRate,
        @Schema(description = "정액 할인 금액 (정액 할인 시 사용)", example = "1000")
                @Min(value = 0, message = "정액 할인 금액은 0 이상이어야 합니다")
                Long fixedDiscountAmount,
        @Schema(description = "최대 할인 금액 (정률 할인 시 상한)", example = "10000")
                @Min(value = 0, message = "최대 할인 금액은 0 이상이어야 합니다")
                Long maximumDiscountAmount,
        @Schema(description = "최소 주문 금액", example = "30000")
                @Min(value = 0, message = "최소 주문 금액은 0 이상이어야 합니다")
                Long minimumOrderAmount,
        @Schema(
                        description = "유효 기간 시작일",
                        example = "2025-01-01T00:00:00Z",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotNull(message = "유효 기간 시작일은 필수입니다")
                Instant validStartAt,
        @Schema(
                        description = "유효 기간 종료일",
                        example = "2025-12-31T23:59:59Z",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotNull(message = "유효 기간 종료일은 필수입니다")
                Instant validEndAt,
        @Schema(description = "고객당 최대 사용 횟수", example = "1")
                @Min(value = 1, message = "고객당 최대 사용 횟수는 1 이상이어야 합니다")
                Integer maxUsagePerCustomer,
        @Schema(description = "전체 최대 사용 횟수", example = "1000")
                @Min(value = 1, message = "전체 최대 사용 횟수는 1 이상이어야 합니다")
                Integer maxTotalUsage,
        @Schema(
                        description = "플랫폼 비용 분담 비율 (0-100)",
                        example = "50",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotNull(message = "플랫폼 비용 분담 비율은 필수입니다")
                BigDecimal platformCostShareRatio,
        @Schema(
                        description = "셀러 비용 분담 비율 (0-100)",
                        example = "50",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotNull(message = "셀러 비용 분담 비율은 필수입니다")
                BigDecimal sellerCostShareRatio,
        @Schema(
                        description = "우선순위 (1-1000, 낮을수록 높은 우선순위)",
                        example = "100",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotNull(message = "우선순위는 필수입니다")
                @Min(value = 1, message = "우선순위는 1 이상이어야 합니다")
                @Max(value = 1000, message = "우선순위는 1000을 초과할 수 없습니다")
                Integer priority,
        @Schema(
                        description = "활성화 여부",
                        example = "true",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotNull(message = "활성화 여부는 필수입니다")
                Boolean isActive) {}
