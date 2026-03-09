package com.ryuqq.setof.storage.legacy.composite.qna.repository;

import static com.ryuqq.setof.storage.legacy.brand.entity.QLegacyBrandEntity.legacyBrandEntity;
import static com.ryuqq.setof.storage.legacy.productgroup.entity.QLegacyProductGroupEntity.legacyProductGroupEntity;
import static com.ryuqq.setof.storage.legacy.productgroup.entity.QLegacyProductGroupImageEntity.legacyProductGroupImageEntity;
import static com.ryuqq.setof.storage.legacy.qna.entity.QLegacyQnaAnswerEntity.legacyQnaAnswerEntity;
import static com.ryuqq.setof.storage.legacy.qna.entity.QLegacyQnaEntity.legacyQnaEntity;
import static com.ryuqq.setof.storage.legacy.qna.entity.QLegacyQnaProductEntity.legacyQnaProductEntity;
import static com.ryuqq.setof.storage.legacy.seller.entity.QLegacySellerEntity.legacySellerEntity;
import static com.ryuqq.setof.storage.legacy.user.entity.QLegacyUserEntity.legacyUserEntity;

import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.storage.legacy.composite.qna.condition.LegacyWebQnaCompositeConditionBuilder;
import com.ryuqq.setof.storage.legacy.composite.qna.dto.LegacyWebMyQnaQueryDto;
import com.ryuqq.setof.storage.legacy.composite.qna.dto.LegacyWebQnaAnswerQueryDto;
import com.ryuqq.setof.storage.legacy.productgroup.entity.LegacyProductGroupImageEntity;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * LegacyWebQnaCompositeQueryDslRepository - 내 Q&A 복합 조회 Repository.
 *
 * <p>PER-REP-002: 모든 조회 로직은 QueryDslRepository에서 처리.
 *
 * <p>PER-REP-004: ConditionBuilder를 사용하여 동적 쿼리 구성.
 *
 * <p>내 Q&A 조회 시 상품 정보(상품그룹, 브랜드, 이미지) JOIN이 필요한 복합 조회를 처리합니다. Adapter에서 criteria의 primitive 값을 직접
 * 전달받습니다.
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
     * 내 Q&A 커서 페이징 ID 목록 조회.
     *
     * @param userId 사용자 ID
     * @param cursor 커서 (마지막 Q&A ID, nullable)
     * @param qnaType Q&A 유형 (nullable)
     * @param fetchSize pageSize + 1 (hasNext 판단용)
     * @return Q&A ID 목록
     */
    public List<Long> fetchMyQnaIds(
            Long userId,
            Long cursor,
            String qnaType,
            LocalDateTime startDate,
            LocalDateTime endDate,
            int fetchSize) {
        return queryFactory
                .select(legacyQnaEntity.id)
                .from(legacyQnaEntity)
                .join(legacySellerEntity)
                .on(legacySellerEntity.id.eq(legacyQnaEntity.sellerId))
                .leftJoin(legacyUserEntity)
                .on(legacyUserEntity.id.eq(legacyQnaEntity.userId))
                .where(
                        conditionBuilder.userIdEq(userId),
                        conditionBuilder.cursorLessThan(cursor),
                        conditionBuilder.qnaTypeEq(qnaType),
                        conditionBuilder.insertDateBetween(startDate, endDate),
                        conditionBuilder.notDeleted())
                .orderBy(legacyQnaEntity.id.desc())
                .limit(fetchSize)
                .fetch();
    }

    /**
     * 내 상품 Q&A 상세 목록 조회 (GroupBy + 답변 + 상품 정보 포함).
     *
     * @param qnaIds Q&A ID 목록
     * @return 내 Q&A 상세 목록 (상품 타겟 + 답변 포함)
     */
    public List<LegacyWebMyQnaQueryDto> fetchMyProductQnasByIds(List<Long> qnaIds) {
        if (qnaIds == null || qnaIds.isEmpty()) {
            return List.of();
        }

        return queryFactory
                .from(legacyQnaEntity)
                .leftJoin(legacyQnaAnswerEntity)
                .on(legacyQnaAnswerEntity.qnaId.eq(legacyQnaEntity.id))
                .join(legacyQnaProductEntity)
                .on(legacyQnaProductEntity.qnaId.eq(legacyQnaEntity.id))
                .join(legacyProductGroupEntity)
                .on(legacyProductGroupEntity.id.eq(legacyQnaProductEntity.productGroupId))
                .join(legacyBrandEntity)
                .on(legacyBrandEntity.id.eq(legacyProductGroupEntity.brandId))
                .join(legacyProductGroupImageEntity)
                .on(
                        legacyProductGroupImageEntity.productGroupId.eq(
                                legacyProductGroupEntity.id),
                        legacyProductGroupImageEntity.productGroupImageType.eq(
                                LegacyProductGroupImageEntity.ProductGroupImageType.MAIN),
                        legacyProductGroupImageEntity.deleteYn.eq(
                                LegacyProductGroupImageEntity.Yn.N))
                .join(legacySellerEntity)
                .on(legacySellerEntity.id.eq(legacyQnaEntity.sellerId))
                .leftJoin(legacyUserEntity)
                .on(legacyUserEntity.id.eq(legacyQnaEntity.userId))
                .where(conditionBuilder.qnaIdIn(qnaIds), conditionBuilder.notDeleted())
                .orderBy(legacyQnaEntity.id.desc())
                .transform(
                        GroupBy.groupBy(legacyQnaEntity.id)
                                .list(
                                        Projections.constructor(
                                                LegacyWebMyQnaQueryDto.class,
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
                                                legacyProductGroupEntity.id,
                                                legacyProductGroupEntity.productGroupName,
                                                legacyProductGroupImageEntity.imageUrl,
                                                legacyBrandEntity.id,
                                                legacyBrandEntity.brandName,
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
}
