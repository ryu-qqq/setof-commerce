package com.ryuqq.setof.storage.legacy.composite.web.gnb.repository;

import static com.ryuqq.setof.storage.legacy.gnb.entity.QLegacyGnbEntity.legacyGnbEntity;

import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.storage.legacy.composite.web.gnb.condition.LegacyWebGnbCompositeConditionBuilder;
import com.ryuqq.setof.storage.legacy.composite.web.gnb.dto.LegacyWebGnbQueryDto;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * LegacyWebGnbCompositeQueryDslRepository - 레거시 Web GNB Composite 조회 Repository.
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
public class LegacyWebGnbCompositeQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private final LegacyWebGnbCompositeConditionBuilder conditionBuilder;

    public LegacyWebGnbCompositeQueryDslRepository(
            @Qualifier("legacyJpaQueryFactory") JPAQueryFactory queryFactory,
            LegacyWebGnbCompositeConditionBuilder conditionBuilder) {
        this.queryFactory = queryFactory;
        this.conditionBuilder = conditionBuilder;
    }

    /**
     * 전시 중인 GNB 목록 조회.
     *
     * @return GNB 목록
     */
    public List<LegacyWebGnbQueryDto> fetchGnbs() {
        return queryFactory
                .select(createGnbProjection())
                .from(legacyGnbEntity)
                .where(conditionBuilder.onDisplayGnb())
                .distinct()
                .orderBy(legacyGnbEntity.displayOrder.asc())
                .fetch();
    }

    /** Projections.constructor()로 GNB Projection 생성. */
    private ConstructorExpression<LegacyWebGnbQueryDto> createGnbProjection() {
        return Projections.constructor(
                LegacyWebGnbQueryDto.class,
                legacyGnbEntity.id,
                legacyGnbEntity.title,
                legacyGnbEntity.linkUrl);
    }
}
