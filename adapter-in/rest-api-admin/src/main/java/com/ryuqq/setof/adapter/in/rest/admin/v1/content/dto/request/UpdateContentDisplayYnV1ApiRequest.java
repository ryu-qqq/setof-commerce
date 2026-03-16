package com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * UpdateContentDisplayYnV1ApiRequest - 콘텐츠 노출 상태 변경 v1 요청 DTO.
 *
 * <p>레거시 UpdateContentDisplayYn 형식을 그대로 유지합니다.
 *
 * @param displayYn 전시 여부 ("Y" or "N")
 * @author ryu-qqq
 * @since 1.1.0
 */
@Schema(description = "콘텐츠 노출 상태 변경 요청")
public record UpdateContentDisplayYnV1ApiRequest(
        @Schema(
                        description = "전시 여부",
                        example = "N",
                        allowableValues = {"Y", "N"})
                String displayYn) {}
