package com.ryuqq.setof.domain.brand.query;

import com.ryuqq.setof.domain.common.vo.QueryContext;

/**
 * Brand 검색 조건 Criteria.
 *
 * <p>브랜드 목록 조회 시 사용하는 검색 조건과 페이징 정보를 정의합니다.
 *
 * <p>Seller 패턴: searchField + searchWord.
 *
 * @param displayed 표시 여부 필터 (null이면 전체)
 * @param searchField 검색 필드 (null이면 전체 필드 검색)
 * @param searchWord 검색어 (null이면 전체)
 * @param queryContext 정렬 및 페이징 정보
 * @author ryu-qqq
 * @since 1.0.0
 */
public record BrandSearchCriteria(
        Boolean displayed,
        BrandSearchField searchField,
        String searchWord,
        QueryContext<BrandSortKey> queryContext) {

    /** Compact Constructor - null 방어 */
    public BrandSearchCriteria {
        if (queryContext == null) {
            queryContext = QueryContext.defaultOf(BrandSortKey.defaultKey());
        }
        if (searchWord != null) {
            searchWord = searchWord.trim();
            if (searchWord.isBlank()) {
                searchWord = null;
            }
        }
    }

    /**
     * 검색 조건 생성.
     *
     * @param displayed 표시 여부 필터 (null이면 전체)
     * @param searchField 검색 필드 (null이면 전체 필드 검색)
     * @param searchWord 검색어 (null이면 전체)
     * @param queryContext 정렬 및 페이징 정보
     * @return BrandSearchCriteria
     */
    public static BrandSearchCriteria of(
            Boolean displayed,
            BrandSearchField searchField,
            String searchWord,
            QueryContext<BrandSortKey> queryContext) {
        return new BrandSearchCriteria(displayed, searchField, searchWord, queryContext);
    }

    /**
     * 기본 검색 조건 생성 (전체 조회, 표시순서 순).
     *
     * @return BrandSearchCriteria
     */
    public static BrandSearchCriteria defaultOf() {
        return new BrandSearchCriteria(
                null, null, null, QueryContext.defaultOf(BrandSortKey.defaultKey()));
    }

    /**
     * 표시 중인 브랜드만 조회하는 조건 생성.
     *
     * @return BrandSearchCriteria
     */
    public static BrandSearchCriteria displayedOnly() {
        return new BrandSearchCriteria(
                true, null, null, QueryContext.defaultOf(BrandSortKey.defaultKey()));
    }

    /** 검색 조건이 있는지 확인. */
    public boolean hasSearchCondition() {
        return searchWord != null && !searchWord.isBlank();
    }

    /** 특정 필드 검색인지 확인. */
    public boolean hasSearchField() {
        return searchField != null;
    }

    /** 표시 여부 필터가 있는지 확인. */
    public boolean hasDisplayedFilter() {
        return displayed != null;
    }

    /** 페이지 크기 반환 (편의 메서드). */
    public int size() {
        return queryContext.size();
    }

    /** 오프셋 반환 (편의 메서드). */
    public long offset() {
        return queryContext.offset();
    }

    /** 현재 페이지 번호 반환 (편의 메서드). */
    public int page() {
        return queryContext.page();
    }
}
