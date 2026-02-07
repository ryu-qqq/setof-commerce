package com.ryuqq.setof.storage.legacy.composite.web.category.repository;

import com.ryuqq.setof.storage.legacy.composite.web.category.dto.LegacyWebTreeCategoryQueryDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * LegacyWebCategoryHierarchyRepository - 레거시 Web 카테고리 계층 조회 Repository.
 *
 * <p>Recursive CTE를 사용하는 Native Query 전용 Repository.
 *
 * <p>PER-REP-005: Recursive CTE는 Native Query로 구현.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Repository
public class LegacyWebCategoryHierarchyRepository {

    private final EntityManager entityManager;

    public LegacyWebCategoryHierarchyRepository(
            @Qualifier("legacyEntityManager") EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * 하위 카테고리 전체 조회 (Recursive CTE).
     *
     * <p>기준 카테고리부터 모든 하위 카테고리를 재귀적으로 탐색. depth 오름차순 정렬.
     *
     * @param categoryId 기준 카테고리 ID
     * @return 하위 카테고리 목록 (기준 카테고리 포함)
     */
    @SuppressWarnings("unchecked")
    public List<LegacyWebTreeCategoryQueryDto> fetchAllChildCategories(Long categoryId) {
        String sql =
                """
WITH RECURSIVE sub AS (
    SELECT category_id, CATEGORY_NAME, DISPLAY_NAME, CATEGORY_DEPTH, PARENT_CATEGORY_ID
    FROM category
    WHERE category_id = :categoryId

    UNION ALL

    SELECT c.category_id, c.CATEGORY_NAME, c.DISPLAY_NAME, c.CATEGORY_DEPTH, c.PARENT_CATEGORY_ID
    FROM sub
    INNER JOIN category c ON sub.category_id = c.PARENT_CATEGORY_ID
)
SELECT category_id, CATEGORY_NAME, DISPLAY_NAME, CATEGORY_DEPTH, PARENT_CATEGORY_ID
FROM sub
ORDER BY CATEGORY_DEPTH ASC
""";

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("categoryId", categoryId);

        List<Object[]> results = query.getResultList();
        return mapToTreeCategoryDtos(results);
    }

    /**
     * 상위 카테고리 전체 조회 (Recursive CTE).
     *
     * <p>기준 카테고리부터 루트까지 모든 상위 카테고리를 재귀적으로 탐색. depth 내림차순 정렬 (기준 카테고리 → 부모 → 조부모 → 루트).
     *
     * @param categoryId 기준 카테고리 ID
     * @return 상위 카테고리 목록 (기준 카테고리 포함)
     */
    @SuppressWarnings("unchecked")
    public List<LegacyWebTreeCategoryQueryDto> fetchAllParentCategories(Long categoryId) {
        String sql =
                """
WITH RECURSIVE sub AS (
    SELECT category_id, CATEGORY_NAME, DISPLAY_NAME, CATEGORY_DEPTH, PARENT_CATEGORY_ID
    FROM category
    WHERE category_id = :categoryId

    UNION ALL

    SELECT c.category_id, c.CATEGORY_NAME, c.DISPLAY_NAME, c.CATEGORY_DEPTH, c.PARENT_CATEGORY_ID
    FROM sub
    INNER JOIN category c ON c.category_id = sub.PARENT_CATEGORY_ID
)
SELECT category_id, CATEGORY_NAME, DISPLAY_NAME, CATEGORY_DEPTH, PARENT_CATEGORY_ID
FROM sub
ORDER BY CATEGORY_DEPTH DESC
""";

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("categoryId", categoryId);

        List<Object[]> results = query.getResultList();
        return mapToTreeCategoryDtos(results);
    }

    /**
     * 상위 카테고리 전체 조회 (Breadcrumb용, 오름차순).
     *
     * <p>루트부터 기준 카테고리까지 (Breadcrumb 표시용).
     *
     * @param categoryId 기준 카테고리 ID
     * @return 상위 카테고리 목록 (루트 → 기준 카테고리)
     */
    @SuppressWarnings("unchecked")
    public List<LegacyWebTreeCategoryQueryDto> fetchBreadcrumb(Long categoryId) {
        String sql =
                """
WITH RECURSIVE sub AS (
    SELECT category_id, CATEGORY_NAME, DISPLAY_NAME, CATEGORY_DEPTH, PARENT_CATEGORY_ID
    FROM category
    WHERE category_id = :categoryId

    UNION ALL

    SELECT c.category_id, c.CATEGORY_NAME, c.DISPLAY_NAME, c.CATEGORY_DEPTH, c.PARENT_CATEGORY_ID
    FROM sub
    INNER JOIN category c ON c.category_id = sub.PARENT_CATEGORY_ID
)
SELECT category_id, CATEGORY_NAME, DISPLAY_NAME, CATEGORY_DEPTH, PARENT_CATEGORY_ID
FROM sub
ORDER BY CATEGORY_DEPTH ASC
""";

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("categoryId", categoryId);

        List<Object[]> results = query.getResultList();
        return mapToTreeCategoryDtos(results);
    }

    /**
     * Native Query 결과를 DTO로 변환.
     *
     * @param results Native Query 결과
     * @return LegacyWebTreeCategoryQueryDto 목록
     */
    private List<LegacyWebTreeCategoryQueryDto> mapToTreeCategoryDtos(List<Object[]> results) {
        return results.stream()
                .map(
                        row ->
                                new LegacyWebTreeCategoryQueryDto(
                                        ((Number) row[0]).longValue(), // category_id
                                        (String) row[1], // CATEGORY_NAME
                                        (String) row[2], // DISPLAY_NAME
                                        ((Number) row[3]).intValue(), // CATEGORY_DEPTH
                                        ((Number) row[4]).longValue() // PARENT_CATEGORY_ID
                                        ))
                .toList();
    }
}
