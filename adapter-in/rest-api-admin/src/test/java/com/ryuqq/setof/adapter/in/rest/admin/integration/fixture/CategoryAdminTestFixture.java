package com.ryuqq.setof.adapter.in.rest.admin.integration.fixture;

import com.ryuqq.setof.application.category.dto.response.CategoryTreeResponse;
import java.util.List;

/**
 * Category Admin 통합 테스트 Fixture
 *
 * <p>Admin API 통합 테스트에서 사용하는 Category 관련 상수 및 Response 빌더를 제공합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
public final class CategoryAdminTestFixture {

    private CategoryAdminTestFixture() {
        // Utility class
    }

    // ============================================================
    // Category Constants
    // ============================================================

    public static final Long DEFAULT_CATEGORY_ID = 1L;
    public static final String DEFAULT_CATEGORY_CODE = "CAT001";
    public static final String DEFAULT_CATEGORY_NAME_KO = "테스트 카테고리";
    public static final int DEFAULT_DEPTH = 1;
    public static final int DEFAULT_SORT_ORDER = 0;

    // ============================================================
    // Response Builders for Mocking
    // ============================================================

    /**
     * CategoryTreeResponse Mock 데이터를 생성합니다. (리프 노드)
     *
     * @return CategoryTreeResponse
     */
    public static CategoryTreeResponse createCategoryTreeResponse() {
        return createCategoryTreeResponse(DEFAULT_CATEGORY_ID, DEFAULT_CATEGORY_CODE, true);
    }

    /**
     * 커스텀 값으로 CategoryTreeResponse Mock 데이터를 생성합니다.
     *
     * @param id 카테고리 ID
     * @param code 카테고리 코드
     * @param isLeaf 리프 노드 여부
     * @return CategoryTreeResponse
     */
    public static CategoryTreeResponse createCategoryTreeResponse(
            Long id, String code, boolean isLeaf) {
        return new CategoryTreeResponse(
                id,
                code,
                DEFAULT_CATEGORY_NAME_KO,
                DEFAULT_DEPTH,
                DEFAULT_SORT_ORDER,
                isLeaf,
                List.of());
    }

    /**
     * 자식 카테고리를 포함한 CategoryTreeResponse Mock 데이터를 생성합니다.
     *
     * @param id 카테고리 ID
     * @param children 자식 카테고리 목록
     * @return CategoryTreeResponse
     */
    public static CategoryTreeResponse createCategoryTreeResponseWithChildren(
            Long id, List<CategoryTreeResponse> children) {
        return new CategoryTreeResponse(
                id,
                "CAT" + String.format("%03d", id),
                DEFAULT_CATEGORY_NAME_KO,
                DEFAULT_DEPTH,
                DEFAULT_SORT_ORDER,
                false,
                children);
    }

    /**
     * 여러 루트 카테고리를 생성합니다. (트리 구조 없음)
     *
     * @param count 개수
     * @return CategoryTreeResponse 목록
     */
    public static List<CategoryTreeResponse> createCategoryTreeResponses(int count) {
        return java.util.stream.IntStream.rangeClosed(1, count)
                .mapToObj(
                        i ->
                                new CategoryTreeResponse(
                                        (long) i,
                                        "CAT" + String.format("%03d", i),
                                        DEFAULT_CATEGORY_NAME_KO + " " + i,
                                        DEFAULT_DEPTH,
                                        i,
                                        true,
                                        List.of()))
                .toList();
    }

    /**
     * 계층 구조를 가진 카테고리 트리를 생성합니다.
     *
     * @return CategoryTreeResponse (루트 → 자식 2개 → 각 자식 1개)
     */
    public static List<CategoryTreeResponse> createCategoryTreeWithHierarchy() {
        // 손자 카테고리
        CategoryTreeResponse grandChild1 =
                new CategoryTreeResponse(111L, "CAT111", "손자 카테고리 1-1-1", 3, 0, true, List.of());
        CategoryTreeResponse grandChild2 =
                new CategoryTreeResponse(121L, "CAT121", "손자 카테고리 1-2-1", 3, 0, true, List.of());

        // 자식 카테고리
        CategoryTreeResponse child1 =
                new CategoryTreeResponse(
                        11L, "CAT011", "자식 카테고리 1-1", 2, 0, false, List.of(grandChild1));
        CategoryTreeResponse child2 =
                new CategoryTreeResponse(
                        12L, "CAT012", "자식 카테고리 1-2", 2, 1, false, List.of(grandChild2));

        // 루트 카테고리
        CategoryTreeResponse root =
                new CategoryTreeResponse(
                        1L, "CAT001", "루트 카테고리 1", 1, 0, false, List.of(child1, child2));

        return List.of(root);
    }
}
