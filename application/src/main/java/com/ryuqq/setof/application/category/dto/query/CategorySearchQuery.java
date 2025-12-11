package com.ryuqq.setof.application.category.dto.query;

/**
 * 카테고리 검색 조건 DTO
 *
 * <p>카테고리 목록 조회 시 사용되는 검색 조건입니다.
 *
 * @param parentId 부모 카테고리 ID (null이면 최상위)
 * @param depth 깊이 필터 (null이면 전체)
 * @param status 카테고리 상태 (ACTIVE, INACTIVE, null=전체)
 * @param includeInactive 비활성 포함 여부
 */
public record CategorySearchQuery(
        Long parentId, Integer depth, String status, boolean includeInactive) {

    /**
     * Static Factory Method - 전체 조건
     *
     * @param parentId 부모 카테고리 ID
     * @param depth 깊이
     * @param status 카테고리 상태
     * @param includeInactive 비활성 포함 여부
     * @return CategorySearchQuery 인스턴스
     */
    public static CategorySearchQuery of(
            Long parentId, Integer depth, String status, boolean includeInactive) {
        return new CategorySearchQuery(parentId, depth, status, includeInactive);
    }

    /**
     * Static Factory Method - 최상위 카테고리 조회
     *
     * @return CategorySearchQuery 인스턴스
     */
    public static CategorySearchQuery forRootCategories() {
        return new CategorySearchQuery(null, 0, "ACTIVE", false);
    }

    /**
     * Static Factory Method - 하위 카테고리 조회
     *
     * @param parentId 부모 카테고리 ID
     * @return CategorySearchQuery 인스턴스
     */
    public static CategorySearchQuery forChildren(Long parentId) {
        return new CategorySearchQuery(parentId, null, "ACTIVE", false);
    }

    /**
     * Static Factory Method - 활성 전체 조회
     *
     * @return CategorySearchQuery 인스턴스
     */
    public static CategorySearchQuery forAllActive() {
        return new CategorySearchQuery(null, null, "ACTIVE", false);
    }

    /**
     * 부모 ID 존재 여부 확인
     *
     * @return 부모 ID가 존재하면 true
     */
    public boolean hasParentId() {
        return parentId != null;
    }

    /**
     * 깊이 필터 존재 여부 확인
     *
     * @return 깊이 필터가 존재하면 true
     */
    public boolean hasDepth() {
        return depth != null;
    }

    /**
     * 상태 필터 존재 여부 확인
     *
     * @return 상태 필터가 존재하면 true
     */
    public boolean hasStatus() {
        return status != null && !status.isBlank();
    }
}
