package com.ryuqq.setof.application.board.factory.query;

import com.ryuqq.setof.application.board.dto.query.SearchBoardQuery;
import com.ryuqq.setof.domain.board.query.BoardSearchCriteria;
import com.ryuqq.setof.domain.board.vo.BoardStatus;
import com.ryuqq.setof.domain.board.vo.BoardType;
import org.springframework.stereotype.Component;

/**
 * Board Query Factory
 *
 * <p>Query DTO를 Domain 검색 조건으로 변환합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class BoardQueryFactory {

    /**
     * SearchBoardQuery → BoardSearchCriteria 변환
     *
     * @param query 검색 쿼리
     * @return 검색 조건
     */
    public BoardSearchCriteria createSearchCriteria(SearchBoardQuery query) {
        BoardType boardType =
                query.boardType() != null ? BoardType.valueOf(query.boardType()) : null;
        BoardStatus status = query.status() != null ? BoardStatus.valueOf(query.status()) : null;

        return new BoardSearchCriteria(
                boardType,
                status,
                query.displayableAt(),
                query.pinned(),
                query.offset(),
                query.limit());
    }
}
