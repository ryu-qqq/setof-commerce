package com.ryuqq.setof.application.category.assembler;

import com.ryuqq.setof.application.category.dto.response.CategoryDisplayResult;
import com.ryuqq.setof.application.category.dto.response.CategoryPageResult;
import com.ryuqq.setof.application.category.dto.response.CategoryResult;
import com.ryuqq.setof.application.category.dto.response.TreeCategoryResult;
import com.ryuqq.setof.domain.category.aggregate.Category;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * Category Assembler.
 *
 * <p>Domain → Result 변환 및 PageResult/TreeResult 생성을 담당합니다.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Component
public class CategoryAssembler {

    /**
     * Domain → CategoryResult 변환.
     *
     * @param category Category 도메인 객체
     * @return CategoryResult
     */
    public CategoryResult toResult(Category category) {
        String targetGroup = category.targetGroup() != null ? category.targetGroup().name() : null;
        return CategoryResult.of(
                category.idValue(),
                category.categoryNameValue(),
                category.parentCategoryIdValue(),
                category.categoryDepthValue(),
                category.isDisplayed(),
                category.pathValue(),
                category.pathValue(), // categoryFullPath: path에 경로가 저장된 경우 동일
                targetGroup);
    }

    /**
     * Domain List → CategoryResult List 변환.
     *
     * @param categories Category 도메인 객체 목록
     * @return CategoryResult 목록
     */
    public List<CategoryResult> toResults(List<Category> categories) {
        return categories.stream().map(this::toResult).toList();
    }

    /**
     * 페이지 결과 생성.
     *
     * @param categories Category 도메인 객체 목록
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @param totalCount 전체 개수
     * @return CategoryPageResult
     */
    public CategoryPageResult toPageResult(
            List<Category> categories, int page, int size, long totalCount) {
        List<CategoryResult> results = toResults(categories);
        return CategoryPageResult.of(results, page, size, totalCount);
    }

    /**
     * Domain → TreeCategoryResult 변환.
     *
     * @param category Category 도메인 객체
     * @param children 자식 카테고리 목록
     * @return TreeCategoryResult
     */
    public TreeCategoryResult toTreeResult(Category category, List<TreeCategoryResult> children) {
        return TreeCategoryResult.of(
                category.idValue(),
                category.categoryNameValue(),
                category.parentCategoryIdValue(),
                category.categoryDepthValue(),
                category.isDisplayed(),
                category.pathValue(),
                children);
    }

    /**
     * Category 리스트를 트리 구조로 변환.
     *
     * @param categories Category 도메인 객체 목록 (정렬되어 있어야 함)
     * @return TreeCategoryResult 트리 구조
     */
    public List<TreeCategoryResult> toTreeResults(List<Category> categories) {
        Map<Long, List<Category>> childrenMap = new HashMap<>();
        List<Category> roots = new ArrayList<>();

        for (Category category : categories) {
            Long parentId = category.parentCategoryIdValue();
            if (parentId == 0L) {
                roots.add(category);
            } else {
                childrenMap.computeIfAbsent(parentId, k -> new ArrayList<>()).add(category);
            }
        }

        return roots.stream().map(root -> buildTree(root, childrenMap)).toList();
    }

    private TreeCategoryResult buildTree(Category category, Map<Long, List<Category>> childrenMap) {
        List<Category> children = childrenMap.getOrDefault(category.idValue(), List.of());
        List<TreeCategoryResult> childResults =
                children.stream().map(child -> buildTree(child, childrenMap)).toList();
        return toTreeResult(category, childResults);
    }

    /**
     * Domain → TreeCategoryResult leaf (자식 없음) 변환.
     *
     * @param category Category 도메인 객체
     * @return TreeCategoryResult (children 비어있음)
     */
    public TreeCategoryResult toLeafResult(Category category) {
        return TreeCategoryResult.leaf(
                category.idValue(),
                category.categoryNameValue(),
                category.parentCategoryIdValue(),
                category.categoryDepthValue(),
                category.isDisplayed(),
                category.pathValue());
    }

    /**
     * Domain List → TreeCategoryResult leaf List 변환.
     *
     * <p>부모 카테고리 조회 시 사용 (자식 없는 flat 리스트)
     *
     * @param categories Category 도메인 객체 목록
     * @return TreeCategoryResult 목록 (각 요소 children 비어있음)
     */
    public List<TreeCategoryResult> toLeafResults(List<Category> categories) {
        return categories.stream().map(this::toLeafResult).toList();
    }

    /**
     * Domain → CategoryDisplayResult 변환.
     *
     * @param category Category 도메인 객체
     * @param children 자식 카테고리 목록
     * @return CategoryDisplayResult
     */
    public CategoryDisplayResult toDisplayResult(
            Category category, List<CategoryDisplayResult> children) {
        return CategoryDisplayResult.of(
                category.idValue(),
                category.displayNameValue(),
                category.parentCategoryIdValue(),
                category.categoryDepthValue(),
                children);
    }

    /**
     * Category 리스트를 CategoryDisplayResult 트리 구조로 변환.
     *
     * @param categories Category 도메인 객체 목록 (정렬되어 있어야 함)
     * @return CategoryDisplayResult 트리 구조
     */
    public List<CategoryDisplayResult> toDisplayResults(List<Category> categories) {
        Map<Long, List<Category>> childrenMap = new HashMap<>();
        List<Category> roots = new ArrayList<>();

        for (Category category : categories) {
            Long parentId = category.parentCategoryIdValue();
            if (parentId == 0L) {
                roots.add(category);
            } else {
                childrenMap.computeIfAbsent(parentId, k -> new ArrayList<>()).add(category);
            }
        }

        return roots.stream().map(root -> buildDisplayTree(root, childrenMap)).toList();
    }

    private CategoryDisplayResult buildDisplayTree(
            Category category, Map<Long, List<Category>> childrenMap) {
        List<Category> children = childrenMap.getOrDefault(category.idValue(), List.of());
        List<CategoryDisplayResult> childResults =
                children.stream().map(child -> buildDisplayTree(child, childrenMap)).toList();
        return toDisplayResult(category, childResults);
    }
}
