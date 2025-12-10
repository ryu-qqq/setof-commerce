package com.ryuqq.setof.adapter.in.rest.v1.mileage.dto.query;

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
@Schema(description = "마일리지 목록 조회 필터")
public record MileageV1SearchApiRequest(
        @Schema(description = "마지막 조회한 도메인 ID (커서 기반 페이징)", example = "10") Long lastDomainId,
        @Schema(description = "마일리지 사유 목록 (복수 선택 가능)", example = "[\"PURCHASE\", \"REVIEW\"]")
                Long reasons,
        @Schema(description = "오프셋", example = "10") Long offSet,
        @Schema(description = "페이지 사이즈", example = "10") Integer pageSize) {}
