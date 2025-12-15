package com.ryuqq.setof.adapter.out.persistence.product.repository;

import com.ryuqq.setof.adapter.out.persistence.product.entity.ProductGroupJpaEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * ProductGroupJpaRepository - ProductGroup JPA Repository
 *
 * <p>Spring Data JPA 기본 CRUD 인터페이스
 *
 * @author development-team
 * @since 1.0.0
 */
public interface ProductGroupJpaRepository extends JpaRepository<ProductGroupJpaEntity, Long> {

    /**
     * ID로 삭제되지 않은 상품그룹 조회
     *
     * @param id 상품그룹 ID
     * @return 상품그룹 Entity
     */
    Optional<ProductGroupJpaEntity> findByIdAndDeletedAtIsNull(Long id);

    /**
     * ID로 존재 여부 확인 (삭제되지 않은 것만)
     *
     * @param id 상품그룹 ID
     * @return 존재 여부
     */
    boolean existsByIdAndDeletedAtIsNull(Long id);
}
