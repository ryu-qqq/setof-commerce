package com.ryuqq.setof.application.shippingpolicy.dto.response;

/**
 * 배송 정책 상세 Response DTO
 *
 * @param shippingPolicyId 배송 정책 ID
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
public record ShippingPolicyResponse(
        Long shippingPolicyId,
        Long sellerId,
        String policyName,
        Integer defaultDeliveryCost,
        Integer freeShippingThreshold,
        String deliveryGuide,
        boolean isDefault,
        Integer displayOrder) {

    public static ShippingPolicyResponse of(
            Long shippingPolicyId,
            Long sellerId,
            String policyName,
            Integer defaultDeliveryCost,
            Integer freeShippingThreshold,
            String deliveryGuide,
            boolean isDefault,
            Integer displayOrder) {
        return new ShippingPolicyResponse(
                shippingPolicyId,
                sellerId,
                policyName,
                defaultDeliveryCost,
                freeShippingThreshold,
                deliveryGuide,
                isDefault,
                displayOrder);
    }
}
