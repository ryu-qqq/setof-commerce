package com.ryuqq.setof.adapter.in.rest.admin.v1.brand.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * ExtendedBrandV1ApiResponse - 확장 브랜드 V1 응답 DTO.
 *
 * <p>레거시 ExtendedBrandContext와 동일한 필드 구조입니다.
 *
 * @param brandId 브랜드 ID
 * @param brandName 브랜드명
 * @param mainDisplayType 메인 표시 타입 (ENGLISH, KOREAN)
 * @param displayEnglishName 영문 표시명
 * @param displayKoreanName 한글 표시명
 * @author ryu-qqq
 * @since 1.0.0
 */
@Schema(description = "확장 브랜드 V1 응답 (레거시 호환)")
public record ExtendedBrandV1ApiResponse(
        @Schema(description = "브랜드 ID", example = "1") long brandId,
        @Schema(description = "브랜드명", example = "NIKE") String brandName,
        @Schema(description = "메인 표시 타입", example = "ENGLISH") String mainDisplayType,
        @Schema(description = "영문 표시명", example = "NIKE") String displayEnglishName,
        @Schema(description = "한글 표시명", example = "나이키") String displayKoreanName) {}
