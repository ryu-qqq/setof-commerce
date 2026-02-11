package com.ryuqq.setof.application.brand.dto.query;

import com.ryuqq.setof.application.common.dto.query.CommonSearchParams;

/**
 * 브랜드 검색 파라미터.
 *
 * <p>APP-DTO-003: SearchParams CommonSearchParams 포함 필수.
 *
 * <p>컨벤션: searchField + searchWord 패턴. 확장 시 코드 변경 최소화.
 *
 * @param searchField 검색 필드 (예: brandName, korBrandName). null이면 전체.
 * @param searchWord 검색어
 * @param searchParams 공통 검색 파라미터 (정렬, 페이징 등)
 * @author ryu-qqq
 * @since 1.0.0
 */
public record BrandSearchParams(
        String searchField, String searchWord, CommonSearchParams searchParams) {

    public static BrandSearchParams of(
            String searchField, String searchWord, CommonSearchParams searchParams) {
        return new BrandSearchParams(searchField, searchWord, searchParams);
    }

    public int page() {
        return searchParams.page();
    }

    public int size() {
        return searchParams.size();
    }

    public String sortKey() {
        return searchParams.sortKey();
    }

    public String sortDirection() {
        return searchParams.sortDirection();
    }
}
