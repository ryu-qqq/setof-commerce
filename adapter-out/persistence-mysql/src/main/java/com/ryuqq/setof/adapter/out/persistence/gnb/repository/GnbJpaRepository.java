package com.ryuqq.setof.adapter.out.persistence.gnb.repository;

import com.ryuqq.setof.adapter.out.persistence.gnb.entity.GnbJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * GnbJpaRepository - GNB JPA Repository
 *
 * <p>Spring Data JPA Repository로서 GNB Entity의 기본 CRUD를 담당합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
public interface GnbJpaRepository extends JpaRepository<GnbJpaEntity, Long> {
    // Query Method 추가 금지
    // @Query 추가 금지
}
