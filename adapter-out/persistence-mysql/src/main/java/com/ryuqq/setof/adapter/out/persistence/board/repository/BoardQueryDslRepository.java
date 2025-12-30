package com.ryuqq.setof.adapter.out.persistence.board.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.adapter.out.persistence.board.entity.BoardJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.board.entity.QBoardJpaEntity;
import com.ryuqq.setof.domain.board.query.BoardSearchCriteria;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * BoardQueryDslRepository - Board QueryDSL Repository
 *
 * <p>QueryDSL 기반 조회 쿼리를 처리하는 전용 Repository입니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Repository
public class BoardQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private static final QBoardJpaEntity qBoard = QBoardJpaEntity.boardJpaEntity;

    public BoardQueryDslRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    /** ID로 Board 단건 조회 */
    public Optional<BoardJpaEntity> findById(Long id) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(qBoard)
                        .where(qBoard.id.eq(id), qBoard.deletedAt.isNull())
                        .fetchOne());
    }

    /** 검색 조건으로 Board 목록 조회 */
    public List<BoardJpaEntity> findByCriteria(BoardSearchCriteria criteria) {
        return queryFactory
                .selectFrom(qBoard)
                .where(
                        boardTypeEquals(
                                criteria.boardType() != null ? criteria.boardType().name() : null),
                        statusEquals(criteria.status() != null ? criteria.status().name() : null),
                        displayableAt(criteria.displayableAt()),
                        pinnedEquals(criteria.pinned()),
                        qBoard.deletedAt.isNull())
                .orderBy(qBoard.pinned.desc(), qBoard.pinOrder.asc(), qBoard.createdAt.desc())
                .offset(criteria.offset())
                .limit(criteria.limit())
                .fetch();
    }

    /** 검색 조건으로 Board 개수 조회 */
    public long countByCriteria(BoardSearchCriteria criteria) {
        Long count =
                queryFactory
                        .select(qBoard.count())
                        .from(qBoard)
                        .where(
                                boardTypeEquals(
                                        criteria.boardType() != null
                                                ? criteria.boardType().name()
                                                : null),
                                statusEquals(
                                        criteria.status() != null
                                                ? criteria.status().name()
                                                : null),
                                displayableAt(criteria.displayableAt()),
                                pinnedEquals(criteria.pinned()),
                                qBoard.deletedAt.isNull())
                        .fetchOne();
        return count != null ? count : 0L;
    }

    /** ID로 존재 여부 확인 */
    public boolean existsById(Long id) {
        Integer count =
                queryFactory
                        .selectOne()
                        .from(qBoard)
                        .where(qBoard.id.eq(id), qBoard.deletedAt.isNull())
                        .fetchFirst();
        return count != null;
    }

    // ===== Dynamic Conditions =====

    private BooleanExpression boardTypeEquals(String boardType) {
        if (boardType == null) {
            return null;
        }
        return qBoard.boardType.eq(boardType);
    }

    private BooleanExpression statusEquals(String status) {
        if (status == null) {
            return null;
        }
        return qBoard.status.eq(status);
    }

    private BooleanExpression displayableAt(Instant displayableAt) {
        if (displayableAt == null) {
            return null;
        }
        return qBoard.displayStartAt
                .loe(displayableAt)
                .and(qBoard.displayEndAt.goe(displayableAt))
                .and(qBoard.status.eq("PUBLISHED"));
    }

    private BooleanExpression pinnedEquals(Boolean pinned) {
        if (pinned == null) {
            return null;
        }
        return qBoard.pinned.eq(pinned);
    }
}
