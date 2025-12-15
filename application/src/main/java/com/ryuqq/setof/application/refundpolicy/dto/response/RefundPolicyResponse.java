package com.ryuqq.setof.application.refundpolicy.dto.response;

/**
 * 환불 정책 상세 Response DTO
 *
 * @param refundPolicyId 환불 정책 ID
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
 * @since 1.0.0
 */
public record RefundPolicyResponse(
        Long refundPolicyId,
        Long sellerId,
        String policyName,
        String returnAddressLine1,
        String returnAddressLine2,
        String returnZipCode,
        Integer refundPeriodDays,
        Integer refundDeliveryCost,
        String refundGuide,
        boolean isDefault,
        Integer displayOrder) {

    public static RefundPolicyResponse of(
            Long refundPolicyId,
            Long sellerId,
            String policyName,
            String returnAddressLine1,
            String returnAddressLine2,
            String returnZipCode,
            Integer refundPeriodDays,
            Integer refundDeliveryCost,
            String refundGuide,
            boolean isDefault,
            Integer displayOrder) {
        return new RefundPolicyResponse(
                refundPolicyId,
                sellerId,
                policyName,
                returnAddressLine1,
                returnAddressLine2,
                returnZipCode,
                refundPeriodDays,
                refundDeliveryCost,
                refundGuide,
                isDefault,
                displayOrder);
    }
}
