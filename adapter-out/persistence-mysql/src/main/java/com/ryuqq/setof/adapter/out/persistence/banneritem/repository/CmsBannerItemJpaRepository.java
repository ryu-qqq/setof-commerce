package com.ryuqq.setof.adapter.out.persistence.banneritem.repository;

import com.ryuqq.setof.adapter.out.persistence.banneritem.entity.CmsBannerItemJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * CmsBannerItemJpaRepository - BannerItem JPA Repository
 *
 * <p>cms_banner_items 테이블의 기본 CRUD 작업을 담당합니다.
 *
 * <p><strong>주의:</strong> 커스텀 메서드 금지 - QueryDslRepository 사용
 *
 * @author development-team
 * @since 1.0.0
 */
public interface CmsBannerItemJpaRepository extends JpaRepository<CmsBannerItemJpaEntity, Long> {
    // 커스텀 메서드 금지 - QueryDslRepository 사용
}
