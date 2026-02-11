package com.ryuqq.setof.adapter.in.rest.v1.brand.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * BrandV1ApiResponse - 브랜드 응답 DTO.
 *
 * <p>레거시 BrandDisplayDto 기반 변환.
 *
 * <p>API-DTO-001: Record 타입 필수.
 *
 * <p>API-DTO-002: DTO 불변성 보장.
 *
 * <p>Response DTO는 Domain 객체 의존 금지 → Result만 의존해야 하며, 변환은 Mapper에서 수행합니다.
 *
 * @param brandId 브랜드 ID
 * @param brandName 브랜드명 (영문 코드)
 * @param korBrandName 브랜드명 (한글)
 * @param brandIconImageUrl 브랜드 아이콘 이미지 URL
 * @author ryu-qqq
 * @since 1.0.0
 * @see com.setof.connectly.module.brand.dto.BrandDisplayDto
 */
@Schema(description = "브랜드 응답")
public record BrandV1ApiResponse(
        @Schema(description = "브랜드 ID", example = "1") long brandId,
        @Schema(description = "브랜드명 (영문 코드)", example = "NIKE") String brandName,
        @Schema(description = "브랜드명 (한글)", example = "나이키") String korBrandName,
        @Schema(
                        description = "브랜드 아이콘 이미지 URL",
                        example = "https://cdn.example.com/brands/nike.png")
                String brandIconImageUrl) {}
