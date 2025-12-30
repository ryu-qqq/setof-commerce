package com.ryuqq.setof.adapter.in.rest.admin.v2.discount.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * 할인 정책 수정 API 요청 DTO
 *
 * <p>sellerId와 discountPolicyId는 URL PathVariable로 전달됩니다.
 *
 * <p>모든 필드는 nullable이며, 변경하고자 하는 필드만 전달합니다.
 *
 * @param policyName 정책명 (nullable)
 * @param maximumDiscountAmount 최대 할인 금액 (nullable)
 * @param minimumOrderAmount 최소 주문 금액 (nullable)
 * @param validEndAt 유효 기간 종료일 (nullable, 연장 시 사용)
 * @param maxUsagePerCustomer 고객당 최대 사용 횟수 (nullable)
 * @param maxTotalUsage 전체 최대 사용 횟수 (nullable)
 * @param platformCostShareRatio 플랫폼 비용 분담 비율 (nullable)
 * @param sellerCostShareRatio 셀러 비용 분담 비율 (nullable)
 * @param priority 우선순위 (nullable)
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "할인 정책 수정 요청")
public record UpdateDiscountPolicyV2ApiRequest(
        @Schema(description = "정책명", example = "수정된 할인 정책")
                @Size(max = 100, message = "정책명은 100자를 초과할 수 없습니다")
                String policyName,
        @Schema(description = "최대 할인 금액", example = "20000")
                @Min(value = 0, message = "최대 할인 금액은 0 이상이어야 합니다")
                Long maximumDiscountAmount,
        @Schema(description = "최소 주문 금액", example = "50000")
                @Min(value = 0, message = "최소 주문 금액은 0 이상이어야 합니다")
                Long minimumOrderAmount,
        @Schema(description = "유효 기간 종료일 (연장 시 사용)", example = "2026-12-31T23:59:59Z")
                Instant validEndAt,
        @Schema(description = "고객당 최대 사용 횟수", example = "2")
                @Min(value = 1, message = "고객당 최대 사용 횟수는 1 이상이어야 합니다")
                Integer maxUsagePerCustomer,
        @Schema(description = "전체 최대 사용 횟수", example = "2000")
                @Min(value = 1, message = "전체 최대 사용 횟수는 1 이상이어야 합니다")
                Integer maxTotalUsage,
        @Schema(description = "플랫폼 비용 분담 비율 (0-100)", example = "60")
                BigDecimal platformCostShareRatio,
        @Schema(description = "셀러 비용 분담 비율 (0-100)", example = "40")
                BigDecimal sellerCostShareRatio,
        @Schema(description = "우선순위 (1-1000)", example = "50")
                @Min(value = 1, message = "우선순위는 1 이상이어야 합니다")
                @Max(value = 1000, message = "우선순위는 1000을 초과할 수 없습니다")
                Integer priority) {}
