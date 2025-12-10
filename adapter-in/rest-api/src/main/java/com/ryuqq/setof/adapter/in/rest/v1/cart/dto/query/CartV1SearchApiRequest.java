package com.ryuqq.setof.adapter.in.rest.v1.cart.dto.query;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 장바구니 목록 조회 필터 Request
 *
 * <p>장바구니 목록을 조회할 때 사용하는 필터 조건입니다. 커서 기반 페이징을 지원합니다.
 *
 * @param lastDomainId 마지막 조회한 도메인 ID (커서 기반 페이징)
 * @param cursorValue 커서 값 (정렬 기준 값)
 * @param orderType 정렬 타입
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "장바구니 목록 조회 필터")
public record CartV1SearchApiRequest(
        @Schema(description = "마지막 조회한 도메인 ID (커서 기반 페이징)", example = "100") Long lastDomainId,
        @Schema(description = "커서 값 (정렬 기준 값)", example = "2024-01-01T00:00:00") String cursorValue,
        @Schema(description = "정렬 타입", example = "LATEST") String orderType,
        @Schema(description = "페이지 사이즈", example = "10") Integer pageSize) {}
