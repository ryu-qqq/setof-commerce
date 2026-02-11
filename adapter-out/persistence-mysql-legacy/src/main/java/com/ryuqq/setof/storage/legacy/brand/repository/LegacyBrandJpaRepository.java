package com.ryuqq.setof.storage.legacy.brand.repository;

import com.ryuqq.setof.storage.legacy.brand.entity.LegacyBrandEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * LegacyBrandJpaRepository - 레거시 브랜드 JPA 레포지토리.
 *
 * <p>Spring Data JPA가 제공하는 기본 CRUD 메서드를 사용합니다.
 *
 * <p>PER-REP-001: JpaRepository는 CUD 작업만 담당.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface LegacyBrandJpaRepository extends JpaRepository<LegacyBrandEntity, Long> {}
