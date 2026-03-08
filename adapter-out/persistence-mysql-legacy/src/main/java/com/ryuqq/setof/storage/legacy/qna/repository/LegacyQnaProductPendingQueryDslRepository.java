package com.ryuqq.setof.storage.legacy.qna.repository;

import static com.ryuqq.setof.storage.legacy.qna.entity.QLegacyQnaEntity.legacyQnaEntity;
import static com.ryuqq.setof.storage.legacy.qna.entity.QLegacyQnaProductEntity.legacyQnaProductEntity;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * LegacyQnaProductPendingQueryDslRepository - 상품 Q&A 미답변 질문 조회 QueryDSL 레포지토리.
 *
 * <p>PER-REP-002: 모든 조회 로직은 QueryDslRepository에서 처리.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Repository
public class LegacyQnaProductPendingQueryDslRepository {

    private final JPAQueryFactory queryFactory;

    public LegacyQnaProductPendingQueryDslRepository(
            @Qualifier("legacyJpaQueryFactory") JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    /**
     * 사용자의 특정 상품에 대한 PENDING 상태 Q&A 존재 여부 확인.
     *
     * @param userId 사용자 ID
     * @param productGroupId 상품 그룹 ID
     * @return PENDING 상태 질문이 존재하면 true
     */
    public boolean existsPendingProductQna(long userId, long productGroupId) {
        Integer result =
                queryFactory
                        .selectOne()
                        .from(legacyQnaEntity)
                        .innerJoin(legacyQnaProductEntity)
                        .on(legacyQnaEntity.id.eq(legacyQnaProductEntity.qnaId))
                        .where(
                                legacyQnaEntity.userId.eq(userId),
                                legacyQnaProductEntity.productGroupId.eq(productGroupId),
                                legacyQnaEntity.qnaStatus.eq("PENDING"),
                                legacyQnaEntity.deleteYn.eq(
                                        com.ryuqq.setof.storage.legacy.qna.entity.LegacyQnaEntity.Yn
                                                .N))
                        .fetchFirst();
        return result != null;
    }
}
