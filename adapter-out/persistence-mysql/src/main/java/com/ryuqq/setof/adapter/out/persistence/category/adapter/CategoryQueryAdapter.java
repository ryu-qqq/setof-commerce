package com.ryuqq.setof.adapter.out.persistence.category.adapter;

import com.ryuqq.setof.adapter.out.persistence.category.mapper.CategoryJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.category.repository.CategoryQueryDslRepository;
import com.ryuqq.setof.application.category.port.out.query.CategoryQueryPort;
import com.ryuqq.setof.domain.category.aggregate.Category;
import com.ryuqq.setof.domain.category.query.criteria.CategorySearchCriteria;
import com.ryuqq.setof.domain.category.vo.CategoryCode;
import com.ryuqq.setof.domain.category.vo.CategoryId;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * CategoryQueryAdapter - Category Query Adapter
 *
 * <p>CQRS의 Query(읽기) 담당으로, Category 조회 요청을 QueryDslRepository에 위임하고 Mapper를 통해 Domain으로 변환하여 반환합니다.
 *
 * <p><strong>책임:</strong>
 *
 * <ul>
 *   <li>ID로 단건 조회 (findById)
 *   <li>카테고리 코드로 조회 (findByCode)
 *   <li>부모 ID로 하위 카테고리 목록 조회 (findByParentId)
 *   <li>검색 조건으로 목록 조회 (findByCondition)
 *   <li>활성 카테고리 목록 조회 (findAllActive)
 *   <li>ID 목록으로 조회 (findByIds)
 *   <li>존재 여부 확인 (existsById, existsActiveById)
 * </ul>
 *
 * <p><strong>금지 사항:</strong>
 *
 * <ul>
 *   <li>비즈니스 로직 금지
 *   <li>저장/수정/삭제 금지 (CommandAdapter로 분리)
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class CategoryQueryAdapter implements CategoryQueryPort {

    private final CategoryQueryDslRepository queryDslRepository;
    private final CategoryJpaEntityMapper categoryJpaEntityMapper;

    public CategoryQueryAdapter(
            CategoryQueryDslRepository queryDslRepository,
            CategoryJpaEntityMapper categoryJpaEntityMapper) {
        this.queryDslRepository = queryDslRepository;
        this.categoryJpaEntityMapper = categoryJpaEntityMapper;
    }

    /**
     * ID로 Category 단건 조회
     *
     * @param id Category ID (Value Object)
     * @return Category Domain (Optional)
     */
    @Override
    public Optional<Category> findById(CategoryId id) {
        return queryDslRepository.findById(id.value()).map(categoryJpaEntityMapper::toDomain);
    }

    /**
     * 카테고리 코드로 Category 조회
     *
     * @param code 카테고리 코드 (Value Object)
     * @return Category Domain (Optional)
     */
    @Override
    public Optional<Category> findByCode(CategoryCode code) {
        return queryDslRepository.findByCode(code.value()).map(categoryJpaEntityMapper::toDomain);
    }

    /**
     * 부모 ID로 하위 카테고리 목록 조회
     *
     * @param parentId 부모 카테고리 ID (Value Object)
     * @return Category 목록 (sortOrder 순)
     */
    @Override
    public List<Category> findByParentId(CategoryId parentId) {
        return queryDslRepository.findByParentId(parentId.value()).stream()
                .map(categoryJpaEntityMapper::toDomain)
                .toList();
    }

    /**
     * 활성화된 모든 Category 목록 조회
     *
     * @return Category 목록 (depth, sortOrder 순)
     */
    @Override
    public List<Category> findAllActive() {
        return queryDslRepository.findAllActive().stream()
                .map(categoryJpaEntityMapper::toDomain)
                .toList();
    }

    /**
     * 검색 조건으로 Category 목록 조회
     *
     * @param criteria 검색 조건 (Domain Criteria)
     * @return Category 목록
     */
    @Override
    public List<Category> findByCriteria(CategorySearchCriteria criteria) {
        return queryDslRepository.findByCondition(criteria).stream()
                .map(categoryJpaEntityMapper::toDomain)
                .toList();
    }

    /**
     * ID 목록으로 Category 조회 (breadcrumb용)
     *
     * @param categoryIds ID 목록
     * @return Category 목록 (depth 순)
     */
    @Override
    public List<Category> findByIds(List<Long> categoryIds) {
        return queryDslRepository.findByIds(categoryIds).stream()
                .map(categoryJpaEntityMapper::toDomain)
                .toList();
    }

    /**
     * ID로 Category 존재 여부 확인
     *
     * @param id Category ID (Value Object)
     * @return 존재 여부
     */
    @Override
    public boolean existsById(CategoryId id) {
        return queryDslRepository.existsById(id.value());
    }

    /**
     * 활성 Category 존재 여부 확인
     *
     * @param categoryId Category ID (Long)
     * @return 활성 존재 여부
     */
    @Override
    public boolean existsActiveById(Long categoryId) {
        return queryDslRepository.existsActiveById(categoryId);
    }
}
