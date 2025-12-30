package com.ryuqq.setof.adapter.out.persistence.cart.repository;

import com.ryuqq.setof.adapter.out.persistence.cart.entity.CartJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * CartJpaRepository - Cart JPA Repository
 *
 * <p>JpaRepository 기본 메서드만 사용합니다. 커스텀 조회는 CartQueryDslRepository를 사용하세요.
 *
 * @author development-team
 * @since 1.0.0
 */
@Repository
public interface CartJpaRepository extends JpaRepository<CartJpaEntity, Long> {
    // 커스텀 메서드 금지 - QueryDslRepository 사용
}
