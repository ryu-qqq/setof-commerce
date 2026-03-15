package com.ryuqq.setof.application.category.port.out;

import com.ryuqq.setof.domain.category.aggregate.Category;
import com.ryuqq.setof.domain.category.id.CategoryId;
import com.ryuqq.setof.domain.category.query.CategorySearchCriteria;
import java.util.List;
import java.util.Optional;

/**
 * Category Query Port.
 *
 * <p>카테고리 조회 관련 Port-Out 인터페이스입니다.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
public interface CategoryQueryPort {

    Optional<Category> findById(CategoryId id);

    List<Category> findByIds(List<CategoryId> ids);

    boolean existsById(CategoryId id);

    List<Category> findByCriteria(CategorySearchCriteria criteria);

    long countByCriteria(CategorySearchCriteria criteria);

    List<Category> findAllDisplayed();

    List<Category> findChildrenByParentId(CategoryId parentId);

    List<Category> findParentsByChildId(CategoryId childId);

    /**
     * 주어진 카테고리의 모든 하위 카테고리 ID 조회 (자기 자신 포함).
     *
     * <p>Recursive CTE로 전체 하위 트리를 조회하여 ID만 반환한다.
     *
     * @param categoryId 부모 카테고리 ID
     * @return 하위 카테고리 ID 목록 (자기 자신 포함)
     */
    List<Long> findDescendantIds(CategoryId categoryId);
}
