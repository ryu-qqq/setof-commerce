package com.ryuqq.setof.application.category.manager.query;

import com.ryuqq.setof.application.category.port.out.query.CategoryQueryPort;
import com.ryuqq.setof.domain.category.aggregate.Category;
import com.ryuqq.setof.domain.category.exception.CategoryNotFoundException;
import com.ryuqq.setof.domain.category.query.criteria.CategorySearchCriteria;
import com.ryuqq.setof.domain.category.vo.CategoryCode;
import com.ryuqq.setof.domain.category.vo.CategoryId;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Category Read Manager
 *
 * <p>단일 Query Port 조회를 담당하는 Read Manager
 *
 * <p>읽기 전용 트랜잭션 관리
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class CategoryReadManager {

    private final CategoryQueryPort categoryQueryPort;

    public CategoryReadManager(CategoryQueryPort categoryQueryPort) {
        this.categoryQueryPort = categoryQueryPort;
    }

    /**
     * Category ID로 조회 (필수)
     *
     * @param categoryId Category ID (Long)
     * @return 조회된 Category
     * @throws CategoryNotFoundException Category를 찾을 수 없는 경우
     */
    @Transactional(readOnly = true)
    public Category findById(Long categoryId) {
        CategoryId id = CategoryId.of(categoryId);
        return categoryQueryPort
                .findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));
    }

    /**
     * Category ID로 조회 (선택)
     *
     * @param categoryId Category ID (Long)
     * @return 조회된 Category (Optional)
     */
    @Transactional(readOnly = true)
    public Optional<Category> findByIdOptional(Long categoryId) {
        CategoryId id = CategoryId.of(categoryId);
        return categoryQueryPort.findById(id);
    }

    /**
     * 카테고리 코드로 조회 (필수)
     *
     * @param categoryCode 카테고리 코드
     * @return 조회된 Category
     * @throws CategoryNotFoundException Category를 찾을 수 없는 경우
     */
    @Transactional(readOnly = true)
    public Category findByCode(String categoryCode) {
        CategoryCode code = CategoryCode.of(categoryCode);
        return categoryQueryPort
                .findByCode(code)
                .orElseThrow(() -> new CategoryNotFoundException(categoryCode));
    }

    /**
     * 부모 ID로 하위 카테고리 목록 조회
     *
     * @param parentId 부모 카테고리 ID
     * @return 하위 Category 목록
     */
    @Transactional(readOnly = true)
    public List<Category> findByParentId(Long parentId) {
        CategoryId id = CategoryId.of(parentId);
        return categoryQueryPort.findByParentId(id);
    }

    /**
     * 활성화된 모든 카테고리 목록 조회
     *
     * @return 활성 Category 목록
     */
    @Transactional(readOnly = true)
    public List<Category> findAllActive() {
        return categoryQueryPort.findAllActive();
    }

    /**
     * 검색 조건으로 카테고리 목록 조회
     *
     * @param criteria 검색 조건 (Domain Criteria)
     * @return Category 목록
     */
    @Transactional(readOnly = true)
    public List<Category> findByCriteria(CategorySearchCriteria criteria) {
        return categoryQueryPort.findByCriteria(criteria);
    }

    /**
     * ID 목록으로 카테고리 목록 조회
     *
     * @param categoryIds 카테고리 ID 목록
     * @return Category 목록
     */
    @Transactional(readOnly = true)
    public List<Category> findByIds(List<Long> categoryIds) {
        return categoryQueryPort.findByIds(categoryIds);
    }

    /**
     * Category ID 존재 여부 확인
     *
     * @param categoryId Category ID (Long)
     * @return 존재하면 true
     */
    @Transactional(readOnly = true)
    public boolean existsById(Long categoryId) {
        CategoryId id = CategoryId.of(categoryId);
        return categoryQueryPort.existsById(id);
    }

    /**
     * 활성 Category 존재 여부 확인
     *
     * @param categoryId Category ID (Long)
     * @return 존재하면 true
     */
    @Transactional(readOnly = true)
    public boolean existsActiveById(Long categoryId) {
        return categoryQueryPort.existsActiveById(categoryId);
    }
}
