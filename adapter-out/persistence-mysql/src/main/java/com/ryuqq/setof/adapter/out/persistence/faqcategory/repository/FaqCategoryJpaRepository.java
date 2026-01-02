package com.ryuqq.setof.adapter.out.persistence.faqcategory.repository;

import com.ryuqq.setof.adapter.out.persistence.faqcategory.entity.FaqCategoryJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * FaqCategoryJpaRepository - FAQ 카테고리 Spring Data JPA Repository
 *
 * <p>기본 CRUD 연산만 담당하며, 복잡한 쿼리는 QueryDslRepository에서 처리합니다.
 *
 * <p>Persistence Layer 규칙:
 *
 * <ul>
 *   <li>Query Method 추가 금지 - QueryDSL 사용
 *   <li>기본 save, findById 메서드만 활용
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
public interface FaqCategoryJpaRepository extends JpaRepository<FaqCategoryJpaEntity, Long> {
    // Query Method 추가 금지 - QueryDSL 사용
}
