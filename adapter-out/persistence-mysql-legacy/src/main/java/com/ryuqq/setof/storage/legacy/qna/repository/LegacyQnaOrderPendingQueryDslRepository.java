package com.ryuqq.setof.storage.legacy.qna.repository;

import static com.ryuqq.setof.storage.legacy.qna.entity.QLegacyQnaEntity.legacyQnaEntity;
import static com.ryuqq.setof.storage.legacy.qna.entity.QLegacyQnaOrderEntity.legacyQnaOrderEntity;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.storage.legacy.common.Yn;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * LegacyQnaOrderPendingQueryDslRepository - 주문 Q&A 미답변 질문 조회 QueryDSL 레포지토리.
 *
 * <p>PER-REP-002: 모든 조회 로직은 QueryDslRepository에서 처리.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Repository
public class LegacyQnaOrderPendingQueryDslRepository {

    private final JPAQueryFactory queryFactory;

    public LegacyQnaOrderPendingQueryDslRepository(
            @Qualifier("legacyJpaQueryFactory") JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    /**
     * 사용자의 특정 주문에 대한 PENDING 상태 Q&A 존재 여부 확인.
     *
     * @param userId 사용자 ID
     * @param legacyOrderId 레거시 주문 ID
     * @return PENDING 상태 질문이 존재하면 true
     */
    public boolean existsPendingOrderQna(long userId, long legacyOrderId) {
        Integer result =
                queryFactory
                        .selectOne()
                        .from(legacyQnaEntity)
                        .innerJoin(legacyQnaOrderEntity)
                        .on(legacyQnaEntity.id.eq(legacyQnaOrderEntity.qnaId))
                        .where(
                                legacyQnaEntity.userId.eq(userId),
                                legacyQnaOrderEntity.orderId.eq(legacyOrderId),
                                legacyQnaEntity.qnaStatus.eq("PENDING"),
                                legacyQnaOrderEntity.deleteYn.eq(Yn.N))
                        .fetchFirst();
        return result != null;
    }
}
