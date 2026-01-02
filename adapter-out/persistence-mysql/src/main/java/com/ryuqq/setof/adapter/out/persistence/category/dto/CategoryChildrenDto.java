package com.ryuqq.setof.adapter.out.persistence.category.dto;

/**
 * CategoryChildrenDto - Persistence Layer 전용 하위 카테고리 조회 DTO
 *
 * <p>Recursive CTE 쿼리 결과를 매핑하는 DTO입니다.
 *
 * <p><strong>사용 목적:</strong>
 *
 * <ul>
 *   <li>Native Query 결과 매핑
 *   <li>Domain Layer 의존성 분리
 * </ul>
 *
 * @param id 카테고리 ID
 * @param code 카테고리 코드
 * @param nameKo 한글 카테고리명
 * @param parentId 부모 카테고리 ID
 * @param depth 깊이
 * @param path 경로 (Path Enumeration)
 * @param sortOrder 정렬 순서
 * @param isLeaf 리프 노드 여부
 * @author development-team
 * @since 1.0.0
 */
public record CategoryChildrenDto(
        Long id,
        String code,
        String nameKo,
        Long parentId,
        Integer depth,
        String path,
        Integer sortOrder,
        Boolean isLeaf) {

    /**
     * Native Query 결과 매핑용 정적 팩토리 메서드
     *
     * @param id 카테고리 ID
     * @param code 카테고리 코드
     * @param nameKo 한글 카테고리명
     * @param parentId 부모 카테고리 ID (nullable)
     * @param depth 깊이
     * @param path 경로
     * @param sortOrder 정렬 순서
     * @param isLeaf 리프 노드 여부
     * @return CategoryChildrenDto 인스턴스
     */
    public static CategoryChildrenDto of(
            Long id,
            String code,
            String nameKo,
            Long parentId,
            Integer depth,
            String path,
            Integer sortOrder,
            Boolean isLeaf) {
        return new CategoryChildrenDto(id, code, nameKo, parentId, depth, path, sortOrder, isLeaf);
    }
}
