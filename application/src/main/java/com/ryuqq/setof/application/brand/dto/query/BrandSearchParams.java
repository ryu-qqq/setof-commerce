package com.ryuqq.setof.application.brand.dto.query;

import com.ryuqq.setof.application.common.dto.query.CommonSearchParams;

/**
 * 브랜드 검색 파라미터.
 *
 * <p>APP-DTO-003: SearchParams CommonSearchParams 포함 필수.
 *
 * @param brandName 브랜드명 (검색 조건)
 * @param searchParams 공통 검색 파라미터 (정렬, 페이징 등)
 * @author ryu-qqq
 * @since 1.0.0
 */
public record BrandSearchParams(String brandName, CommonSearchParams searchParams) {

    public static BrandSearchParams of(String brandName, CommonSearchParams searchParams) {
        return new BrandSearchParams(brandName, searchParams);
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
