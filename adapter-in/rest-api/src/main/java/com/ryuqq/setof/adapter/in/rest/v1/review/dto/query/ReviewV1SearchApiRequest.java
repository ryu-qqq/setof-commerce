package com.ryuqq.setof.adapter.in.rest.v1.review.dto.query;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 브랜드 검색 필터 Request
 *
 * <p>브랜드 목록을 검색할 때 사용하는 필터 조건입니다. 검색어를 통해 브랜드명을 검색할 수 있습니다.
 *
 * @param orderType 정렬 순 (리뷰 카운트)
 * @param productGroupId 상품 그룹 PK 아이디
 * @param lastDomainId 리뷰 커서용 PK 아이디
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "리뷰 검색 필터")
public record ReviewV1SearchApiRequest(
    @Schema(description = "정렬 순", example = "averageRating") String orderType,
    @Schema(description = "상품 그룹 PK 아이디", example = "1") Long productGroupId,
    @Schema(description = "리뷰 커서용 PK 아이디", example = "1") Long lastDomainId,
    @Schema(description = "리뷰 커서용 페이지 크기", example = "10") Integer pageSize
) {
}
