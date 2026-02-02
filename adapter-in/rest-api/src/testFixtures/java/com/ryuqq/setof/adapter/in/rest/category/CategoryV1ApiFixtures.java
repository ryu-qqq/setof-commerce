package com.ryuqq.setof.adapter.in.rest.category;

import com.ryuqq.setof.adapter.in.rest.v1.category.dto.response.CategoryDisplayV1ApiResponse;
import com.ryuqq.setof.application.category.dto.response.CategoryDisplayResult;
import java.util.List;

/**
 * Category V1 API 테스트 Fixtures.
 *
 * <p>카테고리 V1 API 테스트에서 사용되는 Response/Result 객체들을 생성합니다.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
public final class CategoryV1ApiFixtures {

    private CategoryV1ApiFixtures() {}

    // ===== 기본 상수 =====
    public static final Long DEFAULT_CATEGORY_ID = 1L;
    public static final String DEFAULT_CATEGORY_NAME = "의류";
    public static final Long DEFAULT_PARENT_ID = 0L;
    public static final int DEFAULT_DEPTH = 1;

    // ===== CategoryDisplayResult Fixtures =====

    /** 기본 카테고리 표시 결과 (리프 노드) */
    public static CategoryDisplayResult categoryDisplayResult() {
        return CategoryDisplayResult.leaf(
                DEFAULT_CATEGORY_ID, DEFAULT_CATEGORY_NAME, DEFAULT_PARENT_ID, DEFAULT_DEPTH);
    }

    /** 자식 포함 카테고리 표시 결과 */
    public static CategoryDisplayResult categoryDisplayResultWithChildren() {
        CategoryDisplayResult child1 = CategoryDisplayResult.leaf(2L, "상의", DEFAULT_CATEGORY_ID, 2);
        CategoryDisplayResult child2 = CategoryDisplayResult.leaf(3L, "하의", DEFAULT_CATEGORY_ID, 2);
        return CategoryDisplayResult.of(
                DEFAULT_CATEGORY_ID,
                DEFAULT_CATEGORY_NAME,
                DEFAULT_PARENT_ID,
                DEFAULT_DEPTH,
                List.of(child1, child2));
    }

    /** 여러 최상위 카테고리 표시 결과 */
    public static List<CategoryDisplayResult> multipleCategoryDisplayResults() {
        return List.of(
                CategoryDisplayResult.leaf(1L, "의류", 0L, 1),
                CategoryDisplayResult.leaf(10L, "신발", 0L, 1),
                CategoryDisplayResult.leaf(20L, "가방", 0L, 1));
    }

    /** 트리 구조 카테고리 표시 결과 */
    public static List<CategoryDisplayResult> treeCategoryDisplayResults() {
        CategoryDisplayResult child1 = CategoryDisplayResult.leaf(2L, "상의", 1L, 2);
        CategoryDisplayResult child2 = CategoryDisplayResult.leaf(3L, "하의", 1L, 2);
        CategoryDisplayResult parent =
                CategoryDisplayResult.of(1L, "의류", 0L, 1, List.of(child1, child2));

        return List.of(
                parent,
                CategoryDisplayResult.leaf(10L, "신발", 0L, 1),
                CategoryDisplayResult.leaf(20L, "가방", 0L, 1));
    }

    // ===== Response Fixtures =====

    /** 기본 카테고리 표시 응답 (리프 노드) */
    public static CategoryDisplayV1ApiResponse categoryDisplayResponse() {
        return new CategoryDisplayV1ApiResponse(
                DEFAULT_CATEGORY_ID,
                DEFAULT_CATEGORY_NAME,
                DEFAULT_DEPTH,
                DEFAULT_PARENT_ID,
                List.of());
    }

    /** 자식 포함 카테고리 표시 응답 */
    public static CategoryDisplayV1ApiResponse categoryDisplayResponseWithChildren() {
        CategoryDisplayV1ApiResponse child1 =
                new CategoryDisplayV1ApiResponse(2L, "상의", 2, DEFAULT_CATEGORY_ID, List.of());
        CategoryDisplayV1ApiResponse child2 =
                new CategoryDisplayV1ApiResponse(3L, "하의", 2, DEFAULT_CATEGORY_ID, List.of());
        return new CategoryDisplayV1ApiResponse(
                DEFAULT_CATEGORY_ID,
                DEFAULT_CATEGORY_NAME,
                DEFAULT_DEPTH,
                DEFAULT_PARENT_ID,
                List.of(child1, child2));
    }

    /** 여러 카테고리 표시 응답 */
    public static List<CategoryDisplayV1ApiResponse> multipleCategoryDisplayResponses() {
        return List.of(
                new CategoryDisplayV1ApiResponse(1L, "의류", 1, 0L, List.of()),
                new CategoryDisplayV1ApiResponse(10L, "신발", 1, 0L, List.of()),
                new CategoryDisplayV1ApiResponse(20L, "가방", 1, 0L, List.of()));
    }
}
