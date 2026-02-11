package com.ryuqq.setof.storage.legacy.composite.web.banner.repository;

import static com.ryuqq.setof.storage.legacy.banner.entity.QLegacyBannerEntity.legacyBannerEntity;
import static com.ryuqq.setof.storage.legacy.banner.entity.QLegacyBannerItemEntity.legacyBannerItemEntity;

import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.domain.legacy.banner.dto.query.LegacyBannerSearchCondition;
import com.ryuqq.setof.storage.legacy.composite.web.banner.condition.LegacyWebBannerCompositeConditionBuilder;
import com.ryuqq.setof.storage.legacy.composite.web.banner.dto.LegacyWebBannerItemQueryDto;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * LegacyWebBannerCompositeQueryDslRepository - 레거시 Web 배너 Composite 조회 Repository.
 *
 * <p>PER-REP-002: 모든 조회 로직은 QueryDslRepository에서 처리.
 *
 * <p>PER-REP-004: ConditionBuilder를 사용하여 동적 쿼리 구성.
 *
 * <p>Projections.constructor() 사용 (@QueryProjection 금지).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Repository
public class LegacyWebBannerCompositeQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private final LegacyWebBannerCompositeConditionBuilder conditionBuilder;

    public LegacyWebBannerCompositeQueryDslRepository(
            @Qualifier("legacyJpaQueryFactory") JPAQueryFactory queryFactory,
            LegacyWebBannerCompositeConditionBuilder conditionBuilder) {
        this.queryFactory = queryFactory;
        this.conditionBuilder = conditionBuilder;
    }

    /**
     * 배너 타입별 배너 아이템 목록 조회.
     *
     * <p>Banner + BannerItem 조인하여 전시 중인 배너 아이템 조회.
     *
     * @param condition 검색 조건
     * @return 배너 아이템 목록
     */
    public List<LegacyWebBannerItemQueryDto> fetchBannerItems(
            LegacyBannerSearchCondition condition) {
        return queryFactory
                .select(createBannerItemProjection())
                .from(legacyBannerItemEntity)
                .join(legacyBannerEntity)
                .on(legacyBannerEntity.id.eq(legacyBannerItemEntity.bannerId))
                .where(
                        conditionBuilder.bannerTypeEq(condition.bannerType()),
                        conditionBuilder.onDisplayBanner(),
                        conditionBuilder.onDisplayBannerItem())
                .orderBy(legacyBannerItemEntity.displayOrder.asc())
                .fetch();
    }

    /** Projections.constructor()로 BannerItem Projection 생성. */
    private ConstructorExpression<LegacyWebBannerItemQueryDto> createBannerItemProjection() {
        return Projections.constructor(
                LegacyWebBannerItemQueryDto.class,
                legacyBannerItemEntity.id,
                legacyBannerItemEntity.title,
                legacyBannerItemEntity.imageUrl,
                legacyBannerItemEntity.linkUrl);
    }
}
