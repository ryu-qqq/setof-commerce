package com.ryuqq.setof.adapter.in.rest.v1.mypage.dto.query;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * V1 찜 목록 조회 필터 Query
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "찜 목록 조회 필터")
public record FavoriteFilterV1ApiRequest(
        @Schema(description = "마지막 조회한 도메인 ID (커서 기반 페이징)", example = "100") Long lastDomainId) {}
