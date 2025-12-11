package com.ryuqq.setof.application.category.assembler;

import com.ryuqq.setof.application.category.dto.response.CategoryPathResponse;
import com.ryuqq.setof.application.category.dto.response.CategoryPathResponse.BreadcrumbItem;
import com.ryuqq.setof.application.category.dto.response.CategoryResponse;
import com.ryuqq.setof.application.category.dto.response.CategoryTreeResponse;
import com.ryuqq.setof.domain.category.aggregate.Category;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/**
 * Category Assembler
 *
 * <p>Category 도메인 객체를 Response DTO로 변환하는 Assembler
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class CategoryAssembler {

    /**
     * Category 도메인을 CategoryResponse로 변환
     *
     * @param category Category 도메인 객체
     * @return CategoryResponse
     */
    public CategoryResponse toCategoryResponse(Category category) {
        return CategoryResponse.of(
                category.getIdValue(),
                category.getCodeValue(),
                category.getNameKoValue(),
                category.getParentId(),
                category.getDepthValue(),
                category.getPathValue(),
                category.getSortOrder(),
                category.isLeaf(),
                category.getStatusValue());
    }

    /**
     * Category 도메인 목록을 CategoryResponse 목록으로 변환
     *
     * @param categories Category 도메인 목록
     * @return CategoryResponse 목록
     */
    public List<CategoryResponse> toCategoryResponses(List<Category> categories) {
        return categories.stream()
                .sorted(Comparator.comparingInt(Category::getSortOrder))
                .map(this::toCategoryResponse)
                .toList();
    }

    /**
     * Category 도메인 목록을 트리 구조로 변환
     *
     * @param categories 전체 Category 도메인 목록
     * @return CategoryTreeResponse 목록 (최상위부터)
     */
    public List<CategoryTreeResponse> toCategoryTreeResponses(List<Category> categories) {
        // parentId별로 그룹화
        Map<Long, List<Category>> categoryByParent =
                categories.stream()
                        .collect(
                                Collectors.groupingBy(
                                        c -> c.getParentId() != null ? c.getParentId() : 0L));

        // 최상위 카테고리부터 재귀적으로 트리 구성
        return buildTree(categoryByParent, 0L);
    }

    /** 재귀적으로 트리 구조 생성 */
    private List<CategoryTreeResponse> buildTree(
            Map<Long, List<Category>> categoryByParent, Long parentId) {
        List<Category> children = categoryByParent.getOrDefault(parentId, List.of());

        return children.stream()
                .sorted(Comparator.comparingInt(Category::getSortOrder))
                .map(
                        category -> {
                            List<CategoryTreeResponse> childTree =
                                    buildTree(categoryByParent, category.getIdValue());
                            return CategoryTreeResponse.of(
                                    category.getIdValue(),
                                    category.getCodeValue(),
                                    category.getNameKoValue(),
                                    category.getDepthValue(),
                                    category.getSortOrder(),
                                    category.isLeaf(),
                                    childTree);
                        })
                .toList();
    }

    /**
     * Category 도메인 목록을 경로 응답으로 변환
     *
     * @param categoryId 현재 카테고리 ID
     * @param pathCategories 경로 카테고리 목록 (깊이순 정렬)
     * @return CategoryPathResponse
     */
    public CategoryPathResponse toCategoryPathResponse(
            Long categoryId, List<Category> pathCategories) {
        List<BreadcrumbItem> breadcrumbs =
                pathCategories.stream()
                        .sorted(Comparator.comparingInt(Category::getDepthValue))
                        .map(
                                category ->
                                        BreadcrumbItem.of(
                                                category.getIdValue(),
                                                category.getCodeValue(),
                                                category.getNameKoValue(),
                                                category.getDepthValue()))
                        .toList();

        return CategoryPathResponse.of(categoryId, breadcrumbs);
    }
}
