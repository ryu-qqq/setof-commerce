package com.ryuqq.setof.adapter.out.persistence.board.repository;

import com.ryuqq.setof.adapter.out.persistence.board.entity.BoardJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * BoardJpaRepository - Board JPA Repository
 *
 * <p>Spring Data JPA Repository로서 Board Entity의 기본 CRUD를 담당합니다.
 *
 * <p>Query Method 추가 금지 - QueryDslRepository 사용
 *
 * @author development-team
 * @since 1.0.0
 */
public interface BoardJpaRepository extends JpaRepository<BoardJpaEntity, Long> {
    // Query Method 추가 금지
    // @Query 추가 금지
}
