package com.ryuqq.setof.application.faq.dto.query;

/**
 * FAQ 검색 Query
 *
 * @author development-team
 * @since 1.0.0
 */
public record SearchFaqQuery(
        String categoryCode, String status, Boolean isTop, String keyword, int page, int size) {

    public SearchFaqQuery {
        if (page < 0) {
            throw new IllegalArgumentException("페이지는 0 이상이어야 합니다");
        }
        if (size < 1 || size > 100) {
            throw new IllegalArgumentException("사이즈는 1~100 사이여야 합니다");
        }
    }

    /** Admin 검색용 Query 생성 */
    public static SearchFaqQuery forAdmin(
            String categoryCode, String status, Boolean isTop, String keyword, int page, int size) {
        return new SearchFaqQuery(categoryCode, status, isTop, keyword, page, size);
    }

    /** Client 검색용 Query 생성 (PUBLISHED만) */
    public static SearchFaqQuery forClient(
            String categoryCode, Boolean isTop, String keyword, int page, int size) {
        return new SearchFaqQuery(categoryCode, "PUBLISHED", isTop, keyword, page, size);
    }
}
