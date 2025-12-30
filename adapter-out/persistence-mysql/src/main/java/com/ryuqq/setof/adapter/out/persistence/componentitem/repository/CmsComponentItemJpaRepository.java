package com.ryuqq.setof.adapter.out.persistence.componentitem.repository;

import com.ryuqq.setof.adapter.out.persistence.componentitem.entity.CmsComponentItemJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * CmsComponentItemJpaRepository - ComponentItem JPA Repository
 *
 * <p>cms_component_items 테이블의 기본 CRUD 작업을 담당합니다.
 *
 * <p><strong>주의:</strong> 커스텀 메서드 금지 - QueryDslRepository 사용
 *
 * @author development-team
 * @since 1.0.0
 */
public interface CmsComponentItemJpaRepository
        extends JpaRepository<CmsComponentItemJpaEntity, Long> {
    // 커스텀 메서드 금지 - QueryDslRepository 사용
}
