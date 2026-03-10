package com.ryuqq.setof.adapter.out.persistence.product.adapter;

import com.ryuqq.setof.adapter.out.persistence.product.entity.ProductJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.product.mapper.ProductJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.product.repository.ProductJpaRepository;
import com.ryuqq.setof.application.product.port.out.command.ProductCommandPort;
import com.ryuqq.setof.domain.product.aggregate.Product;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * ProductCommandAdapter - 상품 Command 어댑터.
 *
 * <p>ProductCommandPort를 구현하여 영속성 계층과 연결합니다.
 *
 * <p>PER-ADP-003: CommandAdapter는 JpaRepository만 사용.
 *
 * <p>PER-ADP-002: Adapter에서 @Transactional 금지.
 *
 * <p>PER-ADP-003: Domain 반환 (DTO 반환 금지).
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Component
public class ProductCommandAdapter implements ProductCommandPort {

    private final ProductJpaRepository jpaRepository;
    private final ProductJpaEntityMapper mapper;

    public ProductCommandAdapter(
            ProductJpaRepository jpaRepository, ProductJpaEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    /**
     * 상품 저장.
     *
     * @param product 상품 도메인 객체
     * @return 저장된 상품 ID
     */
    @Override
    public Long persist(Product product) {
        ProductJpaEntity entity = mapper.toEntity(product);
        ProductJpaEntity saved = jpaRepository.save(entity);
        return saved.getId();
    }

    /**
     * 상품 목록 일괄 저장.
     *
     * @param products 상품 도메인 객체 목록
     * @return 저장된 상품 ID 목록
     */
    @Override
    public List<Long> persistAll(List<Product> products) {
        List<ProductJpaEntity> entities = products.stream().map(mapper::toEntity).toList();
        List<ProductJpaEntity> saved = jpaRepository.saveAll(entities);
        return saved.stream().map(ProductJpaEntity::getId).toList();
    }
}
