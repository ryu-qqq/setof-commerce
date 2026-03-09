package com.ryuqq.setof.storage.legacy.commoncode.repository;

import static com.ryuqq.setof.storage.legacy.commoncode.entity.QLegacyCommonCodeEntity.legacyCommonCodeEntity;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.storage.legacy.commoncode.entity.LegacyCommonCodeEntity;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * LegacyCommonCodeQueryDslRepository - 레거시 공통 코드 QueryDSL 레포지토리.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Repository
public class LegacyCommonCodeQueryDslRepository {

    private final JPAQueryFactory queryFactory;

    public LegacyCommonCodeQueryDslRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    /**
     * 코드 그룹 ID로 활성 공통 코드 목록 조회.
     *
     * @param codeGroupId 코드 그룹 ID
     * @return 공통 코드 엔티티 목록 (displayOrder 순 정렬)
     */
    public List<LegacyCommonCodeEntity> findByCodeGroupId(long codeGroupId) {
        return queryFactory
                .selectFrom(legacyCommonCodeEntity)
                .where(
                        legacyCommonCodeEntity.codeGroupId.eq(codeGroupId),
                        legacyCommonCodeEntity.deleteYn.eq("N"))
                .orderBy(legacyCommonCodeEntity.displayOrder.asc())
                .fetch();
    }
}
