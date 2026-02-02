package com.ryuqq.setof.adapter.in.rest.admin.category;

import com.ryuqq.setof.adapter.in.rest.admin.v1.category.dto.query.CategorySearchV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.category.dto.response.ProductCategoryV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.category.dto.response.TreeCategoryV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.common.dto.CustomPageableV1ApiResponse;
import com.ryuqq.setof.application.category.dto.response.CategoryPageResult;
import com.ryuqq.setof.application.category.dto.response.CategoryResult;
import com.ryuqq.setof.application.category.dto.response.TreeCategoryResult;
import java.util.List;

/**
 * Category V1 API 테스트 Fixtures.
 *
 * <p>카테고리 V1 API 테스트에서 사용되는 Request/Response/Result 객체들을 생성합니다.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
public final class CategoryV1ApiFixtures {

    private CategoryV1ApiFixtures() {}

    // ===== 기본 상수 =====
    public static final Long DEFAULT_CATEGORY_ID = 1L;
    public static final String DEFAULT_CATEGORY_NAME = "의류";
    public static final String DEFAULT_DISPLAY_NAME = "의류";
    public static final Long DEFAULT_PARENT_ID = 0L;
    public static final int DEFAULT_DEPTH = 1;
    public static final String DEFAULT_PATH = "/1";
    public static final String DEFAULT_TARGET_GROUP = "MEN";

    // ===== Search Request Fixtures =====

    /** 기본 검색 요청 */
    public static CategorySearchV1ApiRequest searchRequest() {
        return new CategorySearchV1ApiRequest(null, null, null, 0, 20);
    }

    /** 카테고리명 검색 요청 */
    public static CategorySearchV1ApiRequest searchRequest(String categoryName) {
        return new CategorySearchV1ApiRequest(categoryName, null, null, 0, 20);
    }

    // ===== Tree Result Fixtures =====

    /** 기본 트리 카테고리 결과 */
    public static TreeCategoryResult treeCategoryResult() {
        return TreeCategoryResult.leaf(
                DEFAULT_CATEGORY_ID,
                DEFAULT_CATEGORY_NAME,
                DEFAULT_PARENT_ID,
                DEFAULT_DEPTH,
                true,
                DEFAULT_PATH);
    }

    /** 자식 포함 트리 카테고리 결과 */
    public static TreeCategoryResult treeCategoryResultWithChildren() {
        TreeCategoryResult child1 =
                TreeCategoryResult.leaf(2L, "상의", DEFAULT_CATEGORY_ID, 2, true, "/1/2");
        TreeCategoryResult child2 =
                TreeCategoryResult.leaf(3L, "하의", DEFAULT_CATEGORY_ID, 2, true, "/1/3");
        return TreeCategoryResult.of(
                DEFAULT_CATEGORY_ID,
                DEFAULT_CATEGORY_NAME,
                DEFAULT_PARENT_ID,
                DEFAULT_DEPTH,
                true,
                DEFAULT_PATH,
                List.of(child1, child2));
    }

    /** 여러 트리 카테고리 결과 */
    public static List<TreeCategoryResult> multipleTreeCategoryResults() {
        return List.of(
                TreeCategoryResult.leaf(1L, "의류", 0L, 1, true, "/1"),
                TreeCategoryResult.leaf(10L, "신발", 0L, 1, true, "/10"),
                TreeCategoryResult.leaf(20L, "가방", 0L, 1, true, "/20"));
    }

    // ===== Category Result Fixtures =====

    /** 기본 카테고리 결과 */
    public static CategoryResult categoryResult() {
        return categoryResult(DEFAULT_CATEGORY_ID, DEFAULT_CATEGORY_NAME);
    }

    /** 커스텀 카테고리 결과 */
    public static CategoryResult categoryResult(Long id, String categoryName) {
        return CategoryResult.of(
                id, categoryName, DEFAULT_PARENT_ID, DEFAULT_DEPTH, true, "/category/" + id);
    }

    /** 카테고리 페이지 결과 */
    public static CategoryPageResult pageResult() {
        return CategoryPageResult.of(List.of(categoryResult()), 1L, 0, 20);
    }

    /** 빈 페이지 결과 */
    public static CategoryPageResult emptyPageResult() {
        return CategoryPageResult.empty();
    }

    // ===== Response Fixtures (API 응답) =====

    /** 트리 카테고리 응답 */
    public static TreeCategoryV1ApiResponse treeCategoryResponse() {
        return new TreeCategoryV1ApiResponse(
                DEFAULT_CATEGORY_ID,
                DEFAULT_CATEGORY_NAME,
                DEFAULT_DISPLAY_NAME,
                DEFAULT_DEPTH,
                DEFAULT_PARENT_ID,
                List.of());
    }

    /** 상품 카테고리 응답 */
    public static ProductCategoryV1ApiResponse productCategoryResponse() {
        return new ProductCategoryV1ApiResponse(
                DEFAULT_CATEGORY_ID,
                DEFAULT_CATEGORY_NAME,
                DEFAULT_DISPLAY_NAME,
                DEFAULT_DEPTH,
                DEFAULT_TARGET_GROUP,
                DEFAULT_CATEGORY_NAME);
    }

    /** 페이지 응답 */
    public static CustomPageableV1ApiResponse<ProductCategoryV1ApiResponse> pageResponse() {
        return CustomPageableV1ApiResponse.of(List.of(productCategoryResponse()), 0, 20, 1L);
    }
}
