package com.ryuqq.setof.adapter.in.rest.v1.brand.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 브랜드 Response
 *
 * <p>브랜드 정보를 반환하는 응답 DTO입니다.
 *
 * @param brandId 브랜드 ID
 * @param brandName 브랜드명
 * @param korBrandName 브랜드명 한글명
 * @param brandIconImageUrl 브랜드 로고 이미지 URL
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "브랜드 응답")
public record BrandV1ApiResponse(
        @Schema(description = "브랜드 ID", example = "100") Long brandId,
        @Schema(description = "브랜드명", example = "브랜드A") String brandName,
        @Schema(description = "브랜드명 한글명", example = "브랜드A") String korBrandName,
        @Schema(description = "로고 이미지 URL", example = "https://cdn.example.com/logo/brandA.png")
                String brandIconImageUrl) {}
