package com.ryuqq.setof.adapter.in.rest.v2.brand.dto.response;

import com.ryuqq.setof.application.brand.dto.response.BrandResponse;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Brand V2 API Response
 *
 * <p>브랜드 상세 정보 응답 DTO입니다.
 *
 * @param brandId 브랜드 ID
 * @param code 브랜드 코드
 * @param nameKo 한글 브랜드명
 * @param nameEn 영문 브랜드명
 * @param logoUrl 로고 이미지 URL
 * @param status 상태
 */
@Schema(description = "Brand V2 응답")
public record BrandV2ApiResponse(
        @Schema(description = "브랜드 ID", example = "1") Long brandId,
        @Schema(description = "브랜드 코드", example = "NIKE001") String code,
        @Schema(description = "한글 브랜드명", example = "나이키") String nameKo,
        @Schema(description = "영문 브랜드명", example = "Nike") String nameEn,
        @Schema(description = "로고 이미지 URL", example = "https://cdn.example.com/brands/nike.png")
                String logoUrl,
        @Schema(description = "상태", example = "ACTIVE") String status) {

    /**
     * Application Layer DTO로부터 생성
     *
     * @param response BrandResponse
     * @return BrandV2ApiResponse
     */
    public static BrandV2ApiResponse from(BrandResponse response) {
        return new BrandV2ApiResponse(
                response.id(),
                response.code(),
                response.nameKo(),
                response.nameEn(),
                response.logoUrl(),
                response.status());
    }
}
