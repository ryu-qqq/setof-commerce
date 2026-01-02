package com.ryuqq.setof.application.board.port.out.query;

import com.ryuqq.setof.domain.board.aggregate.Board;
import com.ryuqq.setof.domain.board.query.BoardSearchCriteria;
import com.ryuqq.setof.domain.board.vo.BoardId;
import java.util.List;
import java.util.Optional;

/**
 * Board 조회 Outbound Port (Query)
 *
 * @author development-team
 * @since 1.0.0
 */
public interface BoardQueryPort {

    /**
     * 게시물 ID로 조회
     *
     * @param boardId 게시물 ID
     * @return 게시물 Optional
     */
    Optional<Board> findById(BoardId boardId);

    /**
     * 게시물 조건 조회
     *
     * @param criteria 검색 조건
     * @return 게시물 목록
     */
    List<Board> findByCriteria(BoardSearchCriteria criteria);

    /**
     * 게시물 조건 개수 조회
     *
     * @param criteria 검색 조건
     * @return 게시물 개수
     */
    long countByCriteria(BoardSearchCriteria criteria);

    /**
     * 게시물 존재 여부 확인
     *
     * @param boardId 게시물 ID
     * @return 존재 여부
     */
    boolean existsById(BoardId boardId);
}
