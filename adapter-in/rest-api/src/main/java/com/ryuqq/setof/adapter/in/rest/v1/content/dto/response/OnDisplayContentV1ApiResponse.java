package com.ryuqq.setof.adapter.in.rest.v1.content.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Set;

/**
 * OnDisplayContentV1ApiResponse - 전시 중인 콘텐츠 ID 목록 응답 DTO.
 *
 * <p>레거시 OnDisplayContent 기반 변환.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Schema(description = "전시 중인 콘텐츠 ID 목록 응답")
public record OnDisplayContentV1ApiResponse(
        @Schema(description = "전시 중인 콘텐츠 ID 목록", example = "[1, 2, 3, 5, 10]")
                Set<Long> contentIds) {}
