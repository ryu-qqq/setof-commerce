package com.ryuqq.setof.adapter.in.rest.admin.v2.banner.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 배너 그룹 노출 상태 변경 API 요청.
 *
 * <p>API-DTO-001: API Request DTO는 Record로 정의.
 *
 * <p>API-DTO-003: Validation 어노테이션은 API Request에만 적용.
 *
 * <p>API-DTO-004: Update Request에 ID 미포함. ID는 PathVariable로 전달.
 *
 * @param active 변경할 노출 여부
 * @author ryu-qqq
 * @since 1.1.0
 */
@Schema(description = "배너 그룹 노출 상태 변경 요청")
public record ChangeBannerGroupStatusApiRequest(
        @Schema(description = "노출 여부", example = "true") boolean active) {}
