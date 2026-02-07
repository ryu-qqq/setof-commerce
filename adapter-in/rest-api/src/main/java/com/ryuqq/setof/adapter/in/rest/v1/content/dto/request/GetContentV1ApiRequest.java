package com.ryuqq.setof.adapter.in.rest.v1.content.dto.request;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * GetContentV1ApiRequest - 콘텐츠 상세 조회 요청 DTO.
 *
 * <p>레거시 bypass 파라미터 기반 변환.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Schema(description = "콘텐츠 상세 조회 요청")
public record GetContentV1ApiRequest(
        @Parameter(
                        description = "전시 기간 체크 우회 여부 (Y: 우회, N: 체크)",
                        example = "N",
                        schema = @Schema(allowableValues = {"Y", "N"}))
                String bypass) {}
