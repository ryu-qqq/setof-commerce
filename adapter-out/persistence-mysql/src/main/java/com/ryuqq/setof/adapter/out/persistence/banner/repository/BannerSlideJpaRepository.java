package com.ryuqq.setof.adapter.out.persistence.banner.repository;

import com.ryuqq.setof.adapter.out.persistence.banner.entity.BannerSlideJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * BannerSlideJpaRepository - 배너 슬라이드 JPA 레포지토리.
 *
 * <p>Spring Data JPA를 통한 기본 CRUD를 제공합니다.
 *
 * <p>PER-REP-001: JpaRepository는 save/saveAll만 사용. deleteAll은 슬라이드 교체 패턴에 한해 예외적으로 허용.
 *
 * <p>PER-REP-003: 모든 조회는 QueryDslRepository에서 처리.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface BannerSlideJpaRepository extends JpaRepository<BannerSlideJpaEntity, Long> {

    /**
     * 배너 그룹 ID에 속한 슬라이드 전체 삭제.
     *
     * <p>PER-REP-001: 슬라이드 교체 패턴(delete-then-insert)에 한해 예외적으로 허용.
     *
     * @param bannerGroupId 배너 그룹 ID
     */
    void deleteAllByBannerGroupId(long bannerGroupId);
}
