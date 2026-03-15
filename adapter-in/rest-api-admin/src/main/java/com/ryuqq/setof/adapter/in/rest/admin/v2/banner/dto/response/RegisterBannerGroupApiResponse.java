package com.ryuqq.setof.adapter.in.rest.admin.v2.banner.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * RegisterBannerGroupApiResponse - 배너 그룹 등록 API 응답.
 *
 * <p>API-DTO-001: Record 타입 필수.
 *
 * <p>API-DTO-002: DTO 불변성 보장.
 *
 * @param bannerGroupId 생성된 배너 그룹 ID
 * @author ryu-qqq
 * @since 1.1.0
 */
@Schema(description = "배너 그룹 등록 응답")
public record RegisterBannerGroupApiResponse(
        @Schema(description = "생성된 배너 그룹 ID", example = "1") Long bannerGroupId) {}
