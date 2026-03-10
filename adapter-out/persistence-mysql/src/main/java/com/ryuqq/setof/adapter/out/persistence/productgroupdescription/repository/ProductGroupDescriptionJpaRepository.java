package com.ryuqq.setof.adapter.out.persistence.productgroupdescription.repository;

import com.ryuqq.setof.adapter.out.persistence.productgroupdescription.entity.ProductGroupDescriptionJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * ProductGroupDescriptionJpaRepository - 상품그룹 상세설명 JPA 레포지토리.
 *
 * <p>PER-REP-001: JPA 레포지토리는 Command 전용.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
public interface ProductGroupDescriptionJpaRepository
        extends JpaRepository<ProductGroupDescriptionJpaEntity, Long> {}
