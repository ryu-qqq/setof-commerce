package com.ryuqq.setof.adapter.in.rest.admin.v1.category.mapper;

import com.ryuqq.setof.adapter.in.rest.admin.v1.category.dto.query.CategorySearchV1ApiRequest;
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
 * CategoryAdminV1ApiMapper - V1 Admin 카테고리 API 매퍼.
 *
 * <p>Application Layer의 결과를 V1 Admin API 응답으로 변환합니다.
 *
 * <p>레거시 TreeCategoryContext, ProductCategoryContext와 동일한 구조로 변환합니다.
 *
 * <p>API-MAP-001: Mapper는 @Component로 정의.
 *
 * <p>API-MAP-002: 순수 변환 로직만 포함 (비즈니스 로직 금지).
 *
 * <p>API-MAP-003: null-safe 변환 필수.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Component
public class CategoryAdminV1ApiMapper {

    private static final Integer DEFAULT_PAGE = 0;
    private static final Integer DEFAULT_SIZE = 20;
    private static final String DEFAULT_TARGET_GROUP = "UNISEX";

    /**
     * CategorySearchV1ApiRequest를 CategorySearchParams로 변환.
     *
     * <p>기본값 처리를 Mapper에서 수행합니다.
     *
     * @param request V1 검색 요청
     * @return UseCase 검색 파라미터
     */
    public CategorySearchParams toSearchParams(CategorySearchV1ApiRequest request) {
        Integer page = request.page() != null ? request.page() : DEFAULT_PAGE;
        Integer size = request.size() != null ? request.size() : DEFAULT_SIZE;

        CommonSearchParams commonParams =
                CommonSearchParams.of(false, null, null, null, null, page, size);

        return CategorySearchParams.of(
                request.categoryName(), request.depth(), request.displayed(), commonParams);
    }

    /**
     * CategoryPageResult를 V1 CustomPageable 호환 페이지 응답으로 변환.
     *
     * <p>레거시 CustomPageable + ProductCategoryContext와 동일한 JSON 구조를 반환합니다.
     *
     * @param pageResult UseCase 실행 결과
     * @return V1 호환 페이지 응답 (레거시 CustomPageable 구조)
     */
    public CustomPageableV1ApiResponse<ProductCategoryV1ApiResponse> toPageResponse(
            CategoryPageResult pageResult) {
        List<ProductCategoryV1ApiResponse> content =
                pageResult.content().stream().map(this::toProductCategoryResponse).toList();
        return CustomPageableV1ApiResponse.of(
                content, pageResult.page(), pageResult.size(), pageResult.totalCount());
    }

    /**
     * CategoryResult를 V1 상품 카테고리 응답으로 변환.
     *
     * <p>레거시 ProductCategoryContext와 동일한 필드로 변환합니다.
     *
     * @param category 카테고리 조회 결과
     * @return V1 호환 상품 카테고리 응답
     */
    private ProductCategoryV1ApiResponse toProductCategoryResponse(CategoryResult category) {
        return new ProductCategoryV1ApiResponse(
                category.categoryId(),
                category.categoryName(),
                category.categoryName(),
                category.depth(),
                DEFAULT_TARGET_GROUP,
                category.targetUrl() != null ? category.targetUrl() : "");
    }

    /**
     * TreeCategoryResult 목록을 V1 트리 카테고리 응답 목록으로 변환.
     *
     * <p>레거시 TreeCategoryContext와 동일한 구조로 변환합니다.
     *
     * @param treeResults 트리 카테고리 결과 목록
     * @return V1 호환 트리 카테고리 응답 목록
     */
    public List<TreeCategoryV1ApiResponse> toTreeResponse(List<TreeCategoryResult> treeResults) {
        return treeResults.stream().map(this::toTreeResponse).toList();
    }

    /**
     * TreeCategoryResult를 V1 트리 카테고리 응답으로 재귀 변환.
     *
     * <p>레거시 TreeCategoryContext와 동일한 필드로 변환합니다.
     *
     * @param result 트리 카테고리 결과
     * @return V1 호환 트리 카테고리 응답
     */
    private TreeCategoryV1ApiResponse toTreeResponse(TreeCategoryResult result) {
        List<TreeCategoryV1ApiResponse> children =
                result.children().stream().map(this::toTreeResponse).toList();

        return new TreeCategoryV1ApiResponse(
                result.categoryId(),
                result.categoryName(),
                result.categoryName(),
                result.depth(),
                result.parentCategoryId(),
                children);
    }
}
