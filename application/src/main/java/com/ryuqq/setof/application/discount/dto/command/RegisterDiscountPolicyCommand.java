package com.ryuqq.setof.application.discount.dto.command;

import com.ryuqq.setof.domain.discount.vo.DiscountGroup;
import com.ryuqq.setof.domain.discount.vo.DiscountTargetType;
import com.ryuqq.setof.domain.discount.vo.DiscountType;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * 할인 정책 등록 Command DTO
 *
 * @param sellerId 셀러 ID
 * @param policyName 정책명
 * @param discountGroup 할인 그룹 (PRODUCT, MEMBER, PAYMENT)
 * @param discountType 할인 타입 (RATE, FIXED_PRICE, TIERED_RATE, TIERED_PRICE)
 * @param discountTargetType 적용 대상 타입 (ALL, PRODUCT, CATEGORY, SELLER, BRAND)
 * @param targetId 적용 대상 ID (nullable, ALL인 경우 null)
 * @param discountRate 할인율 (정률 할인 시 사용, nullable)
 * @param fixedDiscountAmount 정액 할인 금액 (정액 할인 시 사용, nullable)
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
 * @since 1.0.0
 */
public record RegisterDiscountPolicyCommand(
        Long sellerId,
        String policyName,
        DiscountGroup discountGroup,
        DiscountType discountType,
        DiscountTargetType discountTargetType,
        Long targetId,
        Integer discountRate,
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
        boolean isActive) {}
