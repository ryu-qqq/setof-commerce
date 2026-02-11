package com.ryuqq.setof.storage.legacy.board.repository;

import static com.ryuqq.setof.storage.legacy.board.entity.QLegacyBoardEntity.legacyBoardEntity;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.domain.board.query.BoardSearchCriteria;
import com.ryuqq.setof.domain.board.query.BoardSortKey;
import com.ryuqq.setof.domain.common.vo.QueryContext;
import com.ryuqq.setof.domain.common.vo.SortDirection;
import com.ryuqq.setof.storage.legacy.board.condition.LegacyBoardConditionBuilder;
import com.ryuqq.setof.storage.legacy.board.entity.LegacyBoardEntity;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * LegacyBoardQueryDslRepository - 레거시 게시판 QueryDSL 레포지토리.
 *
 * <p>PER-REP-002: 모든 조회 로직은 QueryDslRepository에서 처리.
 *
 * <p>PER-REP-004: ConditionBuilder를 사용하여 동적 쿼리 구성.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Repository
public class LegacyBoardQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private final LegacyBoardConditionBuilder conditionBuilder;

    public LegacyBoardQueryDslRepository(
            JPAQueryFactory queryFactory, LegacyBoardConditionBuilder conditionBuilder) {
        this.queryFactory = queryFactory;
        this.conditionBuilder = conditionBuilder;
    }

    /**
     * 검색 조건으로 게시판 목록 조회.
     *
     * @param criteria 검색 조건
     * @return 게시판 목록
     */
    public List<LegacyBoardEntity> findByCriteria(BoardSearchCriteria criteria) {
        QueryContext<BoardSortKey> qc = criteria.queryContext();

        return queryFactory
                .selectFrom(legacyBoardEntity)
                .orderBy(createOrderSpecifier(qc.sortKey(), qc.sortDirection()))
                .offset(criteria.offset())
                .limit(criteria.size())
                .fetch();
    }

    /**
     * 검색 조건으로 게시판 개수 조회.
     *
     * @param criteria 검색 조건
     * @return 게시판 개수
     */
    public long countByCriteria(BoardSearchCriteria criteria) {
        Long count =
                queryFactory.select(legacyBoardEntity.count()).from(legacyBoardEntity).fetchOne();
        return count != null ? count : 0L;
    }

    private OrderSpecifier<?> createOrderSpecifier(BoardSortKey sortKey, SortDirection direction) {
        boolean isAsc = direction == SortDirection.ASC;

        return switch (sortKey) {
            case CREATED_AT ->
                    isAsc
                            ? legacyBoardEntity.insertDate.asc()
                            : legacyBoardEntity.insertDate.desc();
            case BOARD_ID -> isAsc ? legacyBoardEntity.id.asc() : legacyBoardEntity.id.desc();
        };
    }
}
