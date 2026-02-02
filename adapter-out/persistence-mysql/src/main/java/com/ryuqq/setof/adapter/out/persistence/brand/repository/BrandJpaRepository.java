package com.ryuqq.setof.adapter.out.persistence.brand.repository;

import com.ryuqq.setof.adapter.out.persistence.brand.entity.BrandJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * BrandJpaRepository - 브랜드 JPA 레포지토리.
 *
 * <p>Spring Data JPA를 통한 기본 CRUD를 제공합니다.
 *
 * <p>PER-REP-001: JpaRepository는 save/saveAll만 사용.
 *
 * <p>PER-REP-003: 모든 조회는 QueryDslRepository에서 처리.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
public interface BrandJpaRepository extends JpaRepository<BrandJpaEntity, Long> {}
