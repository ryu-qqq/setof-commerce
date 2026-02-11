package com.ryuqq.setof.adapter.in.rest.v1.content.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * GnbV1ApiResponse - GNB (Global Navigation Bar) 응답 DTO.
 *
 * <p>레거시 GnbResponse 기반 변환.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Schema(description = "GNB 응답")
public record GnbV1ApiResponse(
        @Schema(description = "GNB ID", example = "1") long gnbId,
        @Schema(description = "제목", example = "홈") String title,
        @Schema(description = "링크 URL", example = "/") String linkUrl) {}
