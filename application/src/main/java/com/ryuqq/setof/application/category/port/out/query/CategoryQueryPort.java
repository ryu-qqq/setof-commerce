package com.ryuqq.setof.application.category.port.out.query;

import com.ryuqq.setof.domain.category.aggregate.Category;
import com.ryuqq.setof.domain.category.query.criteria.CategorySearchCriteria;
import com.ryuqq.setof.domain.category.vo.CategoryCode;
import com.ryuqq.setof.domain.category.vo.CategoryId;
import java.util.List;
import java.util.Optional;

/**
 * Category Query Port (Query)
 *
 * <p>Category Aggregate를 조회하는 읽기 전용 Port
 *
 * @author development-team
 * @since 1.0.0
 */
public interface CategoryQueryPort {

    /**
     * ID로 Category 단건 조회
     *
     * @param id Category ID (Value Object)
     * @return Category Domain (Optional)
     */
    Optional<Category> findById(CategoryId id);

    /**
     * 카테고리 코드로 Category 단건 조회
     *
     * @param code 카테고리 코드 (Value Object)
     * @return Category Domain (Optional)
     */
    Optional<Category> findByCode(CategoryCode code);

    /**
     * 부모 ID로 하위 카테고리 목록 조회
     *
     * @param parentId 부모 카테고리 ID (Value Object)
     * @return 하위 Category 목록
     */
    List<Category> findByParentId(CategoryId parentId);

    /**
     * 활성화된 모든 카테고리 목록 조회
     *
     * @return 활성 Category 목록
     */
    List<Category> findAllActive();

    /**
     * 검색 조건으로 카테고리 목록 조회
     *
     * @param criteria 검색 조건 (Domain Criteria)
     * @return Category 목록
     */
    List<Category> findByCriteria(CategorySearchCriteria criteria);

    /**
     * 경로에 포함된 카테고리 목록 조회 (breadcrumb용)
     *
     * @param categoryIds 카테고리 ID 목록
     * @return Category 목록 (깊이순 정렬)
     */
    List<Category> findByIds(List<Long> categoryIds);

    /**
     * Category ID 존재 여부 확인
     *
     * @param id Category ID
     * @return 존재 여부
     */
    boolean existsById(CategoryId id);

    /**
     * Category ID로 활성 카테고리 존재 여부 확인
     *
     * @param categoryId Category ID (Long)
     * @return 존재 여부
     */
    boolean existsActiveById(Long categoryId);
}
