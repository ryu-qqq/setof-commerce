package com.ryuqq.setof.application.brand.dto.query;

/**
 * 브랜드 노출 조회용 검색 파라미터.
 *
 * <p>Public API용 간단한 검색 조건입니다.
 *
 * <p>컨벤션: searchField + searchWord 패턴. 확장 시 코드 변경 최소화.
 *
 * @param searchField 검색 필드 (예: brandName, korBrandName). null이면 전체.
 * @param searchWord 검색어
 * @param displayed 노출 여부
 * @author ryu-qqq
 * @since 1.0.0
 */
public record BrandDisplaySearchParams(String searchField, String searchWord, Boolean displayed) {

    public static BrandDisplaySearchParams of(
            String searchField, String searchWord, Boolean displayed) {
        return new BrandDisplaySearchParams(searchField, searchWord, displayed);
    }
}
