package com.ryuqq.setof.adapter.in.rest.admin.v1.brand.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * V1 확장 브랜드 컨텍스트 Response
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "확장 브랜드 컨텍스트 응답")
public record ExtendedBrandContextV1ApiResponse(
        @Schema(description = "브랜드 ID", example = "1") Long brandId,
        @Schema(description = "브랜드명", example = "Nike") String brandName,
        @Schema(description = "메인 표시명 타입", example = "KOREAN") String mainDisplayType,
        @Schema(description = "영문 표시명", example = "Nike") String displayEnglishName,
        @Schema(description = "한글 표시명", example = "나이키") String displayKoreanName) {
}
