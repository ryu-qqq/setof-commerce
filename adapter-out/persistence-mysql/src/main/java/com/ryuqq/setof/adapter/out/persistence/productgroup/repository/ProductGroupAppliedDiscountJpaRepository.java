package com.ryuqq.setof.adapter.out.persistence.productgroup.repository;

import com.ryuqq.setof.adapter.out.persistence.productgroup.entity.ProductGroupAppliedDiscountJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * ProductGroupAppliedDiscountJpaRepository - 적용 할인 내역 JPA 리포지토리.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface ProductGroupAppliedDiscountJpaRepository
        extends JpaRepository<ProductGroupAppliedDiscountJpaEntity, Long> {

    @Modifying
    @Query(
            "DELETE FROM ProductGroupAppliedDiscountJpaEntity e WHERE e.productGroupId ="
                    + " :productGroupId")
    void deleteByProductGroupId(Long productGroupId);
}
