package com.ryuqq.setof.adapter.in.rest.admin.v2.shippingpolicy.dto.response;

import com.ryuqq.setof.application.shippingpolicy.dto.response.ShippingPolicyResponse;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 배송 정책 API 응답 DTO
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
 * @since 2.0.0
 */
@Schema(description = "배송 정책 응답")
public record ShippingPolicyV2ApiResponse(
        @Schema(description = "배송 정책 ID", example = "1") Long shippingPolicyId,
        @Schema(description = "셀러 ID", example = "1") Long sellerId,
        @Schema(description = "정책명", example = "기본 배송 정책") String policyName,
        @Schema(description = "기본 배송비", example = "3000") Integer defaultDeliveryCost,
        @Schema(description = "무료 배송 기준 금액", example = "50000") Integer freeShippingThreshold,
        @Schema(description = "배송 안내", example = "주문 후 2-3일 내 배송") String deliveryGuide,
        @Schema(description = "기본 정책 여부", example = "true") boolean isDefault,
        @Schema(description = "표시 순서", example = "1") Integer displayOrder) {

    /**
     * Application Response를 API Response로 변환
     *
     * @param response Application 계층 응답
     * @return API 응답 DTO
     */
    public static ShippingPolicyV2ApiResponse from(ShippingPolicyResponse response) {
        return new ShippingPolicyV2ApiResponse(
                response.shippingPolicyId(),
                response.sellerId(),
                response.policyName(),
                response.defaultDeliveryCost(),
                response.freeShippingThreshold(),
                response.deliveryGuide(),
                response.isDefault(),
                response.displayOrder());
    }
}
