package com.ryuqq.setof.application.faqcategory.dto.query;

/**
 * FAQ 카테고리 검색 Query
 *
 * @author development-team
 * @since 1.0.0
 */
public record SearchFaqCategoryQuery(String status, String keyword, int page, int size) {

    public SearchFaqCategoryQuery {
        if (page < 0) {
            throw new IllegalArgumentException("페이지는 0 이상이어야 합니다");
        }
        if (size < 1 || size > 100) {
            throw new IllegalArgumentException("사이즈는 1~100 사이여야 합니다");
        }
    }

    /** Admin 검색용 Query 생성 */
    public static SearchFaqCategoryQuery forAdmin(
            String status, String keyword, int page, int size) {
        return new SearchFaqCategoryQuery(status, keyword, page, size);
    }

    /** Client 검색용 Query 생성 (ACTIVE만) */
    public static SearchFaqCategoryQuery forClient(int page, int size) {
        return new SearchFaqCategoryQuery("ACTIVE", null, page, size);
    }
}
