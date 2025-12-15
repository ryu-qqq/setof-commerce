package com.ryuqq.setof.adapter.out.persistence.product.repository;

import com.ryuqq.setof.adapter.out.persistence.product.entity.ProductJpaEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * ProductJpaRepository - Product (SKU) JPA Repository
 *
 * <p>Spring Data JPA 기본 CRUD 인터페이스
 *
 * @author development-team
 * @since 1.0.0
 */
public interface ProductJpaRepository extends JpaRepository<ProductJpaEntity, Long> {

    /**
     * 상품그룹 ID로 SKU 목록 조회
     *
     * @param productGroupId 상품그룹 ID
     * @return Product Entity 목록
     */
    List<ProductJpaEntity> findByProductGroupIdAndDeletedAtIsNull(Long productGroupId);

    /**
     * 상품그룹 ID로 SKU 삭제 (Soft Delete용 - 실제 삭제 아님)
     *
     * @param productGroupId 상품그룹 ID
     */
    void deleteByProductGroupId(Long productGroupId);
}
