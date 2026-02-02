package com.ryuqq.setof.application.category.port.out.query;

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
}
