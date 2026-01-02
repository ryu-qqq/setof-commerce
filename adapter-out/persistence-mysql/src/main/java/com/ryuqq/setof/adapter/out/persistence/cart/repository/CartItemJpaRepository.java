package com.ryuqq.setof.adapter.out.persistence.cart.repository;

import com.ryuqq.setof.adapter.out.persistence.cart.entity.CartItemJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * CartItemJpaRepository - CartItem JPA Repository
 *
 * <p>JpaRepository 기본 메서드만 사용합니다. 커스텀 조회/삭제는 CartItemQueryDslRepository를 사용하세요.
 *
 * @author development-team
 * @since 1.0.0
 */
@Repository
public interface CartItemJpaRepository extends JpaRepository<CartItemJpaEntity, Long> {
    // 커스텀 메서드 금지 - QueryDslRepository 사용
}
