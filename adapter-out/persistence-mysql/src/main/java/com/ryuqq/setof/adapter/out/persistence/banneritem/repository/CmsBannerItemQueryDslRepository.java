package com.ryuqq.setof.adapter.out.persistence.banneritem.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.adapter.out.persistence.banneritem.entity.CmsBannerItemJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.banneritem.entity.QCmsBannerItemJpaEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * CmsBannerItemQueryDslRepository - BannerItem QueryDSL Repository
 *
 * <p>QueryDSL 기반 동적 쿼리 처리
 *
 * @author development-team
 * @since 1.0.0
 */
@Repository
public class CmsBannerItemQueryDslRepository {

    private static final String STATUS_ACTIVE = "ACTIVE";

    private final JPAQueryFactory queryFactory;
    private final QCmsBannerItemJpaEntity bannerItem =
            QCmsBannerItemJpaEntity.cmsBannerItemJpaEntity;

    public CmsBannerItemQueryDslRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    /**
     * ID로 배너 아이템 조회
     *
     * @param id 배너 아이템 ID
     * @return 배너 아이템 Entity
     */
    public Optional<CmsBannerItemJpaEntity> findById(Long id) {
        CmsBannerItemJpaEntity result =
                queryFactory.selectFrom(bannerItem).where(bannerItem.id.eq(id)).fetchOne();
        return Optional.ofNullable(result);
    }

    /**
     * Banner ID로 활성 아이템 목록 조회 (노출 순서 정렬)
     *
     * @param bannerId Banner ID
     * @return 활성 BannerItem 목록
     */
    public List<CmsBannerItemJpaEntity> findActiveByBannerId(Long bannerId) {
        return queryFactory
                .selectFrom(bannerItem)
                .where(
                        bannerItem.bannerId.eq(bannerId),
                        bannerItem.status.eq(STATUS_ACTIVE),
                        bannerItem.deletedAt.isNull())
                .orderBy(bannerItem.displayOrder.asc())
                .fetch();
    }

    /**
     * Banner ID로 전체 아이템 목록 조회 (삭제 제외)
     *
     * @param bannerId Banner ID
     * @return BannerItem 목록
     */
    public List<CmsBannerItemJpaEntity> findAllByBannerId(Long bannerId) {
        return queryFactory
                .selectFrom(bannerItem)
                .where(bannerItem.bannerId.eq(bannerId), bannerItem.deletedAt.isNull())
                .orderBy(bannerItem.displayOrder.asc())
                .fetch();
    }

    /**
     * 여러 Banner ID로 활성 아이템 목록 조회
     *
     * @param bannerIds Banner ID 목록
     * @return 활성 BannerItem 목록
     */
    public List<CmsBannerItemJpaEntity> findActiveByBannerIds(List<Long> bannerIds) {
        return queryFactory
                .selectFrom(bannerItem)
                .where(
                        bannerItem.bannerId.in(bannerIds),
                        bannerItem.status.eq(STATUS_ACTIVE),
                        bannerItem.deletedAt.isNull())
                .orderBy(bannerItem.bannerId.asc(), bannerItem.displayOrder.asc())
                .fetch();
    }
}
