package com.ryuqq.setof.application.board.port.in.query;

import com.ryuqq.setof.application.board.dto.query.SearchBoardQuery;
import com.ryuqq.setof.application.board.dto.response.BoardResponse;
import java.util.List;

/**
 * Board 검색 UseCase (Query)
 *
 * @author development-team
 * @since 1.0.0
 */
public interface SearchBoardUseCase {

    /**
     * 게시글 검색
     *
     * @param query 검색 쿼리
     * @return 게시글 목록
     */
    List<BoardResponse> execute(SearchBoardQuery query);

    /**
     * 게시글 검색 개수
     *
     * @param query 검색 쿼리
     * @return 게시글 개수
     */
    long count(SearchBoardQuery query);
}
