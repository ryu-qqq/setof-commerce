package com.ryuqq.setof.adapter.out.persistence.checkout.repository;

import com.ryuqq.setof.adapter.out.persistence.checkout.entity.CheckoutItemJpaEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * CheckoutItemJpaRepository - CheckoutItem JPA Repository
 *
 * <p>CheckoutItem 엔티티의 기본 CRUD를 담당합니다.
 *
 * <p><strong>표준 JPA 메서드만 사용:</strong>
 *
 * <ul>
 *   <li>save, saveAll, findById, deleteById, deleteAll - 표준 메서드
 *   <li>커스텀 쿼리는 CheckoutItemQueryDslRepository에서 처리
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
public interface CheckoutItemJpaRepository extends JpaRepository<CheckoutItemJpaEntity, UUID> {
    // 표준 JPA 메서드만 사용 (save, saveAll, findById, deleteById, deleteAll)
    // 커스텀 쿼리는 CheckoutItemQueryDslRepository에서 처리
}
