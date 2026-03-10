package com.ryuqq.setof.adapter.out.persistence.product.repository;

import com.ryuqq.setof.adapter.out.persistence.product.entity.ProductOptionMappingJpaEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * ProductOptionMappingJpaRepository - 상품 옵션 매핑 JPA 레포지토리.
 *
 * <p>PER-REP-001: CommandAdapter에서만 사용.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
public interface ProductOptionMappingJpaRepository
        extends JpaRepository<ProductOptionMappingJpaEntity, Long> {

    void deleteByProductId(Long productId);

    void deleteByProductIdIn(List<Long> productIds);
}
