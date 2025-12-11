package com.ryuqq.setof.application.brand.dto.response;

/**
 * 브랜드 정보 응답 DTO
 *
 * <p>브랜드 상세 조회 시 반환되는 응답 DTO입니다.
 *
 * @param id 브랜드 ID
 * @param code 브랜드 코드
 * @param nameKo 한글 브랜드명
 * @param nameEn 영문 브랜드명 (nullable)
 * @param logoUrl 로고 URL (nullable)
 * @param status 브랜드 상태
 */
public record BrandResponse(
        Long id, String code, String nameKo, String nameEn, String logoUrl, String status) {

    /**
     * Static Factory Method
     *
     * @param id 브랜드 ID
     * @param code 브랜드 코드
     * @param nameKo 한글 브랜드명
     * @param nameEn 영문 브랜드명
     * @param logoUrl 로고 URL
     * @param status 브랜드 상태
     * @return BrandResponse 인스턴스
     */
    public static BrandResponse of(
            Long id, String code, String nameKo, String nameEn, String logoUrl, String status) {
        return new BrandResponse(id, code, nameKo, nameEn, logoUrl, status);
    }
}
