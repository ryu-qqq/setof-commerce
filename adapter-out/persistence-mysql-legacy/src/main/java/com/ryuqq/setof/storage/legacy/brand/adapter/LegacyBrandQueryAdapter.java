package com.ryuqq.setof.storage.legacy.brand.adapter;

import com.ryuqq.setof.application.brand.port.out.BrandQueryPort;
import com.ryuqq.setof.domain.brand.aggregate.Brand;
import com.ryuqq.setof.domain.brand.id.BrandId;
import com.ryuqq.setof.domain.brand.query.BrandSearchCriteria;
import com.ryuqq.setof.storage.legacy.brand.entity.LegacyBrandEntity;
import com.ryuqq.setof.storage.legacy.brand.mapper.LegacyBrandEntityMapper;
import com.ryuqq.setof.storage.legacy.brand.repository.LegacyBrandQueryDslRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * LegacyBrandQueryAdapter - 레거시 브랜드 Query 어댑터.
 *
 * <p>BrandQueryPort를 구현하여 레거시 DB와 연결합니다.
 *
 * <p>PER-ADP-004: QueryAdapter는 QueryDslRepository만 사용.
 *
 * <p>PER-ADP-002: Adapter에서 @Transactional 금지.
 *
 * <p>PER-ADP-003: Domain 반환 (DTO 반환 금지).
 *
 * <p>활성화 조건: persistence.legacy.brand.enabled=true
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
@ConditionalOnProperty(name = "persistence.legacy.brand.enabled", havingValue = "true")
public class LegacyBrandQueryAdapter implements BrandQueryPort {

    private final LegacyBrandQueryDslRepository queryDslRepository;
    private final LegacyBrandEntityMapper mapper;

    public LegacyBrandQueryAdapter(
            LegacyBrandQueryDslRepository queryDslRepository, LegacyBrandEntityMapper mapper) {
        this.queryDslRepository = queryDslRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Brand> findById(BrandId id) {
        return queryDslRepository.findById(id.value()).map(mapper::toDomain);
    }

    @Override
    public List<Brand> findByIds(List<BrandId> ids) {
        List<Long> idValues = ids.stream().map(BrandId::value).toList();
        List<LegacyBrandEntity> entities = queryDslRepository.findByIds(idValues);
        return entities.stream().map(mapper::toDomain).toList();
    }

    @Override
    public boolean existsById(BrandId id) {
        return queryDslRepository.existsById(id.value());
    }

    @Override
    public List<Brand> findByCriteria(BrandSearchCriteria criteria) {
        List<LegacyBrandEntity> entities = queryDslRepository.findByCriteria(criteria);
        return entities.stream().map(mapper::toDomain).toList();
    }

    @Override
    public long countByCriteria(BrandSearchCriteria criteria) {
        return queryDslRepository.countByCriteria(criteria);
    }

    @Override
    public List<Brand> findAllDisplayed() {
        List<LegacyBrandEntity> entities = queryDslRepository.findAllDisplayed();
        return entities.stream().map(mapper::toDomain).toList();
    }
}
