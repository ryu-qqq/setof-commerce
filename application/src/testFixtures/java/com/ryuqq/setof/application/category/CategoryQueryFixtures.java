package com.ryuqq.setof.application.category;

import com.ryuqq.setof.application.category.dto.query.CategorySearchParams;
import com.ryuqq.setof.application.common.dto.query.CommonSearchParams;

/**
 * Category Query 테스트 Fixtures.
 *
 * <p>Category 관련 Query 객체들을 생성하는 테스트 유틸리티입니다.
 */
public final class CategoryQueryFixtures {

    private CategoryQueryFixtures() {}

    // ===== CategorySearchParams =====

    public static CategorySearchParams searchParams() {
        return CategorySearchParams.of(null, null, null, null, defaultCommonSearchParams());
    }

    public static CategorySearchParams searchParams(String searchWord) {
        return CategorySearchParams.of(null, searchWord, null, null, defaultCommonSearchParams());
    }

    public static CategorySearchParams searchParams(
            String searchField, String searchWord, Integer depth, Boolean displayed) {
        return CategorySearchParams.of(
                searchField, searchWord, depth, displayed, defaultCommonSearchParams());
    }

    public static CategorySearchParams searchParams(
            String searchField,
            String searchWord,
            Integer depth,
            Boolean displayed,
            int page,
            int size) {
        return CategorySearchParams.of(
                searchField, searchWord, depth, displayed, commonSearchParams(page, size));
    }

    // ===== CommonSearchParams =====

    public static CommonSearchParams defaultCommonSearchParams() {
        return CommonSearchParams.of(false, null, null, "createdAt", "DESC", 0, 20);
    }

    public static CommonSearchParams commonSearchParams(int page, int size) {
        return CommonSearchParams.of(false, null, null, "createdAt", "DESC", page, size);
    }
}
