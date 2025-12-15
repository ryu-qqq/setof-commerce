package com.ryuqq.setof.adapter.out.persistence.productdescription.repository;

import com.ryuqq.setof.adapter.out.persistence.productdescription.entity.ProductDescriptionImageJpaEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * ProductDescriptionImageJpaRepository - ProductDescriptionImage JPA Repository
 *
 * <p>Spring Data JPA 기본 CRUD 인터페이스
 *
 * @author development-team
 * @since 1.0.0
 */
public interface ProductDescriptionImageJpaRepository
        extends JpaRepository<ProductDescriptionImageJpaEntity, Long> {

    /**
     * 상품설명 ID로 이미지 목록 조회
     *
     * @param productDescriptionId 상품설명 ID
     * @return 이미지 Entity 목록
     */
    List<ProductDescriptionImageJpaEntity> findByProductDescriptionIdOrderByDisplayOrderAsc(
            Long productDescriptionId);

    /**
     * 상품설명 ID로 이미지 삭제
     *
     * @param productDescriptionId 상품설명 ID
     */
    void deleteByProductDescriptionId(Long productDescriptionId);
}
