package com.ryuqq.setof.adapter.in.rest.v1.board.dto.query;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Board 검색 필터 Request
 *
 * <p>Board 목록을 검색할 때 사용하는 필터 조건입니다. 검색어를 통해 브랜드명을 검색할 수 있습니다.
 *
 * @param offSet 페이지 오프셋
 * @param pageSize 페이지 사이즈
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "Board 검색 필터")
public record BoardV1SearchApiRequest(
        @Schema(description = "페이지 오프셋", example = "1") String offSet,
        @Schema(description = "페이지 사이즈 ", example = "1") String pageSize) {}
