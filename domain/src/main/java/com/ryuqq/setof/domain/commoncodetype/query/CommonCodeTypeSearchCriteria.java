package com.ryuqq.setof.domain.commoncodetype.query;

import com.ryuqq.setof.domain.common.vo.QueryContext;

/**
 * CommonCodeType 검색 조건 Criteria.
 *
 * <p>공통 코드 타입 목록 조회 시 사용하는 검색 조건과 페이징 정보를 정의합니다.
 *
 * <p><strong>사용 예시:</strong>
 *
 * <pre>{@code
 * // 활성화된 코드 타입만 조회 (등록순 정렬, 20개)
 * CommonCodeTypeSearchCriteria criteria = CommonCodeTypeSearchCriteria.of(
 *     true,
 *     null,
 *     QueryContext.defaultOf(CommonCodeTypeSortKey.CREATED_AT)
 * );
 *
 * // 키워드 검색
 * CommonCodeTypeSearchCriteria criteria = CommonCodeTypeSearchCriteria.of(
 *     null,
 *     "결제",
 *     QueryContext.firstPage(CommonCodeTypeSortKey.CREATED_AT, SortDirection.DESC, 10)
 * );
 * }</pre>
 *
 * @param active 활성화 상태 필터 (null이면 전체)
 * @param keyword 검색 키워드 (코드, 이름에서 검색, null이면 전체)
 * @param queryContext 정렬 및 페이징 정보
 */
public record CommonCodeTypeSearchCriteria(
        Boolean active, String keyword, QueryContext<CommonCodeTypeSortKey> queryContext) {

    /**
     * 검색 조건 생성
     *
     * @param active 활성화 상태 필터 (null이면 전체)
     * @param keyword 검색 키워드 (null이면 전체)
     * @param queryContext 정렬 및 페이징 정보
     * @return CommonCodeTypeSearchCriteria
     */
    public static CommonCodeTypeSearchCriteria of(
            Boolean active, String keyword, QueryContext<CommonCodeTypeSortKey> queryContext) {
        return new CommonCodeTypeSearchCriteria(active, keyword, queryContext);
    }

    /**
     * 기본 검색 조건 생성 (전체 조회, 등록순 내림차순)
     *
     * @return CommonCodeTypeSearchCriteria
     */
    public static CommonCodeTypeSearchCriteria defaultCriteria() {
        return new CommonCodeTypeSearchCriteria(
                null, null, QueryContext.defaultOf(CommonCodeTypeSortKey.defaultKey()));
    }

    /**
     * 활성화된 항목만 조회하는 조건 생성
     *
     * @return CommonCodeTypeSearchCriteria
     */
    public static CommonCodeTypeSearchCriteria activeOnly() {
        return new CommonCodeTypeSearchCriteria(
                true, null, QueryContext.defaultOf(CommonCodeTypeSortKey.defaultKey()));
    }

    /** 키워드 검색 조건이 있는지 확인 */
    public boolean hasKeyword() {
        return keyword != null && !keyword.isBlank();
    }

    /** 활성화 상태 필터가 있는지 확인 */
    public boolean hasActiveFilter() {
        return active != null;
    }

    /** 페이지 크기 반환 (편의 메서드) */
    public int size() {
        return queryContext.size();
    }

    /** 오프셋 반환 (편의 메서드) */
    public long offset() {
        return queryContext.offset();
    }

    /** 현재 페이지 번호 반환 (편의 메서드) */
    public int page() {
        return queryContext.page();
    }
}
