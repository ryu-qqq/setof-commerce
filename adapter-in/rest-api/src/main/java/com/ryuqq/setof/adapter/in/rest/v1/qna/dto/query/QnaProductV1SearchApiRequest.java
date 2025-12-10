package com.ryuqq.setof.adapter.in.rest.v1.qna.dto.query;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 브랜드 검색 필터 Request
 *
 * <p>브랜드 목록을 검색할 때 사용하는 필터 조건입니다. 검색어를 통해 브랜드명을 검색할 수 있습니다.
 *
 * @param offSet 오프셋
 * @param pageSize 페이지 사이즈
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "리뷰 검색 필터")
public record QnaProductV1SearchApiRequest(
    @Schema(description = "오프셋", example = "10") Long offSet,
    @Schema(description = "페이지 사이즈", example = "10") Integer pageSize
) {
}
