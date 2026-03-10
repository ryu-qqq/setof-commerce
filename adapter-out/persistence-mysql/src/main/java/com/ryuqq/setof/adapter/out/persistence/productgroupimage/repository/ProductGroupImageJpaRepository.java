package com.ryuqq.setof.adapter.out.persistence.productgroupimage.repository;

import com.ryuqq.setof.adapter.out.persistence.productgroupimage.entity.ProductGroupImageJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * ProductGroupImageJpaRepository - 상품그룹 이미지 JPA Repository.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface ProductGroupImageJpaRepository
        extends JpaRepository<ProductGroupImageJpaEntity, Long> {

    void deleteByProductGroupId(Long productGroupId);
}
