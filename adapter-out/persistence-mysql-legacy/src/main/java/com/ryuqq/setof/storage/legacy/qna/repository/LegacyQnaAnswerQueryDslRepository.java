package com.ryuqq.setof.storage.legacy.qna.repository;

import static com.ryuqq.setof.storage.legacy.qna.entity.QLegacyQnaAnswerEntity.legacyQnaAnswerEntity;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.storage.legacy.qna.entity.LegacyQnaAnswerEntity;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * LegacyQnaAnswerQueryDslRepository - Q&A 답변 조회 QueryDSL 레포지토리.
 *
 * <p>PER-REP-002: 모든 조회 로직은 QueryDslRepository에서 처리.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Repository
public class LegacyQnaAnswerQueryDslRepository {

    private final JPAQueryFactory queryFactory;

    public LegacyQnaAnswerQueryDslRepository(
            @Qualifier("legacyJpaQueryFactory") JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    /**
     * Q&A ID로 답변 조회.
     *
     * @param qnaId 레거시 Q&A ID
     * @return 답변 엔티티 (없으면 empty)
     */
    public Optional<LegacyQnaAnswerEntity> findByQnaId(long qnaId) {
        LegacyQnaAnswerEntity result =
                queryFactory
                        .selectFrom(legacyQnaAnswerEntity)
                        .where(legacyQnaAnswerEntity.qnaId.eq(qnaId))
                        .fetchFirst();
        return Optional.ofNullable(result);
    }
}
