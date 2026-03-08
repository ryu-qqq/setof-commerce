package com.ryuqq.setof.adapter.in.rest.v1.navigation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * NavigationMenuV1ApiResponse - 네비게이션 메뉴 응답 DTO.
 *
 * <p>API-DTO-001: Record 타입 필수.
 *
 * <p>API-DTO-002: DTO 불변성 보장.
 *
 * <p>API-DTO-003: Response는 @Schema 어노테이션.
 *
 * <p>displayOrder, displayPeriod 는 내부 Redis TTL 산출 및 정렬 전용 필드로 응답에서 제외됨.
 *
 * @param gnbId GNB 식별자
 * @param title 메뉴 타이틀
 * @param linkUrl 이동 링크 URL
 * @author ryu-qqq
 * @since 1.1.0
 */
@Schema(description = "네비게이션 메뉴 응답")
public record NavigationMenuV1ApiResponse(
        @Schema(description = "GNB ID", example = "1") long gnbId,
        @Schema(description = "메뉴 타이틀", example = "신상품") String title,
        @Schema(description = "이동 링크 URL", example = "/new-arrivals") String linkUrl) {}
