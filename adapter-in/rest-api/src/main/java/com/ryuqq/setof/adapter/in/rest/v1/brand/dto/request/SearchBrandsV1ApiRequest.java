package com.ryuqq.setof.adapter.in.rest.v1.brand.dto.request;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * SearchBrandsV1ApiRequest - 브랜드 목록 검색 요청 DTO.
 *
 * <p>레거시 BrandFilter 기반 변환.
 *
 * <p>API-DTO-001: API Request DTO는 Record로 정의.
 *
 * <p>API-DTO-010: Request DTO 조회 네이밍 규칙 (Search*ApiRequest).
 *
 * <p>기본값 처리는 Mapper에서 수행합니다. Request DTO에서는 기본값 설정 금지.
 *
 * @param searchWord 브랜드명 검색어 (한글/영문, 부분 일치)
 * @author ryu-qqq
 * @since 1.0.0
 * @see com.setof.connectly.module.brand.dto.BrandFilter
 */
@Schema(description = "브랜드 검색 요청")
public record SearchBrandsV1ApiRequest(
        @Parameter(description = "브랜드명 검색어 (한글/영문, 부분 일치)", example = "나이키")
                @Schema(description = "브랜드명 검색어", nullable = true)
                String searchWord) {}
