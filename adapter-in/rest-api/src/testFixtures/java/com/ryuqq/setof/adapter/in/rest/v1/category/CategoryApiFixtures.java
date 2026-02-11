package com.ryuqq.setof.adapter.in.rest.v1.category;

import com.ryuqq.setof.adapter.in.rest.v1.category.dto.response.TreeCategoryV1ApiResponse;
import com.ryuqq.setof.application.category.dto.response.CategoryDisplayResult;
import java.util.List;

/**
 * Category V1 API 테스트 Fixtures.
 *
 * <p>Category 관련 API Response 및 Application Result 객체를 생성하는 테스트 유틸리티입니다.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
public final class CategoryApiFixtures {

    private CategoryApiFixtures() {}

    // ===== TreeCategoryV1ApiResponse =====

    /**
     * 단일 카테고리 응답 생성 (자식 없음).
     *
     * @param categoryId 카테고리 ID
     * @return TreeCategoryV1ApiResponse
     */
    public static TreeCategoryV1ApiResponse categoryResponse(long categoryId) {
        return new TreeCategoryV1ApiResponse(categoryId, "여성", 1, 0L);
    }

    /**
     * 카테고리 응답 생성.
     *
     * @param categoryId 카테고리 ID
     * @param categoryName 카테고리명
     * @param depth 카테고리 깊이
     * @param parentCategoryId 부모 카테고리 ID
     * @return TreeCategoryV1ApiResponse
     */
    public static TreeCategoryV1ApiResponse categoryResponse(
            long categoryId, String categoryName, int depth, long parentCategoryId) {
        return new TreeCategoryV1ApiResponse(categoryId, categoryName, depth, parentCategoryId);
    }

    /**
     * 자식 포함 카테고리 응답 생성.
     *
     * @param categoryId 카테고리 ID
     * @param categoryName 카테고리명
     * @param depth 카테고리 깊이
     * @param parentCategoryId 부모 카테고리 ID
     * @param children 자식 카테고리 목록
     * @return TreeCategoryV1ApiResponse
     */
    public static TreeCategoryV1ApiResponse categoryResponseWithChildren(
            long categoryId,
            String categoryName,
            int depth,
            long parentCategoryId,
            List<TreeCategoryV1ApiResponse> children) {
        return new TreeCategoryV1ApiResponse(
                categoryId, categoryName, depth, parentCategoryId, children);
    }

    /**
     * 카테고리 트리 응답 목록 생성 (2단계 트리 구조).
     *
     * @return TreeCategoryV1ApiResponse 목록
     */
    public static List<TreeCategoryV1ApiResponse> categoryResponseTreeList() {
        TreeCategoryV1ApiResponse child1 = categoryResponse(11L, "상의", 2, 1L);
        TreeCategoryV1ApiResponse child2 = categoryResponse(12L, "하의", 2, 1L);

        TreeCategoryV1ApiResponse parent =
                categoryResponseWithChildren(1L, "여성", 1, 0L, List.of(child1, child2));

        return List.of(parent);
    }

    // ===== CategoryDisplayResult =====

    /**
     * 단일 카테고리 결과 생성 (자식 없음).
     *
     * @param categoryId 카테고리 ID
     * @return CategoryDisplayResult
     */
    public static CategoryDisplayResult displayResult(long categoryId) {
        return CategoryDisplayResult.leaf(categoryId, "여성", 0L, 1);
    }

    /**
     * 카테고리 결과 생성.
     *
     * @param categoryId 카테고리 ID
     * @param categoryName 카테고리명
     * @param parentCategoryId 부모 카테고리 ID
     * @param depth 카테고리 깊이
     * @return CategoryDisplayResult
     */
    public static CategoryDisplayResult displayResult(
            long categoryId, String categoryName, long parentCategoryId, int depth) {
        return CategoryDisplayResult.leaf(categoryId, categoryName, parentCategoryId, depth);
    }

    /**
     * 자식 포함 카테고리 결과 생성.
     *
     * @param categoryId 카테고리 ID
     * @param categoryName 카테고리명
     * @param parentCategoryId 부모 카테고리 ID
     * @param depth 카테고리 깊이
     * @param children 자식 카테고리 목록
     * @return CategoryDisplayResult
     */
    public static CategoryDisplayResult displayResultWithChildren(
            long categoryId,
            String categoryName,
            long parentCategoryId,
            int depth,
            List<CategoryDisplayResult> children) {
        return CategoryDisplayResult.of(
                categoryId, categoryName, parentCategoryId, depth, children);
    }

    /**
     * 카테고리 트리 결과 목록 생성 (2단계 트리 구조).
     *
     * @return CategoryDisplayResult 목록
     */
    public static List<CategoryDisplayResult> displayResultTreeList() {
        CategoryDisplayResult child1 = displayResult(11L, "상의", 1L, 2);
        CategoryDisplayResult child2 = displayResult(12L, "하의", 1L, 2);

        CategoryDisplayResult parent =
                displayResultWithChildren(1L, "여성", 0L, 1, List.of(child1, child2));

        return List.of(parent);
    }
}
