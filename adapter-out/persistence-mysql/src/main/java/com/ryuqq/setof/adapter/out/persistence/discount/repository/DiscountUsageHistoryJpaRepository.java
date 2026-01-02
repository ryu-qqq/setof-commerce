package com.ryuqq.setof.adapter.out.persistence.discount.repository;

import com.ryuqq.setof.adapter.out.persistence.discount.entity.DiscountUsageHistoryJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * DiscountUsageHistoryJpaRepository - 할인 사용 이력 JPA Repository
 *
 * <p>Spring Data JPA Repository로서 DiscountUsageHistory Entity의 기본 CRUD를 담당합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
public interface DiscountUsageHistoryJpaRepository
        extends JpaRepository<DiscountUsageHistoryJpaEntity, Long> {
    // Query Method 추가 금지
    // @Query 추가 금지
}
