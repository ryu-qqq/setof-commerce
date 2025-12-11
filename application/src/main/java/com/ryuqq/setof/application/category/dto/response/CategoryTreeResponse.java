package com.ryuqq.setof.application.category.dto.response;

import java.util.List;

/**
 * 카테고리 트리 응답 DTO
 *
 * <p>카테고리 트리 구조 조회 시 반환되는 응답 DTO입니다. 재귀적 구조로 하위 카테고리를 포함합니다.
 *
 * @param id 카테고리 ID
 * @param code 카테고리 코드
 * @param nameKo 한글 카테고리명
 * @param depth 깊이
 * @param sortOrder 정렬 순서
 * @param isLeaf 리프 노드 여부
 * @param children 하위 카테고리 목록
 */
public record CategoryTreeResponse(
        Long id,
        String code,
        String nameKo,
        int depth,
        int sortOrder,
        boolean isLeaf,
        List<CategoryTreeResponse> children) {

    /**
     * Static Factory Method
     *
     * @param id 카테고리 ID
     * @param code 카테고리 코드
     * @param nameKo 한글 카테고리명
     * @param depth 깊이
     * @param sortOrder 정렬 순서
     * @param isLeaf 리프 노드 여부
     * @param children 하위 카테고리 목록
     * @return CategoryTreeResponse 인스턴스
     */
    public static CategoryTreeResponse of(
            Long id,
            String code,
            String nameKo,
            int depth,
            int sortOrder,
            boolean isLeaf,
            List<CategoryTreeResponse> children) {
        return new CategoryTreeResponse(id, code, nameKo, depth, sortOrder, isLeaf, children);
    }

    /**
     * 리프 노드 생성 (하위 카테고리 없음)
     *
     * @param id 카테고리 ID
     * @param code 카테고리 코드
     * @param nameKo 한글 카테고리명
     * @param depth 깊이
     * @param sortOrder 정렬 순서
     * @return CategoryTreeResponse 인스턴스
     */
    public static CategoryTreeResponse leaf(
            Long id, String code, String nameKo, int depth, int sortOrder) {
        return new CategoryTreeResponse(id, code, nameKo, depth, sortOrder, true, List.of());
    }
}
