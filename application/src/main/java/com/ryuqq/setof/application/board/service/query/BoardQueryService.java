package com.ryuqq.setof.application.board.service.query;

import com.ryuqq.setof.application.board.assembler.BoardAssembler;
import com.ryuqq.setof.application.board.dto.query.SearchBoardQuery;
import com.ryuqq.setof.application.board.dto.response.BoardResponse;
import com.ryuqq.setof.application.board.factory.query.BoardQueryFactory;
import com.ryuqq.setof.application.board.manager.query.BoardReadManager;
import com.ryuqq.setof.application.board.port.in.query.GetBoardUseCase;
import com.ryuqq.setof.application.board.port.in.query.SearchBoardUseCase;
import com.ryuqq.setof.domain.board.aggregate.Board;
import com.ryuqq.setof.domain.board.query.BoardSearchCriteria;
import com.ryuqq.setof.domain.board.vo.BoardId;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Board 조회 Service
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class BoardQueryService implements GetBoardUseCase, SearchBoardUseCase {

    private final BoardReadManager boardReadManager;
    private final BoardQueryFactory boardQueryFactory;
    private final BoardAssembler boardAssembler;

    public BoardQueryService(
            BoardReadManager boardReadManager,
            BoardQueryFactory boardQueryFactory,
            BoardAssembler boardAssembler) {
        this.boardReadManager = boardReadManager;
        this.boardQueryFactory = boardQueryFactory;
        this.boardAssembler = boardAssembler;
    }

    @Override
    public BoardResponse execute(BoardId boardId) {
        Board board = boardReadManager.findById(boardId);
        return boardAssembler.toResponse(board);
    }

    @Override
    public List<BoardResponse> execute(SearchBoardQuery query) {
        BoardSearchCriteria criteria = boardQueryFactory.createSearchCriteria(query);
        List<Board> boards = boardReadManager.findByCriteria(criteria);
        return boardAssembler.toResponses(boards);
    }

    @Override
    public long count(SearchBoardQuery query) {
        BoardSearchCriteria criteria = boardQueryFactory.createSearchCriteria(query);
        return boardReadManager.countByCriteria(criteria);
    }
}
