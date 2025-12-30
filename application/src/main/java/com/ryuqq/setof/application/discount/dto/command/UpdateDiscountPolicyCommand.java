package com.ryuqq.setof.application.discount.dto.command;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * 할인 정책 수정 Command DTO
 *
 * @param discountPolicyId 할인 정책 ID
 * @param sellerId 셀러 ID (권한 확인용)
 * @param policyName 정책명 (nullable - 변경하지 않으면 null)
 * @param maximumDiscountAmount 최대 할인 금액 (nullable)
 * @param minimumOrderAmount 최소 주문 금액 (nullable)
 * @param validEndAt 유효 기간 종료일 (nullable - 연장 시 사용)
 * @param maxUsagePerCustomer 고객당 최대 사용 횟수 (nullable)
 * @param maxTotalUsage 전체 최대 사용 횟수 (nullable)
 * @param platformCostShareRatio 플랫폼 비용 분담 비율 (nullable)
 * @param sellerCostShareRatio 셀러 비용 분담 비율 (nullable)
 * @param priority 우선순위 (nullable)
 * @author development-team
 * @since 1.0.0
 */
public record UpdateDiscountPolicyCommand(
        Long discountPolicyId,
        Long sellerId,
        String policyName,
        Long maximumDiscountAmount,
        Long minimumOrderAmount,
        Instant validEndAt,
        Integer maxUsagePerCustomer,
        Integer maxTotalUsage,
        BigDecimal platformCostShareRatio,
        BigDecimal sellerCostShareRatio,
        Integer priority) {}
