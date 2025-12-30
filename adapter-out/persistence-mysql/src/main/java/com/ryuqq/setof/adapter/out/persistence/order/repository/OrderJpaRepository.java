package com.ryuqq.setof.adapter.out.persistence.order.repository;

import com.ryuqq.setof.adapter.out.persistence.order.entity.OrderJpaEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * OrderJpaRepository - Order JPA Repository
 *
 * <p>Spring Data JPA Repository로서 Order Entity의 기본 CRUD를 담당합니다.
 *
 * <p><strong>제공 메서드 (표준 JPA 메서드만 사용):</strong>
 *
 * <ul>
 *   <li>save(entity): 저장/수정 (INSERT/UPDATE)
 *   <li>delete(entity): 삭제 (DELETE)
 *   <li>findById(id): ID로 조회
 * </ul>
 *
 * <p><strong>커스텀 쿼리:</strong> OrderQueryDslRepository 사용
 *
 * @author development-team
 * @since 1.0.0
 */
public interface OrderJpaRepository extends JpaRepository<OrderJpaEntity, UUID> {
    // 커스텀 메서드 금지 - QueryDslRepository 사용
}
