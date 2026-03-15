package com.ryuqq.setof.adapter.in.rest.admin.v2.navigation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * RegisterNavigationMenuApiResponse - 네비게이션 메뉴 등록 API 응답.
 *
 * <p>API-DTO-001: Record 타입 필수.
 *
 * <p>API-DTO-002: DTO 불변성 보장.
 *
 * @param navigationMenuId 생성된 네비게이션 메뉴 ID
 * @author ryu-qqq
 * @since 1.1.0
 */
@Schema(description = "네비게이션 메뉴 등록 응답")
public record RegisterNavigationMenuApiResponse(
        @Schema(description = "생성된 네비게이션 메뉴 ID", example = "1") Long navigationMenuId) {}
