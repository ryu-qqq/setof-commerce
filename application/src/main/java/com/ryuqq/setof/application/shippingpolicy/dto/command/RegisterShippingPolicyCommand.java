package com.ryuqq.setof.application.shippingpolicy.dto.command;

/**
 * 배송 정책 등록 Command DTO
 *
 * @param sellerId 셀러 ID
 * @param policyName 정책명
 * @param defaultDeliveryCost 기본 배송비
 * @param freeShippingThreshold 무료 배송 기준 금액 (nullable)
 * @param deliveryGuide 배송 안내 (nullable)
 * @param isDefault 기본 정책 여부
 * @param displayOrder 표시 순서
 * @author development-team
 * @since 1.0.0
 */
public record RegisterShippingPolicyCommand(
        Long sellerId,
        String policyName,
        int defaultDeliveryCost,
        Integer freeShippingThreshold,
        String deliveryGuide,
        boolean isDefault,
        int displayOrder) {}
