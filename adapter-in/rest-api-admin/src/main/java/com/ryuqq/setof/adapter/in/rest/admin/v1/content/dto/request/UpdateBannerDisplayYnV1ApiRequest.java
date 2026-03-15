package com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * UpdateBannerDisplayYnV1ApiRequest - 배너 그룹 노출 상태 변경 요청 DTO.
 *
 * <p>레거시 displayYn 형식을 그대로 유지합니다.
 *
 * <p>PATCH /api/v1/content/banner/{bannerId}/display-status — 노출 상태 변경
 *
 * @param displayYn 전시 여부 ("Y" or "N")
 * @author ryu-qqq
 * @since 1.1.0
 */
@Schema(description = "배너 그룹 노출 상태 변경 요청")
public record UpdateBannerDisplayYnV1ApiRequest(
        @Schema(
                        description = "전시 여부",
                        example = "N",
                        allowableValues = {"Y", "N"})
                String displayYn) {}
