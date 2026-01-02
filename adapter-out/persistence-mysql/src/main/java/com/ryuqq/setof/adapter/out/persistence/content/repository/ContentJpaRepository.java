package com.ryuqq.setof.adapter.out.persistence.content.repository;

import com.ryuqq.setof.adapter.out.persistence.content.entity.ContentJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * ContentJpaRepository - Content JPA Repository
 *
 * <p>Spring Data JPA Repository로서 Content Entity의 기본 CRUD를 담당합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
public interface ContentJpaRepository extends JpaRepository<ContentJpaEntity, Long> {
    // Query Method 추가 금지
    // @Query 추가 금지
}
