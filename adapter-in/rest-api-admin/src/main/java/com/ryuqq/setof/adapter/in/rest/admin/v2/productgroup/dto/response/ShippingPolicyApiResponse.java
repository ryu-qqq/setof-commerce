package com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/** 배송 정책 API 응답 DTO. */
@Schema(description = "배송 정책 응답")
public record ShippingPolicyApiResponse(
        @Schema(description = "정책 ID", example = "1") Long policyId,
        @Schema(description = "셀러 ID", example = "1") Long sellerId,
        @Schema(description = "정책명", example = "기본 배송") String policyName,
        @Schema(description = "기본 정책 여부") boolean defaultPolicy,
        @Schema(description = "활성 여부") boolean active,
        @Schema(description = "배송비 유형") String shippingFeeType,
        @Schema(description = "배송비 유형 표시명") String shippingFeeTypeDisplayName,
        @Schema(description = "기본 배송비", example = "3000") int baseFee,
        @Schema(description = "무료 배송 기준 금액", nullable = true) Integer freeThreshold,
        @Schema(description = "제주 추가 배송비", example = "3000") int jejuExtraFee,
        @Schema(description = "도서산간 추가 배송비", example = "5000") int islandExtraFee,
        @Schema(description = "반품 배송비", example = "3000") int returnFee,
        @Schema(description = "교환 배송비", example = "6000") int exchangeFee,
        @Schema(description = "최소 배송일", example = "1") int leadTimeMinDays,
        @Schema(description = "최대 배송일", example = "3") int leadTimeMaxDays,
        @Schema(description = "생성일시 (ISO 8601)") String createdAt,
        @Schema(description = "수정일시 (ISO 8601)") String updatedAt) {}
