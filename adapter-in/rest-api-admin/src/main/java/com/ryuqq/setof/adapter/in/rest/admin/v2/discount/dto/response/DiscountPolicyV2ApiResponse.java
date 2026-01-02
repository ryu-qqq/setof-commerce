package com.ryuqq.setof.adapter.in.rest.admin.v2.discount.dto.response;

import com.ryuqq.setof.application.discount.dto.response.DiscountPolicyResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * 할인 정책 API 응답 DTO
 *
 * @param discountPolicyId 할인 정책 ID
 * @param sellerId 셀러 ID
 * @param policyName 정책명
 * @param discountGroup 할인 그룹
 * @param discountType 할인 타입
 * @param discountTargetType 적용 대상 타입
 * @param targetId 적용 대상 ID
 * @param discountRate 할인율
 * @param fixedDiscountAmount 정액 할인 금액
 * @param maximumDiscountAmount 최대 할인 금액
 * @param minimumOrderAmount 최소 주문 금액
 * @param validStartAt 유효 기간 시작일
 * @param validEndAt 유효 기간 종료일
 * @param maxUsagePerCustomer 고객당 최대 사용 횟수
 * @param maxTotalUsage 전체 최대 사용 횟수
 * @param platformCostShareRatio 플랫폼 비용 분담 비율
 * @param sellerCostShareRatio 셀러 비용 분담 비율
 * @param priority 우선순위
 * @param isActive 활성화 여부
 * @param isDeleted 삭제 여부
 * @param isCurrentlyValid 현재 유효 여부
 * @param createdAt 생성일시
 * @param updatedAt 수정일시
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "할인 정책 응답")
public record DiscountPolicyV2ApiResponse(
        @Schema(description = "할인 정책 ID", example = "1") Long discountPolicyId,
        @Schema(description = "셀러 ID", example = "1") Long sellerId,
        @Schema(description = "정책명", example = "여름 시즌 할인") String policyName,
        @Schema(description = "할인 그룹", example = "PRODUCT") String discountGroup,
        @Schema(description = "할인 타입", example = "RATE") String discountType,
        @Schema(description = "적용 대상 타입", example = "ALL") String discountTargetType,
        @Schema(description = "적용 대상 ID", example = "1") Long targetId,
        @Schema(description = "할인율 (%)", example = "10") BigDecimal discountRate,
        @Schema(description = "정액 할인 금액", example = "1000") Long fixedDiscountAmount,
        @Schema(description = "최대 할인 금액", example = "10000") Long maximumDiscountAmount,
        @Schema(description = "최소 주문 금액", example = "30000") Long minimumOrderAmount,
        @Schema(description = "유효 기간 시작일", example = "2025-01-01T00:00:00Z") Instant validStartAt,
        @Schema(description = "유효 기간 종료일", example = "2025-12-31T23:59:59Z") Instant validEndAt,
        @Schema(description = "고객당 최대 사용 횟수", example = "1") Integer maxUsagePerCustomer,
        @Schema(description = "전체 최대 사용 횟수", example = "1000") Integer maxTotalUsage,
        @Schema(description = "플랫폼 비용 분담 비율 (%)", example = "50") BigDecimal platformCostShareRatio,
        @Schema(description = "셀러 비용 분담 비율 (%)", example = "50") BigDecimal sellerCostShareRatio,
        @Schema(description = "우선순위", example = "100") int priority,
        @Schema(description = "활성화 여부", example = "true") boolean isActive,
        @Schema(description = "삭제 여부", example = "false") boolean isDeleted,
        @Schema(description = "현재 유효 여부", example = "true") boolean isCurrentlyValid,
        @Schema(description = "생성일시", example = "2025-01-01T00:00:00Z") Instant createdAt,
        @Schema(description = "수정일시", example = "2025-01-01T00:00:00Z") Instant updatedAt) {

    /**
     * Application Response를 API Response로 변환
     *
     * @param response Application 계층 응답
     * @return API 응답 DTO
     */
    public static DiscountPolicyV2ApiResponse from(DiscountPolicyResponse response) {
        return new DiscountPolicyV2ApiResponse(
                response.discountPolicyId(),
                response.sellerId(),
                response.policyName(),
                response.discountGroup().name(),
                response.discountType().name(),
                response.discountTargetType().name(),
                response.targetId(),
                response.discountRate(),
                response.fixedDiscountAmount(),
                response.maximumDiscountAmount(),
                response.minimumOrderAmount(),
                response.validStartAt(),
                response.validEndAt(),
                response.maxUsagePerCustomer(),
                response.maxTotalUsage(),
                response.platformCostShareRatio(),
                response.sellerCostShareRatio(),
                response.priority(),
                response.isActive(),
                response.isDeleted(),
                response.isCurrentlyValid(),
                response.createdAt(),
                response.updatedAt());
    }
}
