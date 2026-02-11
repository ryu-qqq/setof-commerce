package com.ryuqq.setof.adapter.in.rest.v1.category.mapper;

import com.ryuqq.setof.adapter.in.rest.v1.category.dto.response.TreeCategoryV1ApiResponse;
import com.ryuqq.setof.application.category.dto.response.CategoryDisplayResult;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * CategoryV1ApiMapper - 카테고리 V1 API Request/Response 변환 매퍼.
 *
 * <p>API-MAP-001: Mapper는 @Component로 등록.
 *
 * <p>API-MAP-002: 양방향 변환 지원.
 *
 * <p>API-MAP-003: Application Result → API Response 변환.
 *
 * <p>API-MAP-005: 순수 변환 로직만.
 *
 * <p>레거시 CategoryController.getCategories 흐름 변환.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Component
public class CategoryV1ApiMapper {

    /**
     * CategoryDisplayResult 목록 → TreeCategoryV1ApiResponse 목록 변환.
     *
     * <p>트리 구조를 재귀적으로 변환합니다.
     *
     * @param results CategoryDisplayResult 트리 목록
     * @return TreeCategoryV1ApiResponse 트리 목록
     */
    public List<TreeCategoryV1ApiResponse> toListResponse(List<CategoryDisplayResult> results) {
        return results.stream().map(this::toResponse).toList();
    }

    /**
     * CategoryDisplayResult → TreeCategoryV1ApiResponse 변환.
     *
     * <p>자식 노드를 재귀적으로 변환합니다.
     *
     * @param result CategoryDisplayResult
     * @return TreeCategoryV1ApiResponse
     */
    public TreeCategoryV1ApiResponse toResponse(CategoryDisplayResult result) {
        List<TreeCategoryV1ApiResponse> children =
                result.children() != null && !result.children().isEmpty()
                        ? result.children().stream().map(this::toResponse).toList()
                        : List.of();

        long categoryId = result.categoryId() != null ? result.categoryId() : 0L;
        long parentCategoryId = result.parentCategoryId() != null ? result.parentCategoryId() : 0L;

        return new TreeCategoryV1ApiResponse(
                categoryId,
                result.categoryName() != null ? result.categoryName() : "",
                result.depth(),
                parentCategoryId,
                children);
    }
}
