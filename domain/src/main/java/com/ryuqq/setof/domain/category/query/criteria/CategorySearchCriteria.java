package com.ryuqq.setof.domain.category.query.criteria;

/**
 * CategorySearchCriteria - 카테고리 검색 조건 Domain VO
 *
 * <p>카테고리 목록 조회 시 사용되는 검색 조건입니다.
 *
 * <p><strong>사용 위치:</strong>
 *
 * <ul>
 *   <li>Domain Layer에서 정의
 *   <li>Persistence Layer Repository에서 사용
 *   <li>Application Layer에서 생성 (Factory/Service)
 * </ul>
 *
 * <p><strong>의존성 방향:</strong>
 *
 * <pre>{@code
 * Application Layer → Domain Layer ← Persistence Layer
 *                      (Criteria)
 * }</pre>
 *
 * @param parentId 부모 카테고리 ID (null이면 최상위)
 * @param depth 깊이 필터 (null이면 전체)
 * @param status 카테고리 상태 (ACTIVE, INACTIVE, null=전체)
 * @param includeInactive 비활성 포함 여부
 * @author development-team
 * @since 1.0.0
 */
public record CategorySearchCriteria(
        Long parentId, Integer depth, String status, boolean includeInactive) {

    /**
     * Static Factory Method - 전체 조건
     *
     * @param parentId 부모 카테고리 ID
     * @param depth 깊이
     * @param status 카테고리 상태
     * @param includeInactive 비활성 포함 여부
     * @return CategorySearchCriteria 인스턴스
     */
    public static CategorySearchCriteria of(
            Long parentId, Integer depth, String status, boolean includeInactive) {
        return new CategorySearchCriteria(parentId, depth, status, includeInactive);
    }

    /**
     * Static Factory Method - 최상위 카테고리 조회
     *
     * @return CategorySearchCriteria 인스턴스
     */
    public static CategorySearchCriteria forRootCategories() {
        return new CategorySearchCriteria(null, 0, "ACTIVE", false);
    }

    /**
     * Static Factory Method - 하위 카테고리 조회
     *
     * @param parentId 부모 카테고리 ID
     * @return CategorySearchCriteria 인스턴스
     */
    public static CategorySearchCriteria forChildren(Long parentId) {
        return new CategorySearchCriteria(parentId, null, "ACTIVE", false);
    }

    /**
     * Static Factory Method - 활성 전체 조회
     *
     * @return CategorySearchCriteria 인스턴스
     */
    public static CategorySearchCriteria forAllActive() {
        return new CategorySearchCriteria(null, null, "ACTIVE", false);
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
