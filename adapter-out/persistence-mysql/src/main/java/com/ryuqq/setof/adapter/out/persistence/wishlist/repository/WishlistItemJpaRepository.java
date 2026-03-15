package com.ryuqq.setof.adapter.out.persistence.wishlist.repository;

import com.ryuqq.setof.adapter.out.persistence.wishlist.entity.WishlistItemJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * WishlistItemJpaRepository - 찜 항목 JPA 레포지토리.
 *
 * <p>Spring Data JPA를 통한 기본 CRUD를 제공합니다.
 *
 * <p>PER-REP-001: JpaRepository는 save/saveAll만 사용.
 *
 * <p>PER-REP-003: 모든 조회는 QueryDslRepository에서 처리.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface WishlistItemJpaRepository extends JpaRepository<WishlistItemJpaEntity, Long> {}
