package com.ryuqq.setof.adapter.out.persistence.productgroup.repository;

import com.ryuqq.setof.adapter.out.persistence.productgroup.entity.SellerOptionGroupJpaEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * SellerOptionGroupJpaRepository - 셀러 옵션 그룹 JPA 레포지토리.
 *
 * <p>PER-REP-001: CommandAdapter는 JpaRepository만 사용.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
public interface SellerOptionGroupJpaRepository
        extends JpaRepository<SellerOptionGroupJpaEntity, Long> {

    List<SellerOptionGroupJpaEntity> findByProductGroupId(Long productGroupId);
}
