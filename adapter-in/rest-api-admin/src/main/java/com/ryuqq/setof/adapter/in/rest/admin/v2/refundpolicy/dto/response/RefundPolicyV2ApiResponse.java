package com.ryuqq.setof.adapter.in.rest.admin.v2.refundpolicy.dto.response;

import com.ryuqq.setof.application.refundpolicy.dto.response.RefundPolicyResponse;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 환불 정책 API 응답 DTO
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
 * @since 2.0.0
 */
@Schema(description = "환불 정책 응답")
public record RefundPolicyV2ApiResponse(
        @Schema(description = "환불 정책 ID", example = "1") Long refundPolicyId,
        @Schema(description = "셀러 ID", example = "1") Long sellerId,
        @Schema(description = "정책명", example = "기본 환불 정책") String policyName,
        @Schema(description = "반품 주소 1", example = "서울시 강남구") String returnAddressLine1,
        @Schema(description = "반품 주소 2", example = "역삼동 123-45") String returnAddressLine2,
        @Schema(description = "반품 우편번호", example = "06234") String returnZipCode,
        @Schema(description = "환불 가능 기간 (일)", example = "7") Integer refundPeriodDays,
        @Schema(description = "환불 배송비", example = "3000") Integer refundDeliveryCost,
        @Schema(description = "환불 안내", example = "상품 수령 후 7일 이내 교환/환불 가능") String refundGuide,
        @Schema(description = "기본 정책 여부", example = "true") boolean isDefault,
        @Schema(description = "표시 순서", example = "1") Integer displayOrder) {

    /**
     * Application Response를 API Response로 변환
     *
     * @param response Application 계층 응답
     * @return API 응답 DTO
     */
    public static RefundPolicyV2ApiResponse from(RefundPolicyResponse response) {
        return new RefundPolicyV2ApiResponse(
                response.refundPolicyId(),
                response.sellerId(),
                response.policyName(),
                response.returnAddressLine1(),
                response.returnAddressLine2(),
                response.returnZipCode(),
                response.refundPeriodDays(),
                response.refundDeliveryCost(),
                response.refundGuide(),
                response.isDefault(),
                response.displayOrder());
    }
}
