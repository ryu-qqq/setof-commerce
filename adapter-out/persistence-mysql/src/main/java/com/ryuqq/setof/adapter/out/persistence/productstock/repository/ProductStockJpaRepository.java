package com.ryuqq.setof.adapter.out.persistence.productstock.repository;

import com.ryuqq.setof.adapter.out.persistence.productstock.entity.ProductStockJpaEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * ProductStockJpaRepository - ProductStock JPA Repository
 *
 * <p>Spring Data JPA 기본 CRUD 인터페이스
 *
 * @author development-team
 * @since 1.0.0
 */
public interface ProductStockJpaRepository extends JpaRepository<ProductStockJpaEntity, Long> {

    /**
     * 상품 ID로 재고 조회
     *
     * @param productId 상품 ID
     * @return 재고 Entity
     */
    Optional<ProductStockJpaEntity> findByProductId(Long productId);
}
