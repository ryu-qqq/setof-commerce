package com.ryuqq.setof.application.board.service.command;

import com.ryuqq.setof.application.board.dto.command.UnpinBoardCommand;
import com.ryuqq.setof.application.board.manager.command.BoardPersistenceManager;
import com.ryuqq.setof.application.board.manager.query.BoardReadManager;
import com.ryuqq.setof.application.board.port.in.command.UnpinBoardUseCase;
import com.ryuqq.setof.domain.board.aggregate.Board;
import com.ryuqq.setof.domain.board.vo.BoardId;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import org.springframework.stereotype.Service;

/**
 * Board 상단 고정 해제 Service
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class UnpinBoardService implements UnpinBoardUseCase {

    private final BoardReadManager boardReadManager;
    private final BoardPersistenceManager boardPersistenceManager;
    private final ClockHolder clockHolder;

    public UnpinBoardService(
            BoardReadManager boardReadManager,
            BoardPersistenceManager boardPersistenceManager,
            ClockHolder clockHolder) {
        this.boardReadManager = boardReadManager;
        this.boardPersistenceManager = boardPersistenceManager;
        this.clockHolder = clockHolder;
    }

    @Override
    public void execute(UnpinBoardCommand command) {
        BoardId boardId = BoardId.of(command.boardId());
        Board existing = boardReadManager.findById(boardId);
        Board unpinned = existing.unpin(command.updatedBy(), clockHolder.getClock().instant());
        boardPersistenceManager.persist(unpinned);
    }
}
