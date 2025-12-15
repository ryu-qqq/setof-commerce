package com.ryuqq.setof.adapter.out.persistence.product.adapter;

import com.ryuqq.setof.adapter.out.persistence.product.entity.ProductGroupJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.product.mapper.ProductJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.product.repository.ProductGroupJpaRepository;
import com.ryuqq.setof.adapter.out.persistence.product.repository.ProductQueryDslRepository;
import com.ryuqq.setof.application.product.port.out.query.ProductGroupQueryPort;
import com.ryuqq.setof.domain.product.aggregate.ProductGroup;
import com.ryuqq.setof.domain.product.vo.ProductGroupId;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * ProductQueryAdapter - ProductGroupQueryPort 구현체
 *
 * <p>ProductGroup Aggregate를 조회하는 Adapter
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class ProductQueryAdapter implements ProductGroupQueryPort {

    private final ProductGroupJpaRepository jpaRepository;
    private final ProductQueryDslRepository queryDslRepository;
    private final ProductJpaEntityMapper mapper;

    public ProductQueryAdapter(
            ProductGroupJpaRepository jpaRepository,
            ProductQueryDslRepository queryDslRepository,
            ProductJpaEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.queryDslRepository = queryDslRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<ProductGroup> findById(ProductGroupId productGroupId) {
        return jpaRepository
                .findByIdAndDeletedAtIsNull(productGroupId.value())
                .map(mapper::toDomain);
    }

    @Override
    public List<ProductGroup> findByConditions(
            Long sellerId,
            Long categoryId,
            Long brandId,
            String name,
            String status,
            int offset,
            int limit) {
        List<ProductGroupJpaEntity> entities =
                queryDslRepository.findByConditions(
                        sellerId, categoryId, brandId, name, status, offset, limit);
        return entities.stream().map(mapper::toDomain).toList();
    }

    @Override
    public long countByConditions(
            Long sellerId, Long categoryId, Long brandId, String name, String status) {
        return queryDslRepository.countByConditions(sellerId, categoryId, brandId, name, status);
    }

    @Override
    public boolean existsById(ProductGroupId productGroupId) {
        return jpaRepository.existsByIdAndDeletedAtIsNull(productGroupId.value());
    }
}
