package com.ryuqq.setof.adapter.out.persistence.component.repository;

import com.ryuqq.setof.adapter.out.persistence.component.entity.ComponentJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * ComponentJpaRepository - Component JPA Repository
 *
 * <p>Spring Data JPA Repository로서 Component Entity의 기본 CRUD를 담당합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
public interface ComponentJpaRepository extends JpaRepository<ComponentJpaEntity, Long> {
    // Query Method 추가 금지
    // @Query 추가 금지
}
