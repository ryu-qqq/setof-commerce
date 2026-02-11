package com.ryuqq.setof.adapter.out.persistence.category.adapter;

import com.ryuqq.setof.adapter.out.persistence.category.dto.CategoryTreeDto;
import com.ryuqq.setof.adapter.out.persistence.category.entity.CategoryJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.category.mapper.CategoryJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.category.repository.CategoryQueryDslRepository;
import com.ryuqq.setof.application.category.port.out.query.CategoryQueryPort;
import com.ryuqq.setof.domain.category.aggregate.Category;
import com.ryuqq.setof.domain.category.id.CategoryId;
import com.ryuqq.setof.domain.category.query.CategorySearchCriteria;
import java.util.List;
import java.util.Optional;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * CategoryQueryAdapter - 카테고리 Query 어댑터.
 *
 * <p>CategoryQueryPort를 구현하여 영속성 계층과 연결합니다.
 *
 * <p>PER-ADP-004: QueryAdapter는 QueryDslRepository만 사용.
 *
 * <p>PER-ADP-002: Adapter에서 @Transactional 금지.
 *
 * <p>PER-ADP-003: Domain 반환 (DTO 반환 금지).
 *
 * <p>PER-ADP-005: Entity -> Domain 변환 (Mapper 사용).
 *
 * <p>활성화 조건: persistence.legacy.category.enabled=false
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Component
@ConditionalOnProperty(name = "persistence.legacy.category.enabled", havingValue = "false")
public class CategoryQueryAdapter implements CategoryQueryPort {

    private final CategoryQueryDslRepository queryDslRepository;
    private final CategoryJpaEntityMapper mapper;

    /**
     * 생성자 주입.
     *
     * <p>PER-ADP-006: Mapper + QueryDslRepository 의존.
     *
     * @param queryDslRepository QueryDSL 레포지토리
     * @param mapper Entity-Domain 매퍼
     */
    public CategoryQueryAdapter(
            CategoryQueryDslRepository queryDslRepository, CategoryJpaEntityMapper mapper) {
        this.queryDslRepository = queryDslRepository;
        this.mapper = mapper;
    }

    /**
     * ID로 카테고리 조회.
     *
     * @param id 카테고리 ID
     * @return 카테고리 Optional
     */
    @Override
    public Optional<Category> findById(CategoryId id) {
        return queryDslRepository.findById(id.value()).map(mapper::toDomain);
    }

    /**
     * ID 목록으로 카테고리 목록 조회.
     *
     * @param ids 카테고리 ID 목록
     * @return 카테고리 목록
     */
    @Override
    public List<Category> findByIds(List<CategoryId> ids) {
        List<Long> idValues = ids.stream().map(CategoryId::value).toList();
        List<CategoryJpaEntity> entities = queryDslRepository.findByIds(idValues);
        return entities.stream().map(mapper::toDomain).toList();
    }

    /**
     * ID 존재 여부 확인.
     *
     * @param id 카테고리 ID
     * @return 존재 여부
     */
    @Override
    public boolean existsById(CategoryId id) {
        return queryDslRepository.existsById(id.value());
    }

    /**
     * 검색 조건으로 카테고리 목록 조회.
     *
     * @param criteria 검색 조건
     * @return 카테고리 목록
     */
    @Override
    public List<Category> findByCriteria(CategorySearchCriteria criteria) {
        List<CategoryJpaEntity> entities = queryDslRepository.findByCriteria(criteria);
        return entities.stream().map(mapper::toDomain).toList();
    }

    /**
     * 검색 조건으로 카테고리 개수 조회.
     *
     * @param criteria 검색 조건
     * @return 카테고리 개수
     */
    @Override
    public long countByCriteria(CategorySearchCriteria criteria) {
        return queryDslRepository.countByCriteria(criteria);
    }

    /**
     * 노출 중인 카테고리 전체 조회.
     *
     * @return 노출 중인 카테고리 목록
     */
    @Override
    public List<Category> findAllDisplayed() {
        List<CategoryJpaEntity> entities = queryDslRepository.findAllDisplayed();
        return entities.stream().map(mapper::toDomain).toList();
    }

    /**
     * 부모 카테고리 ID로 자식 카테고리 목록 조회.
     *
     * @param parentId 부모 카테고리 ID
     * @return 자식 카테고리 목록
     */
    @Override
    public List<Category> findChildrenByParentId(CategoryId parentId) {
        List<CategoryJpaEntity> entities =
                queryDslRepository.findChildrenByParentId(parentId.value());
        return entities.stream().map(mapper::toDomain).toList();
    }

    /**
     * 자식 카테고리 ID로 부모 카테고리 목록 조회 (Recursive CTE).
     *
     * @param childId 자식 카테고리 ID
     * @return 부모 카테고리 목록 (루트부터 순서대로)
     */
    @Override
    public List<Category> findParentsByChildId(CategoryId childId) {
        List<CategoryTreeDto> dtos = queryDslRepository.findAncestorsByChildId(childId.value());
        return dtos.stream().map(mapper::toDomain).toList();
    }
}
