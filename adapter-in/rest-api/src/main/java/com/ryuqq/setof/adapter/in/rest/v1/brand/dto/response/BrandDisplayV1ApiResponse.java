package com.ryuqq.setof.adapter.in.rest.v1.brand.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * BrandDisplayV1ApiResponse - 브랜드 표시용 V1 응답 DTO.
 *
 * <p>레거시 BrandDisplayDto와 동일한 필드 구조입니다.
 *
 * @param brandId 브랜드 ID
 * @param brandName 브랜드명
 * @param korBrandName 한글 브랜드명
 * @param brandIconImageUrl 브랜드 아이콘 이미지 URL
 * @author ryu-qqq
 * @since 1.0.0
 */
@Schema(description = "브랜드 표시용 V1 응답 (레거시 호환)")
public record BrandDisplayV1ApiResponse(
        @Schema(description = "브랜드 ID", example = "1") long brandId,
        @Schema(description = "브랜드명", example = "NIKE") String brandName,
        @Schema(description = "한글 브랜드명", example = "나이키") String korBrandName,
        @Schema(description = "브랜드 아이콘 이미지 URL", example = "https://example.com/nike.png")
                String brandIconImageUrl) {}
