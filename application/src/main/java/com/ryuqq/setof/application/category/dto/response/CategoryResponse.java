package com.ryuqq.setof.application.category.dto.response;

/**
 * 카테고리 정보 응답 DTO
 *
 * <p>카테고리 상세 조회 시 반환되는 응답 DTO입니다.
 *
 * @param id 카테고리 ID
 * @param code 카테고리 코드
 * @param nameKo 한글 카테고리명
 * @param parentId 부모 카테고리 ID (nullable)
 * @param depth 깊이
 * @param path 경로 (Path Enumeration)
 * @param sortOrder 정렬 순서
 * @param isLeaf 리프 노드 여부
 * @param status 카테고리 상태
 */
public record CategoryResponse(
        Long id,
        String code,
        String nameKo,
        Long parentId,
        int depth,
        String path,
        int sortOrder,
        boolean isLeaf,
        String status) {

    /**
     * Static Factory Method
     *
     * @param id 카테고리 ID
     * @param code 카테고리 코드
     * @param nameKo 한글 카테고리명
     * @param parentId 부모 카테고리 ID
     * @param depth 깊이
     * @param path 경로
     * @param sortOrder 정렬 순서
     * @param isLeaf 리프 노드 여부
     * @param status 카테고리 상태
     * @return CategoryResponse 인스턴스
     */
    public static CategoryResponse of(
            Long id,
            String code,
            String nameKo,
            Long parentId,
            int depth,
            String path,
            int sortOrder,
            boolean isLeaf,
            String status) {
        return new CategoryResponse(
                id, code, nameKo, parentId, depth, path, sortOrder, isLeaf, status);
    }
}
