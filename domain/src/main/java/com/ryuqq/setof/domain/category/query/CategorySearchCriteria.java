package com.ryuqq.setof.domain.category.query;

import com.ryuqq.setof.domain.category.id.CategoryId;
import com.ryuqq.setof.domain.category.vo.CategoryType;
import com.ryuqq.setof.domain.category.vo.TargetGroup;
import com.ryuqq.setof.domain.common.vo.QueryContext;

/**
 * Category 검색 조건 Criteria.
 *
 * <p>카테고리 목록 조회 시 사용하는 검색 조건과 페이징 정보를 정의합니다.
 *
 * @param parentCategoryId 부모 카테고리 ID 필터 (null이면 전체)
 * @param displayed 표시 여부 필터 (null이면 전체)
 * @param targetGroup 타겟 그룹 필터 (null이면 전체)
 * @param categoryType 카테고리 타입 필터 (null이면 전체)
 * @param searchField 검색 필드 (null이면 전체 필드 검색)
 * @param searchWord 검색어 (null이면 전체)
 * @param queryContext 정렬 및 페이징 정보
 * @author ryu-qqq
 * @since 1.0.0
 */
public record CategorySearchCriteria(
        CategoryId parentCategoryId,
        Boolean displayed,
        TargetGroup targetGroup,
        CategoryType categoryType,
        CategorySearchField searchField,
        String searchWord,
        QueryContext<CategorySortKey> queryContext) {

    /** Compact Constructor - null 방어 */
    public CategorySearchCriteria {
        if (queryContext == null) {
            queryContext = QueryContext.defaultOf(CategorySortKey.defaultKey());
        }
        if (searchWord != null) {
            searchWord = searchWord.trim();
            if (searchWord.isBlank()) {
                searchWord = null;
            }
        }
    }

    /**
     * 검색 조건 생성
     *
     * @param parentCategoryId 부모 카테고리 ID 필터 (null이면 전체)
     * @param displayed 표시 여부 필터 (null이면 전체)
     * @param targetGroup 타겟 그룹 필터 (null이면 전체)
     * @param categoryType 카테고리 타입 필터 (null이면 전체)
     * @param searchField 검색 필드 (null이면 전체 필드 검색)
     * @param searchWord 검색어 (null이면 전체)
     * @param queryContext 정렬 및 페이징 정보
     * @return CategorySearchCriteria
     */
    public static CategorySearchCriteria of(
            CategoryId parentCategoryId,
            Boolean displayed,
            TargetGroup targetGroup,
            CategoryType categoryType,
            CategorySearchField searchField,
            String searchWord,
            QueryContext<CategorySortKey> queryContext) {
        return new CategorySearchCriteria(
                parentCategoryId,
                displayed,
                targetGroup,
                categoryType,
                searchField,
                searchWord,
                queryContext);
    }

    /**
     * 기본 검색 조건 생성 (전체 조회, 등록일시 순)
     *
     * @return CategorySearchCriteria
     */
    public static CategorySearchCriteria defaultOf() {
        return new CategorySearchCriteria(
                null,
                null,
                null,
                null,
                null,
                null,
                QueryContext.defaultOf(CategorySortKey.defaultKey()));
    }

    /**
     * 부모 카테고리별 하위 카테고리 조회 조건 생성
     *
     * @param parentCategoryId 부모 카테고리 ID (필수)
     * @return CategorySearchCriteria
     */
    public static CategorySearchCriteria byParent(CategoryId parentCategoryId) {
        return new CategorySearchCriteria(
                parentCategoryId,
                null,
                null,
                null,
                null,
                null,
                QueryContext.defaultOf(CategorySortKey.defaultKey()));
    }

    /**
     * 루트 카테고리만 조회하는 조건 생성
     *
     * @return CategorySearchCriteria
     */
    public static CategorySearchCriteria rootOnly() {
        return new CategorySearchCriteria(
                CategoryId.of(0L),
                null,
                null,
                null,
                null,
                null,
                QueryContext.defaultOf(CategorySortKey.defaultKey()));
    }

    /** 부모 카테고리 ID 원시값 반환 (null이면 null) */
    public Long parentCategoryIdValue() {
        return parentCategoryId != null ? parentCategoryId.value() : null;
    }

    /** 부모 카테고리 필터가 있는지 확인 */
    public boolean hasParentFilter() {
        return parentCategoryId != null;
    }

    /** 표시 여부 필터가 있는지 확인 */
    public boolean hasDisplayedFilter() {
        return displayed != null;
    }

    /** 검색 조건이 있는지 확인 */
    public boolean hasSearchCondition() {
        return searchWord != null && !searchWord.isBlank();
    }

    /** 특정 필드 검색인지 확인 */
    public boolean hasSearchField() {
        return searchField != null;
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
