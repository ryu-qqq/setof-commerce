package com.ryuqq.setof.application.category.dto.response;

import java.util.List;

/**
 * 트리 구조 카테고리 조회 결과.
 *
 * <p>재귀 구조로 자식 카테고리를 포함합니다.
 *
 * @param categoryId 카테고리 ID
 * @param categoryName 카테고리명
 * @param parentCategoryId 부모 카테고리 ID
 * @param depth 카테고리 깊이
 * @param displayed 노출 여부
 * @param path 카테고리 경로
 * @param children 자식 카테고리 목록
 * @author ryu-qqq
 * @since 1.0.0
 */
public record TreeCategoryResult(
        Long categoryId,
        String categoryName,
        Long parentCategoryId,
        int depth,
        boolean displayed,
        String path,
        List<TreeCategoryResult> children) {

    public static TreeCategoryResult of(
            Long categoryId,
            String categoryName,
            Long parentCategoryId,
            int depth,
            boolean displayed,
            String path,
            List<TreeCategoryResult> children) {
        return new TreeCategoryResult(
                categoryId, categoryName, parentCategoryId, depth, displayed, path, children);
    }

    public static TreeCategoryResult leaf(
            Long categoryId,
            String categoryName,
            Long parentCategoryId,
            int depth,
            boolean displayed,
            String path) {
        return new TreeCategoryResult(
                categoryId, categoryName, parentCategoryId, depth, displayed, path, List.of());
    }
}
