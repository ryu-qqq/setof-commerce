package com.ryuqq.setof.adapter.out.persistence.productgroupprice.repository;

import com.ryuqq.setof.adapter.out.persistence.productgroupprice.entity.ProductGroupPriceJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * ProductGroupPriceJpaRepository - 상품 그룹 가격 JPA 레포지토리.
 *
 * <p>PER-REP-001: JpaRepository 상속으로 기본 CRUD 제공.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface ProductGroupPriceJpaRepository
        extends JpaRepository<ProductGroupPriceJpaEntity, Long> {}
