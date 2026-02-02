package com.ryuqq.setof.adapter.in.rest.v1.brand.dto.query;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * BrandSearchV1ApiRequest - 브랜드 검색 V1 요청 DTO.
 *
 * <p>API-DTO-001: Request DTO는 record 타입 필수.
 *
 * <p>API-DTO-002: 기본값 처리는 Mapper에서 수행 (compact constructor 기본값 할당 금지).
 *
 * @param brandName 브랜드명 (검색 조건)
 * @param displayed 노출 여부
 * @author ryu-qqq
 * @since 1.0.0
 */
@Schema(description = "브랜드 검색 V1 요청")
public record BrandSearchV1ApiRequest(
        @Schema(description = "브랜드명 (검색 조건)", example = "나이키") String brandName,
        @Schema(description = "노출 여부", example = "true") Boolean displayed) {}
