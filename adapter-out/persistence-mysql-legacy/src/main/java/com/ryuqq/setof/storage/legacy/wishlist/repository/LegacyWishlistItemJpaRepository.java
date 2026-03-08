package com.ryuqq.setof.storage.legacy.wishlist.repository;

import com.ryuqq.setof.storage.legacy.wishlist.entity.LegacyWishlistItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * LegacyWishlistItemJpaRepository - 레거시 찜 항목 JPA 레포지토리.
 *
 * <p>PER-REP-001: JpaRepository는 CUD 작업만 담당.
 *
 * <p>하드 삭제 방식 - deleteAll() 사용.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface LegacyWishlistItemJpaRepository
        extends JpaRepository<LegacyWishlistItemEntity, Long> {}
