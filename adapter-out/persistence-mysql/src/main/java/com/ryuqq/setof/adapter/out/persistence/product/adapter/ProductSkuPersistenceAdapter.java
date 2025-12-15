package com.ryuqq.setof.adapter.out.persistence.product.adapter;

import com.ryuqq.setof.adapter.out.persistence.product.entity.ProductJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.product.mapper.ProductJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.product.repository.ProductJpaRepository;
import com.ryuqq.setof.application.product.port.out.command.ProductPersistencePort;
import com.ryuqq.setof.domain.product.aggregate.Product;
import com.ryuqq.setof.domain.product.vo.ProductId;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * ProductSkuPersistenceAdapter - ProductPersistencePort 구현체
 *
 * <p>Product(SKU) Aggregate를 영속화하는 Adapter
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class ProductSkuPersistenceAdapter implements ProductPersistencePort {

    private final ProductJpaRepository jpaRepository;
    private final ProductJpaEntityMapper mapper;

    public ProductSkuPersistenceAdapter(
            ProductJpaRepository jpaRepository, ProductJpaEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public ProductId persist(Product product) {
        ProductJpaEntity entity = mapper.toProductEntity(product);
        ProductJpaEntity savedEntity = jpaRepository.save(entity);
        return ProductId.of(savedEntity.getId());
    }

    @Override
    public List<ProductId> persistAll(List<Product> products) {
        List<ProductJpaEntity> entities = products.stream().map(mapper::toProductEntity).toList();
        List<ProductJpaEntity> savedEntities = jpaRepository.saveAll(entities);
        return savedEntities.stream().map(e -> ProductId.of(e.getId())).toList();
    }
}
