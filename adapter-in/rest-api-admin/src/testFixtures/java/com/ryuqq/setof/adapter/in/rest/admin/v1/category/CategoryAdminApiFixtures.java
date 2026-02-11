package com.ryuqq.setof.adapter.in.rest.admin.v1.category;

import com.ryuqq.setof.adapter.in.rest.admin.v1.category.dto.request.SearchCategoriesV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.category.dto.response.ProductCategoryV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.category.dto.response.TreeCategoryV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.common.dto.CustomPageableV1ApiResponse;
import com.ryuqq.setof.application.category.dto.response.CategoryPageResult;
import com.ryuqq.setof.application.category.dto.response.CategoryResult;
import com.ryuqq.setof.application.category.dto.response.TreeCategoryResult;
import java.util.List;

/**
 * Category API 테스트 Fixtures.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
public final class CategoryAdminApiFixtures {

    private CategoryAdminApiFixtures() {}

    // ===== Request Fixtures =====

    public static SearchCategoriesV1ApiRequest searchCategoriesRequest() {
        return new SearchCategoriesV1ApiRequest(null, "의류", 2, null, 0, 20);
    }

    public static SearchCategoriesV1ApiRequest searchCategoriesRequestWithDefaults() {
        return new SearchCategoriesV1ApiRequest(null, null, null, null, null, null);
    }

    public static SearchCategoriesV1ApiRequest searchCategoriesRequestWithBlankName() {
        return new SearchCategoriesV1ApiRequest(null, "  ", null, null, 0, 20);
    }

    // ===== Application Result Fixtures =====

    public static TreeCategoryResult treeCategoryResult(Long id) {
        return TreeCategoryResult.of(
                id, "의류", 0L, 1, true, "의류", List.of(childTreeCategoryResult(id + 1, id)));
    }

    public static TreeCategoryResult treeCategoryResultWithNulls() {
        return TreeCategoryResult.of(null, null, null, 1, true, "path", List.of());
    }

    public static TreeCategoryResult childTreeCategoryResult(Long id, Long parentId) {
        return TreeCategoryResult.leaf(id, "상의", parentId, 2, true, "의류 > 상의");
    }

    public static TreeCategoryResult leafTreeCategoryResult(Long id) {
        return TreeCategoryResult.leaf(id, "티셔츠", 200L, 3, true, "의류 > 상의 > 티셔츠");
    }

    public static CategoryResult categoryResult(Long id) {
        return CategoryResult.of(
                id, "티셔츠", 200L, 3, true, "http://test.com/category", "의류 > 상의 > 티셔츠", "ALL");
    }

    public static CategoryResult categoryResultWithNulls() {
        return CategoryResult.of(null, null, null, null, null, null, null, null);
    }

    public static CategoryPageResult categoryPageResult() {
        return CategoryPageResult.of(
                List.of(categoryResult(100L), categoryResult(101L)), 0, 20, 50L);
    }

    public static CategoryPageResult categoryPageResultEmpty() {
        return CategoryPageResult.empty();
    }

    // ===== API Response Fixtures =====

    public static TreeCategoryV1ApiResponse treeCategoryApiResponse(Long id) {
        return new TreeCategoryV1ApiResponse(
                id, "의류", "의류", 1, 0L, List.of(childTreeCategoryApiResponse(id + 1, id)));
    }

    public static TreeCategoryV1ApiResponse childTreeCategoryApiResponse(Long id, Long parentId) {
        return new TreeCategoryV1ApiResponse(id, "상의", "상의", 2, parentId, List.of());
    }

    public static TreeCategoryV1ApiResponse leafTreeCategoryApiResponse(Long id) {
        return new TreeCategoryV1ApiResponse(id, "티셔츠", "티셔츠", 3, 200L, List.of());
    }

    public static ProductCategoryV1ApiResponse productCategoryApiResponse(Long id) {
        return new ProductCategoryV1ApiResponse(id, "티셔츠", "티셔츠", 3, "의류 > 상의 > 티셔츠", "ALL");
    }

    public static CustomPageableV1ApiResponse<ProductCategoryV1ApiResponse>
            productCategoryPageResponse() {
        List<ProductCategoryV1ApiResponse> content =
                List.of(productCategoryApiResponse(100L), productCategoryApiResponse(101L));
        return CustomPageableV1ApiResponse.of(content, 0, 20, 50L);
    }
}
