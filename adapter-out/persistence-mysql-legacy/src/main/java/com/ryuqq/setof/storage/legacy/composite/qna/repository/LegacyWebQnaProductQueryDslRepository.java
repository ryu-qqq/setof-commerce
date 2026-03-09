package com.ryuqq.setof.storage.legacy.composite.qna.repository;

import static com.ryuqq.setof.storage.legacy.qna.entity.QLegacyQnaAnswerEntity.legacyQnaAnswerEntity;
import static com.ryuqq.setof.storage.legacy.qna.entity.QLegacyQnaEntity.legacyQnaEntity;
import static com.ryuqq.setof.storage.legacy.qna.entity.QLegacyQnaImageEntity.legacyQnaImageEntity;
import static com.ryuqq.setof.storage.legacy.qna.entity.QLegacyQnaProductEntity.legacyQnaProductEntity;
import static com.ryuqq.setof.storage.legacy.user.entity.QLegacyUserEntity.legacyUserEntity;

import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.storage.legacy.common.Yn;
import com.ryuqq.setof.storage.legacy.composite.qna.condition.LegacyWebQnaProductConditionBuilder;
import com.ryuqq.setof.storage.legacy.composite.qna.dto.LegacyWebQnaAnswerQueryDto;
import com.ryuqq.setof.storage.legacy.composite.qna.dto.LegacyWebQnaQueryDto;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * LegacyWebQnaProductQueryDslRepository - 상품 Q&A 조회 Repository.
 *
 * <p>PER-REP-002: 모든 조회 로직은 QueryDslRepository에서 처리.
 *
 * <p>PER-REP-004: ConditionBuilder를 사용하여 동적 쿼리 구성.
 *
 * <p>Adapter에서 criteria의 primitive 값을 직접 전달받습니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Repository
public class LegacyWebQnaProductQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private final LegacyWebQnaProductConditionBuilder conditionBuilder;

    public LegacyWebQnaProductQueryDslRepository(
            @Qualifier("legacyJpaQueryFactory") JPAQueryFactory queryFactory,
            LegacyWebQnaProductConditionBuilder conditionBuilder) {
        this.queryFactory = queryFactory;
        this.conditionBuilder = conditionBuilder;
    }

    /**
     * 상품 Q&A 페이징 ID 목록 조회.
     *
     * @param productGroupId 상품그룹 ID
     * @param offset 페이지 오프셋
     * @param limit 페이지 크기
     * @return Q&A ID 목록
     */
    public List<Long> fetchProductQnaIds(long productGroupId, long offset, int limit) {
        return queryFactory
                .select(legacyQnaEntity.id)
                .from(legacyQnaProductEntity)
                .leftJoin(legacyQnaEntity)
                .on(legacyQnaEntity.id.eq(legacyQnaProductEntity.qnaId))
                .leftJoin(legacyUserEntity)
                .on(legacyUserEntity.id.eq(legacyQnaEntity.userId))
                .where(
                        conditionBuilder.productGroupIdEq(productGroupId),
                        conditionBuilder.notDeleted())
                .orderBy(legacyQnaEntity.id.desc())
                .offset(offset)
                .limit(limit)
                .fetch();
    }

    /**
     * 상품 Q&A 상세 목록 조회 (GroupBy + 답변 집계).
     *
     * @param qnaIds Q&A ID 목록
     * @return Q&A 상세 목록 (답변 포함)
     */
    public List<LegacyWebQnaQueryDto> fetchProductQnasByIds(List<Long> qnaIds) {
        if (qnaIds == null || qnaIds.isEmpty()) {
            return List.of();
        }

        return queryFactory
                .from(legacyQnaEntity)
                .leftJoin(legacyQnaAnswerEntity)
                .on(legacyQnaAnswerEntity.qnaId.eq(legacyQnaEntity.id))
                .leftJoin(legacyQnaImageEntity)
                .on(
                        legacyQnaImageEntity.qnaId.eq(legacyQnaEntity.id),
                        legacyQnaImageEntity.qnaAnswerId.eq(legacyQnaAnswerEntity.id),
                        legacyQnaImageEntity.deleteYn.eq(Yn.N))
                .leftJoin(legacyUserEntity)
                .on(legacyUserEntity.id.eq(legacyQnaEntity.userId))
                .where(conditionBuilder.qnaIdIn(qnaIds), conditionBuilder.notDeleted())
                .orderBy(legacyQnaEntity.id.desc())
                .transform(
                        GroupBy.groupBy(legacyQnaEntity.id)
                                .list(
                                        Projections.constructor(
                                                LegacyWebQnaQueryDto.class,
                                                legacyQnaEntity.id,
                                                legacyQnaEntity.qnaContents.title,
                                                legacyQnaEntity.qnaContents.content,
                                                legacyQnaEntity.privateYn.stringValue(),
                                                legacyQnaEntity.qnaStatus,
                                                legacyQnaEntity.qnaType,
                                                legacyQnaEntity.qnaDetailType,
                                                legacyQnaEntity.userType,
                                                legacyQnaEntity.userId,
                                                legacyUserEntity.name,
                                                legacyQnaEntity.insertDate,
                                                legacyQnaEntity.updateDate,
                                                GroupBy.set(
                                                        Projections.constructor(
                                                                LegacyWebQnaAnswerQueryDto.class,
                                                                legacyQnaAnswerEntity.id,
                                                                legacyQnaAnswerEntity.qnaParentId,
                                                                legacyQnaAnswerEntity.qnaWriterType,
                                                                legacyQnaAnswerEntity
                                                                        .qnaContents
                                                                        .title,
                                                                legacyQnaAnswerEntity
                                                                        .qnaContents
                                                                        .content,
                                                                legacyQnaAnswerEntity.insertDate,
                                                                legacyQnaAnswerEntity
                                                                        .updateDate)))));
    }

    /**
     * 상품 Q&A 전체 건수 조회.
     *
     * @param productGroupId 상품그룹 ID
     * @return 전체 Q&A 건수
     */
    public long countProductQnas(long productGroupId) {
        Long count =
                queryFactory
                        .select(legacyQnaProductEntity.count())
                        .from(legacyQnaProductEntity)
                        .join(legacyQnaEntity)
                        .on(legacyQnaEntity.id.eq(legacyQnaProductEntity.qnaId))
                        .where(
                                conditionBuilder.productGroupIdEq(productGroupId),
                                conditionBuilder.notDeleted())
                        .fetchOne();
        return count != null ? count : 0L;
    }
}
