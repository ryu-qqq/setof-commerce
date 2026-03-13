package com.ryuqq.setof.adapter.out.persistence.navigation.repository;

import com.ryuqq.setof.adapter.out.persistence.navigation.entity.NavigationMenuJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * NavigationMenuJpaRepository - 네비게이션 메뉴 JPA 레포지토리.
 *
 * <p>Spring Data JPA를 통한 기본 CRUD를 제공합니다.
 *
 * <p>PER-REP-002: 모든 조회는 QueryDslRepository에서 처리.
 *
 * <p>PER-REP-004: JpaRepository는 save/saveAll만 사용.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface NavigationMenuJpaRepository extends JpaRepository<NavigationMenuJpaEntity, Long> {}
