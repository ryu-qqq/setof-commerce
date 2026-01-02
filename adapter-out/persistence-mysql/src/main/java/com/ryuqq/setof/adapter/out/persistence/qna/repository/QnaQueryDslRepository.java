package com.ryuqq.setof.adapter.out.persistence.qna.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.adapter.out.persistence.qna.entity.QQnaImageJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.qna.entity.QQnaJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.qna.entity.QnaImageJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.qna.entity.QnaJpaEntity;
import com.ryuqq.setof.domain.common.vo.SortDirection;
import com.ryuqq.setof.domain.qna.query.criteria.QnaSearchCriteria;
import com.ryuqq.setof.domain.qna.vo.QnaSortBy;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * QnaQueryDslRepository - QnA QueryDSL Repository
 *
 * <p>QueryDSL 기반 조회 쿼리를 처리하는 전용 Repository입니다.
 *
 * <p><strong>표준 메서드:</strong>
 *
 * <ul>
 *   <li>findById(Long id): ID로 QnA 단건 조회
 *   <li>findByCriteria(QnaSearchCriteria): 조건으로 QnA 목록 조회
 *   <li>countByCriteria(QnaSearchCriteria): 조건으로 QnA 총 개수 조회
 *   <li>existsById(Long id): 존재 여부 확인
 *   <li>findImagesByQnaId(Long qnaId): QnA에 속한 이미지 조회
 * </ul>
 *
 * <p><strong>금지 사항:</strong>
 *
 * <ul>
 *   <li>Join 절대 금지
 *   <li>비즈니스 로직 금지
 *   <li>Mapper 호출 금지
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Repository
public class QnaQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private static final QQnaJpaEntity qQna = QQnaJpaEntity.qnaJpaEntity;
    private static final QQnaImageJpaEntity qQnaImage = QQnaImageJpaEntity.qnaImageJpaEntity;

    public QnaQueryDslRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    /**
     * ID로 QnA 단건 조회
     *
     * @param id QnA ID
     * @return QnaJpaEntity (Optional)
     */
    public Optional<QnaJpaEntity> findById(Long id) {
        return Optional.ofNullable(
                queryFactory.selectFrom(qQna).where(qQna.id.eq(id), notDeleted()).fetchOne());
    }

    /**
     * ID로 QnA 단건 조회 (삭제 포함)
     *
     * @param id QnA ID
     * @return QnaJpaEntity (Optional)
     */
    public Optional<QnaJpaEntity> findByIdIncludeDeleted(Long id) {
        return Optional.ofNullable(queryFactory.selectFrom(qQna).where(qQna.id.eq(id)).fetchOne());
    }

    /**
     * 검색 조건으로 QnA 목록 조회
     *
     * @param criteria 검색 조건
     * @return QnaJpaEntity 목록
     */
    public List<QnaJpaEntity> findByCriteria(QnaSearchCriteria criteria) {
        BooleanBuilder condition = buildCondition(criteria);

        return queryFactory
                .selectFrom(qQna)
                .where(condition, notDeleted())
                .orderBy(getOrderSpecifier(criteria.sortBy(), criteria.sortDirection()))
                .offset(criteria.offset())
                .limit(criteria.pageSize())
                .fetch();
    }

    /**
     * 검색 조건으로 총 개수 조회
     *
     * @param criteria 검색 조건
     * @return 총 개수
     */
    public long countByCriteria(QnaSearchCriteria criteria) {
        BooleanBuilder condition = buildCondition(criteria);

        Long count =
                queryFactory
                        .select(qQna.count())
                        .from(qQna)
                        .where(condition, notDeleted())
                        .fetchOne();

        return count != null ? count : 0L;
    }

    /**
     * ID로 QnA 존재 여부 확인
     *
     * @param id QnA ID
     * @return 존재 여부
     */
    public boolean existsById(Long id) {
        Integer count =
                queryFactory
                        .selectOne()
                        .from(qQna)
                        .where(qQna.id.eq(id), notDeleted())
                        .fetchFirst();
        return count != null;
    }

    /**
     * QnA ID로 이미지 목록 조회
     *
     * @param qnaId QnA ID
     * @return QnaImageJpaEntity 목록
     */
    public List<QnaImageJpaEntity> findImagesByQnaId(Long qnaId) {
        return queryFactory
                .selectFrom(qQnaImage)
                .where(qQnaImage.qnaId.eq(qnaId))
                .orderBy(qQnaImage.displayOrder.asc())
                .fetch();
    }

    /**
     * 다건 QnA ID로 이미지 목록 조회
     *
     * @param qnaIds QnA ID 목록
     * @return QnaImageJpaEntity 목록
     */
    public List<QnaImageJpaEntity> findImagesByQnaIds(List<Long> qnaIds) {
        if (qnaIds == null || qnaIds.isEmpty()) {
            return List.of();
        }
        return queryFactory
                .selectFrom(qQnaImage)
                .where(qQnaImage.qnaId.in(qnaIds))
                .orderBy(qQnaImage.qnaId.asc(), qQnaImage.displayOrder.asc())
                .fetch();
    }

    /**
     * 검색 조건 빌드
     *
     * @param criteria 검색 조건
     * @return BooleanBuilder
     */
    private BooleanBuilder buildCondition(QnaSearchCriteria criteria) {
        BooleanBuilder builder = new BooleanBuilder();

        if (criteria.hasQnaType()) {
            builder.and(qQna.qnaType.eq(criteria.qnaType().name()));
        }

        if (criteria.hasTargetId()) {
            builder.and(qQna.targetId.eq(criteria.targetId()));
        }

        if (criteria.hasStatus()) {
            builder.and(qQna.status.eq(criteria.status().name()));
        }

        if (criteria.hasWriterName()) {
            builder.and(qQna.writerName.containsIgnoreCase(criteria.writerName()));
        }

        return builder;
    }

    /**
     * 정렬 조건 생성
     *
     * @param sortBy 정렬 기준
     * @param direction 정렬 방향
     * @return OrderSpecifier
     */
    private OrderSpecifier<?> getOrderSpecifier(QnaSortBy sortBy, SortDirection direction) {
        boolean isAsc = direction == SortDirection.ASC;

        return switch (sortBy) {
            case ID -> isAsc ? qQna.id.asc() : qQna.id.desc();
            case CREATED_AT -> isAsc ? qQna.createdAt.asc() : qQna.createdAt.desc();
            case UPDATED_AT -> isAsc ? qQna.updatedAt.asc() : qQna.updatedAt.desc();
        };
    }

    /** 삭제되지 않은 조건 */
    private com.querydsl.core.types.dsl.BooleanExpression notDeleted() {
        return qQna.deletedAt.isNull();
    }
}
