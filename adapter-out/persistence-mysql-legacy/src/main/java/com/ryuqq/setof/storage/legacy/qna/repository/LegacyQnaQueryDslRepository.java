package com.ryuqq.setof.storage.legacy.qna.repository;

import static com.ryuqq.setof.storage.legacy.qna.entity.QLegacyQnaEntity.legacyQnaEntity;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.storage.legacy.qna.entity.LegacyQnaEntity;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * LegacyQnaQueryDslRepository - Q&A 단건 조회 QueryDSL Repository.
 *
 * <p>PER-REP-002: 모든 조회 로직은 QueryDslRepository에서 처리.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Repository
public class LegacyQnaQueryDslRepository {

    private final JPAQueryFactory queryFactory;

    public LegacyQnaQueryDslRepository(
            @Qualifier("legacyJpaQueryFactory") JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    /**
     * Q&A ID로 단건 조회.
     *
     * @param qnaId 레거시 Q&A ID
     * @return Q&A 엔티티 (없으면 empty)
     */
    public Optional<LegacyQnaEntity> findById(long qnaId) {
        LegacyQnaEntity result =
                queryFactory
                        .selectFrom(legacyQnaEntity)
                        .where(legacyQnaEntity.id.eq(qnaId))
                        .fetchFirst();
        return Optional.ofNullable(result);
    }
}
