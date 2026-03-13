package com.ryuqq.setof.adapter.in.rest.admin.v2.productgroup.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/** 환불 정책 API 응답 DTO. */
@Schema(description = "환불 정책 응답")
public record RefundPolicyApiResponse(
        @Schema(description = "정책 ID", example = "1") Long policyId,
        @Schema(description = "셀러 ID", example = "1") Long sellerId,
        @Schema(description = "정책명", example = "기본 환불") String policyName,
        @Schema(description = "기본 정책 여부") boolean defaultPolicy,
        @Schema(description = "활성 여부") boolean active,
        @Schema(description = "반품 기간 (일)", example = "7") int returnPeriodDays,
        @Schema(description = "교환 기간 (일)", example = "7") int exchangePeriodDays,
        @Schema(description = "반품 불가 사유 목록")
                List<NonReturnableConditionApiResponse> nonReturnableConditions,
        @Schema(description = "부분 환불 가능 여부") boolean partialRefundEnabled,
        @Schema(description = "검수 필요 여부") boolean inspectionRequired,
        @Schema(description = "검수 기간 (일)", example = "3") int inspectionPeriodDays,
        @Schema(description = "추가 정보", nullable = true) String additionalInfo,
        @Schema(description = "생성일시 (ISO 8601)") String createdAt,
        @Schema(description = "수정일시 (ISO 8601)") String updatedAt) {}
