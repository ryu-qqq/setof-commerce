package com.ryuqq.setof.adapter.in.rest.admin.v1.category.dto.query;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * CategorySearchV1ApiRequest - 카테고리 검색 V1 요청 DTO.
 *
 * <p>API-DTO-001: Request DTO는 record 타입 필수.
 *
 * <p>API-DTO-002: 기본값 처리는 Mapper에서 수행 (compact constructor 기본값 할당 금지).
 *
 * @param categoryName 카테고리명 (검색 조건)
 * @param depth 카테고리 깊이
 * @param displayed 노출 여부
 * @param page 페이지 번호 (0부터 시작)
 * @param size 페이지 크기
 * @author ryu-qqq
 * @since 1.0.0
 */
@Schema(description = "카테고리 검색 V1 요청")
public record CategorySearchV1ApiRequest(
        @Schema(description = "카테고리명 (검색 조건)", example = "의류") String categoryName,
        @Schema(description = "카테고리 깊이", example = "1") Integer depth,
        @Schema(description = "노출 여부", example = "true") Boolean displayed,
        @Schema(description = "페이지 번호", example = "0") Integer page,
        @Schema(description = "페이지 크기", example = "20") Integer size) {}
