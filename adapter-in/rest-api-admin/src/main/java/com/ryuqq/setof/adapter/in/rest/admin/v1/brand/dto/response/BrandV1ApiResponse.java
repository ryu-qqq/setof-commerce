package com.ryuqq.setof.adapter.in.rest.admin.v1.brand.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * BrandV1ApiResponse - 브랜드 V1 응답 DTO.
 *
 * @param brandId 브랜드 ID
 * @param brandName 브랜드명
 * @param brandNameKo 브랜드 한글명
 * @param brandIconImageUrl 브랜드 아이콘 이미지 URL
 * @param displayed 노출 여부
 * @author ryu-qqq
 * @since 1.0.0
 */
@Schema(description = "브랜드 V1 응답")
public record BrandV1ApiResponse(
        @Schema(description = "브랜드 ID", example = "1") Long brandId,
        @Schema(description = "브랜드명", example = "NIKE") String brandName,
        @Schema(description = "브랜드 한글명", example = "나이키") String brandNameKo,
        @Schema(description = "브랜드 아이콘 이미지 URL", example = "https://example.com/nike.png")
                String brandIconImageUrl,
        @Schema(description = "노출 여부", example = "true") boolean displayed) {}
