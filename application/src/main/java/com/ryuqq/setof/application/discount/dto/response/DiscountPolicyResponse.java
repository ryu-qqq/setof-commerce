package com.ryuqq.setof.application.discount.dto.response;

import com.ryuqq.setof.domain.discount.vo.DiscountGroup;
import com.ryuqq.setof.domain.discount.vo.DiscountTargetType;
import com.ryuqq.setof.domain.discount.vo.DiscountType;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/**
 * 할인 정책 Response DTO
 *
 * @param discountPolicyId 할인 정책 ID
 * @param sellerId 셀러 ID
 * @param policyName 정책명
 * @param discountGroup 할인 그룹
 * @param discountType 할인 타입
 * @param discountTargetType 적용 대상 타입
 * @param targetId 적용 대상 ID (첫 번째 targetId, 하위 호환용)
 * @param targetIds 적용 대상 ID 목록
 * @param discountRate 할인율 (정률 할인 시)
 * @param fixedDiscountAmount 정액 할인 금액 (정액 할인 시)
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
 * @since 1.0.0
 */
public record DiscountPolicyResponse(
        Long discountPolicyId,
        Long sellerId,
        String policyName,
        DiscountGroup discountGroup,
        DiscountType discountType,
        DiscountTargetType discountTargetType,
        Long targetId,
        List<Long> targetIds,
        BigDecimal discountRate,
        Long fixedDiscountAmount,
        Long maximumDiscountAmount,
        Long minimumOrderAmount,
        Instant validStartAt,
        Instant validEndAt,
        Integer maxUsagePerCustomer,
        Integer maxTotalUsage,
        BigDecimal platformCostShareRatio,
        BigDecimal sellerCostShareRatio,
        int priority,
        boolean isActive,
        boolean isDeleted,
        boolean isCurrentlyValid,
        Instant createdAt,
        Instant updatedAt) {

    /**
     * Static Factory Method
     *
     * @return DiscountPolicyResponse
     */
    public static DiscountPolicyResponse of(
            Long discountPolicyId,
            Long sellerId,
            String policyName,
            DiscountGroup discountGroup,
            DiscountType discountType,
            DiscountTargetType discountTargetType,
            Long targetId,
            List<Long> targetIds,
            BigDecimal discountRate,
            Long fixedDiscountAmount,
            Long maximumDiscountAmount,
            Long minimumOrderAmount,
            Instant validStartAt,
            Instant validEndAt,
            Integer maxUsagePerCustomer,
            Integer maxTotalUsage,
            BigDecimal platformCostShareRatio,
            BigDecimal sellerCostShareRatio,
            int priority,
            boolean isActive,
            boolean isDeleted,
            boolean isCurrentlyValid,
            Instant createdAt,
            Instant updatedAt) {
        return new DiscountPolicyResponse(
                discountPolicyId,
                sellerId,
                policyName,
                discountGroup,
                discountType,
                discountTargetType,
                targetId,
                targetIds,
                discountRate,
                fixedDiscountAmount,
                maximumDiscountAmount,
                minimumOrderAmount,
                validStartAt,
                validEndAt,
                maxUsagePerCustomer,
                maxTotalUsage,
                platformCostShareRatio,
                sellerCostShareRatio,
                priority,
                isActive,
                isDeleted,
                isCurrentlyValid,
                createdAt,
                updatedAt);
    }
}
