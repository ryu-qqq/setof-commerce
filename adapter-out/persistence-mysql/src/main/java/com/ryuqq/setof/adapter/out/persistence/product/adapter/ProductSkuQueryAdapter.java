package com.ryuqq.setof.adapter.out.persistence.product.adapter;

import com.ryuqq.setof.adapter.out.persistence.product.entity.ProductJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.product.mapper.ProductJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.product.repository.ProductJpaRepository;
import com.ryuqq.setof.application.product.port.out.query.ProductSkuQueryPort;
import com.ryuqq.setof.domain.product.aggregate.Product;
import com.ryuqq.setof.domain.product.vo.ProductGroupId;
import com.ryuqq.setof.domain.product.vo.ProductId;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * ProductSkuQueryAdapter - ProductSkuQueryPort 구현체
 *
 * <p>Product(SKU) Aggregate 조회를 위한 Adapter
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class ProductSkuQueryAdapter implements ProductSkuQueryPort {

    private final ProductJpaRepository jpaRepository;
    private final ProductJpaEntityMapper mapper;

    public ProductSkuQueryAdapter(
            ProductJpaRepository jpaRepository, ProductJpaEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Product> findById(ProductId productId) {
        return jpaRepository.findById(productId.value()).map(mapper::toProductDomain);
    }

    @Override
    public List<Product> findByProductGroupId(ProductGroupId productGroupId) {
        List<ProductJpaEntity> entities =
                jpaRepository.findByProductGroupIdAndDeletedAtIsNull(productGroupId.value());
        return entities.stream().map(mapper::toProductDomain).toList();
    }

    @Override
    public List<Product> findByProductGroupIds(List<ProductGroupId> productGroupIds) {
        List<Long> ids = productGroupIds.stream().map(ProductGroupId::value).toList();
        // TODO: 배치 조회 메서드 추가 필요 - 현재는 개별 조회로 대체
        return ids.stream()
                .flatMap(
                        id ->
                                jpaRepository.findByProductGroupIdAndDeletedAtIsNull(id).stream()
                                        .map(mapper::toProductDomain))
                .toList();
    }
}
