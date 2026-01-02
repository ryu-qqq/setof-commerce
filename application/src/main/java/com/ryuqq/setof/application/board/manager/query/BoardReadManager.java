package com.ryuqq.setof.application.board.manager.query;

import com.ryuqq.setof.application.board.port.out.query.BoardQueryPort;
import com.ryuqq.setof.domain.board.aggregate.Board;
import com.ryuqq.setof.domain.board.exception.BoardNotFoundException;
import com.ryuqq.setof.domain.board.query.BoardSearchCriteria;
import com.ryuqq.setof.domain.board.vo.BoardId;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Board 조회 Manager (Query)
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
@Transactional(readOnly = true)
public class BoardReadManager {

    private final BoardQueryPort boardQueryPort;

    public BoardReadManager(BoardQueryPort boardQueryPort) {
        this.boardQueryPort = boardQueryPort;
    }

    /**
     * 게시글 ID로 조회
     *
     * @param boardId 게시글 ID
     * @return 게시글
     * @throws BoardNotFoundException 게시글이 존재하지 않는 경우
     */
    public Board findById(BoardId boardId) {
        return boardQueryPort
                .findById(boardId)
                .orElseThrow(() -> new BoardNotFoundException(boardId));
    }

    /**
     * 게시글 조건 조회
     *
     * @param criteria 검색 조건
     * @return 게시글 목록
     */
    public List<Board> findByCriteria(BoardSearchCriteria criteria) {
        return boardQueryPort.findByCriteria(criteria);
    }

    /**
     * 게시글 조건 개수 조회
     *
     * @param criteria 검색 조건
     * @return 게시글 개수
     */
    public long countByCriteria(BoardSearchCriteria criteria) {
        return boardQueryPort.countByCriteria(criteria);
    }
}
