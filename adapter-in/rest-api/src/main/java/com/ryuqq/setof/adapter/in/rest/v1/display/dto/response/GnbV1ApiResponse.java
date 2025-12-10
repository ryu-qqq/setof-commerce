package com.ryuqq.setof.adapter.in.rest.v1.display.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Gnb Response
 *
 * <p>Gnb 정보를 반환하는 응답 DTO입니다.
 *
 * @param gnbId Gnb Id
 * @param title Gnb 제목
 * @param linkUrl Gnb 링크
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "Gnb 응답")
public record GnbV1ApiResponse(
        @Schema(description = "gnb Id", example = "1") long gnbId,
        @Schema(description = "gnb 타이틀", example = "상품 전시") String title,
        @Schema(description = "링크 url", example = "https://cdn.sample.com") String linkUrl) {}
