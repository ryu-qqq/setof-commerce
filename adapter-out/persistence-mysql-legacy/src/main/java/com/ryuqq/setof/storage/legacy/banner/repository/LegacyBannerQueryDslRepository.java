package com.ryuqq.setof.storage.legacy.banner.repository;

import static com.ryuqq.setof.storage.legacy.banner.entity.QLegacyBannerEntity.legacyBannerEntity;
import static com.ryuqq.setof.storage.legacy.banner.entity.QLegacyBannerItemEntity.legacyBannerItemEntity;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.storage.legacy.banner.condition.LegacyBannerConditionBuilder;
import com.ryuqq.setof.storage.legacy.banner.entity.LegacyBannerItemEntity;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * LegacyBannerQueryDslRepository - 레거시 배너 조회 Repository.
 *
 * <p>banner + banner_item 조인 조회 (Composite Query).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Repository
public class LegacyBannerQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private final LegacyBannerConditionBuilder conditionBuilder;

    public LegacyBannerQueryDslRepository(
            @Qualifier("legacyJpaQueryFactory") JPAQueryFactory queryFactory,
            LegacyBannerConditionBuilder conditionBuilder) {
        this.queryFactory = queryFactory;
        this.conditionBuilder = conditionBuilder;
    }

    /**
     * 배너 타입별 전시 중인 배너 아이템 목록 조회.
     *
     * @param bannerType 배너 타입
     * @return 배너 아이템 엔티티 목록
     */
    public List<LegacyBannerItemEntity> fetchBannerItems(String bannerType) {
        return queryFactory
                .select(legacyBannerItemEntity)
                .from(legacyBannerItemEntity)
                .join(legacyBannerEntity)
                .on(legacyBannerItemEntity.bannerId.eq(legacyBannerEntity.id))
                .where(
                        conditionBuilder.bannerTypeEq(bannerType),
                        conditionBuilder.onDisplayBanner(),
                        conditionBuilder.onDisplayBannerItem())
                .orderBy(legacyBannerItemEntity.displayOrder.asc())
                .fetch();
    }
}
