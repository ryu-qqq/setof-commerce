package com.ryuqq.setof.adapter.out.persistence.banner.repository;

import com.ryuqq.setof.adapter.out.persistence.banner.entity.BannerJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * BannerJpaRepository - Banner JPA Repository
 *
 * <p>Spring Data JPA Repository로서 Banner Entity의 기본 CRUD를 담당합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
public interface BannerJpaRepository extends JpaRepository<BannerJpaEntity, Long> {
    // Query Method 추가 금지
    // @Query 추가 금지
}
