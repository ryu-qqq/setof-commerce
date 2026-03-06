package com.ryuqq.setof.adapter.in.rest.admin.v2.seller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * SellerApiResponse - 셀러 목록 조회 응답.
 *
 * <p>API-DTO-001: Record 타입 필수.
 *
 * <p>API-DTO-005: 날짜 String 변환 필수 (Instant 타입 사용 금지).
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Schema(description = "셀러 응답")
public record SellerApiResponse(
        @Schema(description = "셀러 ID", example = "1") Long id,
        @Schema(description = "셀러명") String sellerName,
        @Schema(description = "표시명") String displayName,
        @Schema(description = "로고 URL") String logoUrl,
        @Schema(description = "설명") String description,
        @Schema(description = "활성화 여부") boolean active,
        @Schema(description = "생성일시 (ISO 8601)", example = "2025-01-26T10:30:00+09:00")
                String createdAt,
        @Schema(description = "수정일시 (ISO 8601)", example = "2025-01-26T10:30:00+09:00")
                String updatedAt) {}
