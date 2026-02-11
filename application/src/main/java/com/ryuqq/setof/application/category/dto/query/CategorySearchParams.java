package com.ryuqq.setof.application.category.dto.query;

import com.ryuqq.setof.application.common.dto.query.CommonSearchParams;

/**
 * 카테고리 검색 파라미터.
 *
 * <p>APP-DTO-003: SearchParams CommonSearchParams 포함 필수.
 *
 * @param searchField 검색 필드 (null이면 전체 필드 검색)
 * @param searchWord 검색어 (null이면 전체)
 * @param depth 카테고리 깊이
 * @param displayed 노출 여부
 * @param searchParams 공통 검색 파라미터 (정렬, 페이징 등)
 * @author ryu-qqq
 * @since 1.0.0
 */
public record CategorySearchParams(
        String searchField,
        String searchWord,
        Integer depth,
        Boolean displayed,
        CommonSearchParams searchParams) {

    public static CategorySearchParams of(
            String searchField,
            String searchWord,
            Integer depth,
            Boolean displayed,
            CommonSearchParams searchParams) {
        return new CategorySearchParams(searchField, searchWord, depth, displayed, searchParams);
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
