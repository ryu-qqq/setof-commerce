package com.ryuqq.setof.storage.legacy.composite.web.board.repository;

import static com.ryuqq.setof.storage.legacy.board.entity.QLegacyBoardEntity.legacyBoardEntity;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.domain.legacy.board.dto.query.LegacyBoardSearchCondition;
import com.ryuqq.setof.storage.legacy.composite.web.board.dto.LegacyWebBoardQueryDto;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * LegacyWebBoardCompositeQueryDslRepository - 레거시 게시판 Composite 조회 Repository.
 *
 * <p>PER-REP-002: 모든 조회 로직은 QueryDslRepository에서 처리.
 *
 * <p>Projections.constructor() 사용 (@QueryProjection 금지).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Repository
public class LegacyWebBoardCompositeQueryDslRepository {

    private final JPAQueryFactory queryFactory;

    public LegacyWebBoardCompositeQueryDslRepository(
            @Qualifier("legacyJpaQueryFactory") JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    /**
     * 게시글 목록 조회 (페이징).
     *
     * @param condition 검색 조건 (페이지 정보 포함)
     * @return 게시글 목록
     */
    public List<LegacyWebBoardQueryDto> fetchBoards(LegacyBoardSearchCondition condition) {
        return queryFactory
                .select(
                        Projections.constructor(
                                LegacyWebBoardQueryDto.class,
                                legacyBoardEntity.title,
                                legacyBoardEntity.contents))
                .from(legacyBoardEntity)
                .offset(condition.offset())
                .limit(condition.pageSize())
                .fetch();
    }

    /**
     * 게시글 전체 개수 조회.
     *
     * @return 전체 게시글 개수
     */
    public long countBoards() {
        Long count =
                queryFactory.select(legacyBoardEntity.count()).from(legacyBoardEntity).fetchOne();
        return count != null ? count : 0L;
    }
}
