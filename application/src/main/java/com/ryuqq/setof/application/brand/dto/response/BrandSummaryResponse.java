package com.ryuqq.setof.application.brand.dto.response;

/**
 * 브랜드 요약 정보 응답 DTO
 *
 * <p>브랜드 목록 조회 시 반환되는 간략한 응답 DTO입니다.
 *
 * @param id 브랜드 ID
 * @param code 브랜드 코드
 * @param nameKo 한글 브랜드명
 * @param logoUrl 로고 URL (nullable)
 */
public record BrandSummaryResponse(Long id, String code, String nameKo, String logoUrl) {

    /**
     * Static Factory Method
     *
     * @param id 브랜드 ID
     * @param code 브랜드 코드
     * @param nameKo 한글 브랜드명
     * @param logoUrl 로고 URL
     * @return BrandSummaryResponse 인스턴스
     */
    public static BrandSummaryResponse of(Long id, String code, String nameKo, String logoUrl) {
        return new BrandSummaryResponse(id, code, nameKo, logoUrl);
    }
}
