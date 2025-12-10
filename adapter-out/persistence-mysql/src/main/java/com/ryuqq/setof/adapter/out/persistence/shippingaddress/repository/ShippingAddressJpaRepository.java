package com.ryuqq.setof.adapter.out.persistence.shippingaddress.repository;

import com.ryuqq.setof.adapter.out.persistence.shippingaddress.entity.ShippingAddressJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * ShippingAddressJpaRepository - ShippingAddress JPA Repository
 *
 * <p>Spring Data JPA Repository로서 ShippingAddress Entity의 기본 CRUD를 담당합니다.
 *
 * <p><strong>제공 메서드 (Command 전용):</strong>
 *
 * <ul>
 *   <li>save(entity): 저장/수정 (INSERT/UPDATE)
 * </ul>
 *
 * <p><strong>Query 작업:</strong>
 *
 * <ul>
 *   <li>모든 Query 작업은 ShippingAddressQueryDslRepository 사용
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
public interface ShippingAddressJpaRepository
        extends JpaRepository<ShippingAddressJpaEntity, Long> {
    // Query Method 추가 금지
    // @Query 추가 금지
}
