package com.ryuqq.setof.adapter.in.rest.v2.brand.dto.response;

import com.ryuqq.setof.application.brand.dto.response.BrandSummaryResponse;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Brand Summary V2 API Response
 *
 * <p>브랜드 요약 정보 응답 DTO입니다. (목록 조회용)
 *
 * @param brandId 브랜드 ID
 * @param code 브랜드 코드
 * @param nameKo 한글 브랜드명
 * @param logoUrl 로고 이미지 URL
 */
@Schema(description = "Brand Summary V2 응답 (목록용)")
public record BrandSummaryV2ApiResponse(
        @Schema(description = "브랜드 ID", example = "1") Long brandId,
        @Schema(description = "브랜드 코드", example = "NIKE001") String code,
        @Schema(description = "한글 브랜드명", example = "나이키") String nameKo,
        @Schema(description = "로고 이미지 URL", example = "https://cdn.example.com/brands/nike.png")
                String logoUrl) {

    /**
     * Application Layer DTO로부터 생성
     *
     * @param response BrandSummaryResponse
     * @return BrandSummaryV2ApiResponse
     */
    public static BrandSummaryV2ApiResponse from(BrandSummaryResponse response) {
        return new BrandSummaryV2ApiResponse(
                response.id(), response.code(), response.nameKo(), response.logoUrl());
    }
}
