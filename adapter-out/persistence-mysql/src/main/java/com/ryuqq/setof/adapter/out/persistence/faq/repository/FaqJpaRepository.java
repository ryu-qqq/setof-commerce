package com.ryuqq.setof.adapter.out.persistence.faq.repository;

import com.ryuqq.setof.adapter.out.persistence.faq.entity.FaqJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * FaqJpaRepository - FAQ JPA Repository
 *
 * <p>Spring Data JPA Repository로서 FAQ Entity의 기본 CRUD를 담당합니다.
 *
 * <p>Query Method 추가 금지 - QueryDslRepository 사용
 *
 * @author development-team
 * @since 1.0.0
 */
public interface FaqJpaRepository extends JpaRepository<FaqJpaEntity, Long> {
    // Query Method 추가 금지
    // @Query 추가 금지
}
