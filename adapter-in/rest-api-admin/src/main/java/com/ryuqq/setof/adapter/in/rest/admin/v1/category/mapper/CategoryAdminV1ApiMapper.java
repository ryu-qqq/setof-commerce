package com.ryuqq.setof.adapter.in.rest.admin.v1.category.mapper;

import com.ryuqq.setof.adapter.in.rest.admin.v1.category.dto.request.SearchCategoriesV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.category.dto.response.ProductCategoryV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.category.dto.response.TreeCategoryV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.common.dto.CustomPageableV1ApiResponse;
import com.ryuqq.setof.application.category.dto.query.CategorySearchParams;
import com.ryuqq.setof.application.category.dto.response.CategoryPageResult;
import com.ryuqq.setof.application.category.dto.response.CategoryResult;
import com.ryuqq.setof.application.category.dto.response.TreeCategoryResult;
import com.ryuqq.setof.application.common.dto.query.CommonSearchParams;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * CategoryAdminV1ApiMapper - 카테고리 Admin V1 API Request/Response 변환 매퍼.
 *
 * <p>API-MAP-001: Mapper는 @Component로 등록.
 *
 * <p>API-MAP-003: Application Result → API Response 변환.
 *
 * <p>레거시 CategoryController 흐름 변환.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Component
public class CategoryAdminV1ApiMapper {

    private static final String SEARCH_FIELD_DISPLAY_NAME = "displayName";
    private static final String DEFAULT_SORT_KEY = "createdAt";
    private static final String DEFAULT_SORT_DIRECTION = "ASC";
    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_SIZE = 20;

    /**
     * SearchCategoriesV1ApiRequest → CategorySearchParams 변환.
     *
     * <p>categoryName → displayName 필드 LIKE 검색. No-Offset(lastCategoryId)은 현재 미지원.
     *
     * @param request 검색 요청 DTO
     * @return CategorySearchParams
     */
    public CategorySearchParams toSearchParams(SearchCategoriesV1ApiRequest request) {
        String searchWord =
                request.categoryName() != null && !request.categoryName().isBlank()
                        ? request.categoryName().trim()
                        : null;
        int page = request.page() != null ? request.page() : DEFAULT_PAGE;
        int size = request.size() != null ? request.size() : DEFAULT_SIZE;

        CommonSearchParams searchParams =
                CommonSearchParams.of(
                        false, null, null, DEFAULT_SORT_KEY, DEFAULT_SORT_DIRECTION, page, size);

        return CategorySearchParams.of(
                SEARCH_FIELD_DISPLAY_NAME,
                searchWord,
                request.categoryDepth(),
                null, // displayed: null (전체)
                searchParams);
    }

    /**
     * TreeCategoryResult 목록 → TreeCategoryV1ApiResponse 목록 변환.
     *
     * @param results TreeCategoryResult 목록
     * @return TreeCategoryV1ApiResponse 목록
     */
    public List<TreeCategoryV1ApiResponse> toTreeListResponse(List<TreeCategoryResult> results) {
        return results.stream().map(this::toTreeResponse).toList();
    }

    /**
     * TreeCategoryResult → TreeCategoryV1ApiResponse 변환 (재귀).
     *
     * @param result TreeCategoryResult
     * @return TreeCategoryV1ApiResponse
     */
    public TreeCategoryV1ApiResponse toTreeResponse(TreeCategoryResult result) {
        List<TreeCategoryV1ApiResponse> children =
                result.children() != null && !result.children().isEmpty()
                        ? result.children().stream().map(this::toTreeResponse).toList()
                        : List.of();

        long categoryId = result.categoryId() != null ? result.categoryId() : 0L;
        long parentCategoryId = result.parentCategoryId() != null ? result.parentCategoryId() : 0L;
        String displayName = result.categoryName() != null ? result.categoryName() : "";

        return new TreeCategoryV1ApiResponse(
                categoryId,
                result.categoryName() != null ? result.categoryName() : "",
                displayName,
                result.depth(),
                parentCategoryId,
                children);
    }

    /**
     * TreeCategoryResult → TreeCategoryV1ApiResponse 변환 (leaf, children 빈 목록).
     *
     * <p>flat list 응답용 (fetchAllChildCategories, fetchAllParentCategories,
     * fetchAllParentCategoriesBulk).
     *
     * @param result TreeCategoryResult
     * @return TreeCategoryV1ApiResponse
     */
    public TreeCategoryV1ApiResponse toTreeLeafResponse(TreeCategoryResult result) {
        long categoryId = result.categoryId() != null ? result.categoryId() : 0L;
        long parentCategoryId = result.parentCategoryId() != null ? result.parentCategoryId() : 0L;
        String name = result.categoryName() != null ? result.categoryName() : "";
        return new TreeCategoryV1ApiResponse(
                categoryId, name, name, result.depth(), parentCategoryId, List.of());
    }

    /**
     * CategoryPageResult → CustomPageableV1ApiResponse 변환.
     *
     * @param pageResult Application 페이지 결과
     * @return CustomPageableV1ApiResponse
     */
    public CustomPageableV1ApiResponse<ProductCategoryV1ApiResponse> toPageResponse(
            CategoryPageResult pageResult) {
        List<ProductCategoryV1ApiResponse> content =
                pageResult.content().stream().map(this::toProductResponse).toList();
        return CustomPageableV1ApiResponse.of(
                content,
                pageResult.pageMeta().page(),
                pageResult.pageMeta().size(),
                pageResult.pageMeta().totalElements());
    }

    /**
     * CategoryResult → ProductCategoryV1ApiResponse 변환.
     *
     * @param result CategoryResult
     * @return ProductCategoryV1ApiResponse
     */
    public ProductCategoryV1ApiResponse toProductResponse(CategoryResult result) {
        long categoryId = result.categoryId() != null ? result.categoryId() : 0L;
        String categoryName = result.categoryName() != null ? result.categoryName() : "";
        String displayName = categoryName;
        String categoryFullPath =
                result.categoryFullPath() != null ? result.categoryFullPath() : "";
        String targetGroup = result.targetGroup() != null ? result.targetGroup() : "ALL";
        return new ProductCategoryV1ApiResponse(
                categoryId,
                categoryName,
                displayName,
                result.depth() != null ? result.depth() : 0,
                categoryFullPath,
                targetGroup);
    }
}
