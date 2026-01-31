package com.ryuqq.setof.application.commoncodetype.dto.query;

import com.ryuqq.setof.application.common.dto.query.CommonSearchParams;

/**
 * 공통 코드 타입 검색 파라미터.
 *
 * <p>APP-DTO-003: SearchParams CommonSearchParams 포함 필수
 *
 * <p>APP-DTO-002: Command/Query 인스턴스 메서드 금지
 *
 * @param active 활성화 여부 필터
 * @param keyword 검색 키워드 (코드, 이름)
 * @param searchParams 공통 검색 파라미터 (정렬, 페이징 등)
 */
public record CommonCodeTypeSearchParams(
        Boolean active, String keyword, CommonSearchParams searchParams) {

    public static CommonCodeTypeSearchParams of(
            Boolean active, String keyword, CommonSearchParams searchParams) {
        return new CommonCodeTypeSearchParams(active, keyword, searchParams);
    }

    // Delegate methods for convenience
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
