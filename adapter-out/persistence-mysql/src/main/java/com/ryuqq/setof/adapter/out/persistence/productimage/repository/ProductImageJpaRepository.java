package com.ryuqq.setof.adapter.out.persistence.productimage.repository;

import com.ryuqq.setof.adapter.out.persistence.productimage.entity.ProductImageJpaEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * ProductImageJpaRepository - ProductImage JPA Repository
 *
 * <p>Spring Data JPA 기본 CRUD 인터페이스
 *
 * @author development-team
 * @since 1.0.0
 */
public interface ProductImageJpaRepository extends JpaRepository<ProductImageJpaEntity, Long> {

    /**
     * 상품그룹 ID로 이미지 목록 조회
     *
     * @param productGroupId 상품그룹 ID
     * @return 이미지 Entity 목록
     */
    List<ProductImageJpaEntity> findByProductGroupId(Long productGroupId);

    /**
     * 상품그룹 ID로 이미지 삭제
     *
     * @param productGroupId 상품그룹 ID
     */
    void deleteByProductGroupId(Long productGroupId);
}
