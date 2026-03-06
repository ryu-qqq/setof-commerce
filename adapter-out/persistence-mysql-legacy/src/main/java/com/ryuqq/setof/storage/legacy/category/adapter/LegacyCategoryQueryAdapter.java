package com.ryuqq.setof.storage.legacy.category.adapter;

import com.ryuqq.setof.application.category.port.out.CategoryQueryPort;
import com.ryuqq.setof.domain.category.aggregate.Category;
import com.ryuqq.setof.domain.category.id.CategoryId;
import com.ryuqq.setof.domain.category.query.CategorySearchCriteria;
import com.ryuqq.setof.storage.legacy.category.dto.LegacyCategoryTreeDto;
import com.ryuqq.setof.storage.legacy.category.entity.LegacyCategoryEntity;
import com.ryuqq.setof.storage.legacy.category.mapper.LegacyCategoryEntityMapper;
import com.ryuqq.setof.storage.legacy.category.repository.LegacyCategoryQueryDslRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * LegacyCategoryQueryAdapter - 레거시 카테고리 Query 어댑터.
 *
 * <p>CategoryQueryPort를 구현하여 레거시 DB와 연결합니다.
 *
 * <p>PER-ADP-004: QueryAdapter는 QueryDslRepository만 사용.
 *
 * <p>PER-ADP-002: Adapter에서 @Transactional 금지.
 *
 * <p>PER-ADP-003: Domain 반환 (DTO 반환 금지).
 *
 * <p>활성화 조건: persistence.legacy.category.enabled=true
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
@ConditionalOnProperty(name = "persistence.legacy.category.enabled", havingValue = "true")
public class LegacyCategoryQueryAdapter implements CategoryQueryPort {

    private final LegacyCategoryQueryDslRepository queryDslRepository;
    private final LegacyCategoryEntityMapper mapper;

    public LegacyCategoryQueryAdapter(
            LegacyCategoryQueryDslRepository queryDslRepository,
            LegacyCategoryEntityMapper mapper) {
        this.queryDslRepository = queryDslRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Category> findById(CategoryId id) {
        return queryDslRepository.findById(id.value()).map(mapper::toDomain);
    }

    @Override
    public List<Category> findByIds(List<CategoryId> ids) {
        List<Long> idValues = ids.stream().map(CategoryId::value).toList();
        List<LegacyCategoryEntity> entities = queryDslRepository.findByIds(idValues);
        return entities.stream().map(mapper::toDomain).toList();
    }

    @Override
    public boolean existsById(CategoryId id) {
        return queryDslRepository.existsById(id.value());
    }

    @Override
    public List<Category> findByCriteria(CategorySearchCriteria criteria) {
        List<LegacyCategoryEntity> entities = queryDslRepository.findByCriteria(criteria);
        return entities.stream().map(mapper::toDomain).toList();
    }

    @Override
    public long countByCriteria(CategorySearchCriteria criteria) {
        return queryDslRepository.countByCriteria(criteria);
    }

    @Override
    public List<Category> findAllDisplayed() {
        List<LegacyCategoryEntity> entities = queryDslRepository.findAllDisplayed();
        return entities.stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<Category> findChildrenByParentId(CategoryId parentId) {
        List<LegacyCategoryEntity> entities =
                queryDslRepository.findChildrenByParentId(parentId.value());
        return entities.stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<Category> findParentsByChildId(CategoryId childId) {
        List<LegacyCategoryTreeDto> dtos = queryDslRepository.findParentsByChildId(childId.value());
        return dtos.stream().map(mapper::toDomain).toList();
    }

}
