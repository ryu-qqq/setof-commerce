package com.ryuqq.setof.adapter.out.persistence.productdescription.repository;

import com.ryuqq.setof.adapter.out.persistence.productdescription.entity.ProductDescriptionJpaEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * ProductDescriptionJpaRepository - ProductDescription JPA Repository
 *
 * <p>Spring Data JPA 기본 CRUD 인터페이스
 *
 * @author development-team
 * @since 1.0.0
 */
public interface ProductDescriptionJpaRepository
        extends JpaRepository<ProductDescriptionJpaEntity, Long> {

    /**
     * 상품그룹 ID로 상품설명 조회
     *
     * @param productGroupId 상품그룹 ID
     * @return 상품설명 Entity
     */
    Optional<ProductDescriptionJpaEntity> findByProductGroupId(Long productGroupId);
}
