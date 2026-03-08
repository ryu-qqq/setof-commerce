package com.ryuqq.setof.storage.legacy.gnb.repository;

import static com.ryuqq.setof.storage.legacy.gnb.entity.QLegacyGnbEntity.legacyGnbEntity;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.storage.legacy.gnb.condition.LegacyGnbConditionBuilder;
import com.ryuqq.setof.storage.legacy.gnb.entity.LegacyGnbEntity;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * LegacyGnbQueryDslRepository - 레거시 GNB 조회 Repository.
 *
 * <p>PER-REP-002: 모든 조회 로직은 QueryDslRepository에서 처리.
 *
 * <p>PER-REP-004: ConditionBuilder를 사용하여 동적 쿼리 구성.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Repository
public class LegacyGnbQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private final LegacyGnbConditionBuilder conditionBuilder;

    public LegacyGnbQueryDslRepository(
            @Qualifier("legacyJpaQueryFactory") JPAQueryFactory queryFactory,
            LegacyGnbConditionBuilder conditionBuilder) {
        this.queryFactory = queryFactory;
        this.conditionBuilder = conditionBuilder;
    }

    /**
     * 전시 중인 GNB 목록 조회.
     *
     * @return GNB 엔티티 목록
     */
    public List<LegacyGnbEntity> fetchGnbs() {
        return queryFactory
                .selectFrom(legacyGnbEntity)
                .where(conditionBuilder.onDisplayGnb())
                .distinct()
                .orderBy(legacyGnbEntity.displayOrder.asc())
                .fetch();
    }
}
