package com.ryuqq.setof.adapter.out.persistence.product.repository;

import com.ryuqq.setof.adapter.out.persistence.product.entity.ProductJpaEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * ProductJpaRepository - 상품 JPA 레포지토리.
 *
 * <p>PER-REP-001: CommandAdapter에서만 사용.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
public interface ProductJpaRepository extends JpaRepository<ProductJpaEntity, Long> {

    List<ProductJpaEntity> findByProductGroupId(Long productGroupId);
}
