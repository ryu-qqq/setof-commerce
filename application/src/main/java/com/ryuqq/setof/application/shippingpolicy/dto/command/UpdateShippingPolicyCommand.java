package com.ryuqq.setof.application.shippingpolicy.dto.command;

/**
 * 배송 정책 수정 Command DTO
 *
 * @param shippingPolicyId 배송 정책 ID
 * @param policyName 정책명
 * @param defaultDeliveryCost 기본 배송비
 * @param freeShippingThreshold 무료 배송 기준 금액 (nullable)
 * @param deliveryGuide 배송 안내 (nullable)
 * @param displayOrder 표시 순서
 * @author development-team
 * @since 1.0.0
 */
public record UpdateShippingPolicyCommand(
        Long shippingPolicyId,
        String policyName,
        int defaultDeliveryCost,
        Integer freeShippingThreshold,
        String deliveryGuide,
        int displayOrder) {}
