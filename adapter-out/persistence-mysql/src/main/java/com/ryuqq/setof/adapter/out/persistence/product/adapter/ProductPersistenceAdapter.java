package com.ryuqq.setof.adapter.out.persistence.product.adapter;

import com.ryuqq.setof.adapter.out.persistence.product.entity.ProductGroupJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.product.mapper.ProductJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.product.repository.ProductGroupJpaRepository;
import com.ryuqq.setof.application.product.port.out.command.ProductGroupPersistencePort;
import com.ryuqq.setof.domain.product.aggregate.ProductGroup;
import com.ryuqq.setof.domain.product.vo.ProductGroupId;
import org.springframework.stereotype.Component;

/**
 * ProductPersistenceAdapter - ProductGroupPersistencePort 구현체
 *
 * <p>ProductGroup Aggregate를 영속화하는 Adapter
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class ProductPersistenceAdapter implements ProductGroupPersistencePort {

    private final ProductGroupJpaRepository jpaRepository;
    private final ProductJpaEntityMapper mapper;

    public ProductPersistenceAdapter(
            ProductGroupJpaRepository jpaRepository, ProductJpaEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public ProductGroupId persist(ProductGroup productGroup) {
        ProductGroupJpaEntity entity = mapper.toEntity(productGroup);
        ProductGroupJpaEntity savedEntity = jpaRepository.save(entity);
        return ProductGroupId.of(savedEntity.getId());
    }
}
