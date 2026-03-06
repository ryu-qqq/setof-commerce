package com.ryuqq.setof.storage.legacy.composite.web.qna.repository;

import static com.ryuqq.setof.storage.legacy.qna.entity.QLegacyQnaEntity.legacyQnaEntity;
import static com.ryuqq.setof.storage.legacy.qna.entity.QLegacyQnaProductEntity.legacyQnaProductEntity;
import static com.ryuqq.setof.storage.legacy.user.entity.QLegacyUserEntity.legacyUserEntity;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.domain.legacy.qna.dto.query.LegacyQnaSearchCondition;
import com.ryuqq.setof.storage.legacy.composite.web.qna.condition.LegacyWebQnaCompositeConditionBuilder;
import com.ryuqq.setof.storage.legacy.composite.web.qna.dto.LegacyWebQnaQueryDto;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

/**
 * LegacyWebQnaCompositeQueryDslRepository - 레거시 Web Q&A Composite 조회 Repository.
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
public class LegacyWebQnaCompositeQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private final LegacyWebQnaCompositeConditionBuilder conditionBuilder;

    public LegacyWebQnaCompositeQueryDslRepository(
            @Qualifier("legacyJpaQueryFactory") JPAQueryFactory queryFactory,
            LegacyWebQnaCompositeConditionBuilder conditionBuilder) {
        this.queryFactory = queryFactory;
        this.conditionBuilder = conditionBuilder;
    }

    /**
     * 상품그룹 Q&A ID 목록 조회 (페이징용).
     *
     * <p>fetchProductQnas의 첫 번째 단계: ID 목록만 조회.
     *
     * @param productGroupId 상품그룹 ID
     * @param pageable 페이징 정보
     * @return Q&A ID 목록
     */
    public List<Long> fetchQnaProductIds(Long productGroupId, Pageable pageable) {
        return queryFactory
                .select(legacyQnaEntity.id)
                .from(legacyQnaProductEntity)
                .leftJoin(legacyQnaEntity)
                .on(legacyQnaEntity.id.eq(legacyQnaProductEntity.qnaId))
                .leftJoin(legacyUserEntity)
                .on(legacyUserEntity.id.eq(legacyQnaEntity.userId))
                .where(conditionBuilder.productGroupIdEq(productGroupId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(legacyQnaEntity.id.desc())
                .fetch();
    }

    /**
     * Q&A 목록 조회 (ID 목록 기반).
     *
     * <p>fetchProductQnas의 두 번째 단계: ID 목록으로 상세 조회.
     *
     * @param qnaIds Q&A ID 목록
     * @return Q&A 목록
     */
    public List<LegacyWebQnaQueryDto> fetchQnas(List<Long> qnaIds) {
        if (qnaIds == null || qnaIds.isEmpty()) {
            return List.of();
        }
        return queryFactory
                .select(createProjection())
                .from(legacyQnaEntity)
                .leftJoin(legacyQnaProductEntity)
                .on(legacyQnaProductEntity.qnaId.eq(legacyQnaEntity.id))
                .leftJoin(legacyUserEntity)
                .on(legacyUserEntity.id.eq(legacyQnaEntity.userId))
                .where(conditionBuilder.qnaIdIn(qnaIds))
                .orderBy(legacyQnaEntity.id.desc())
                .fetch();
    }

    /**
     * 상품그룹 Q&A 개수 조회.
     *
     * @param productGroupId 상품그룹 ID
     * @return Q&A 개수
     */
    public long countQnasByProductGroupId(Long productGroupId) {
        Long count =
                queryFactory
                        .select(legacyQnaProductEntity.countDistinct())
                        .from(legacyQnaProductEntity)
                        .where(conditionBuilder.productGroupIdEq(productGroupId))
                        .fetchOne();
        return count != null ? count : 0L;
    }

    /**
     * 내 Q&A ID 목록 조회 (커서 기반 페이징).
     *
     * <p>fetchMyQnas의 첫 번째 단계: ID 목록만 조회.
     *
     * @param condition 검색 조건
     * @param pageable 페이징 정보
     * @return Q&A ID 목록
     */
    public List<Long> fetchMyQnaIds(LegacyQnaSearchCondition condition, Pageable pageable) {
        return queryFactory
                .select(legacyQnaEntity.id)
                .from(legacyQnaEntity)
                .where(
                        conditionBuilder.userIdEq(condition.userId()),
                        conditionBuilder.qnaIdLt(condition.lastDomainId()),
                        conditionBuilder.qnaTypeEq(condition.qnaType()),
                        conditionBuilder.betweenTime(condition.startDate(), condition.endDate()))
                .limit(pageable.getPageSize() + 1) // hasNext 판단용 +1
                .orderBy(legacyQnaEntity.id.desc())
                .fetch();
    }

    /**
     * 내 Q&A 목록 조회 (상품 Q&A).
     *
     * <p>fetchMyQnas의 두 번째 단계: 상품 Q&A 상세 조회.
     *
     * @param qnaIds Q&A ID 목록
     * @return Q&A 목록
     */
    public List<LegacyWebQnaQueryDto> fetchMyProductQnas(List<Long> qnaIds) {
        if (qnaIds == null || qnaIds.isEmpty()) {
            return List.of();
        }
        return queryFactory
                .select(createProjection())
                .from(legacyQnaEntity)
                .leftJoin(legacyQnaProductEntity)
                .on(legacyQnaProductEntity.qnaId.eq(legacyQnaEntity.id))
                .leftJoin(legacyUserEntity)
                .on(legacyUserEntity.id.eq(legacyQnaEntity.userId))
                .where(conditionBuilder.qnaIdIn(qnaIds))
                .orderBy(legacyQnaEntity.id.desc())
                .fetch();
    }

    /**
     * Projections.constructor()로 Projection 생성.
     *
     * <p>@QueryProjection 대신 사용.
     */
    private com.querydsl.core.types.ConstructorExpression<LegacyWebQnaQueryDto> createProjection() {
        return Projections.constructor(
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
                legacyQnaEntity.sellerId,
                legacyQnaProductEntity.productGroupId,
                legacyQnaEntity.insertDate,
                legacyQnaEntity.updateDate);
    }
}
