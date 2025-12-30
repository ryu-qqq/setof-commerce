package com.ryuqq.setof.application.board.service.command;

import com.ryuqq.setof.application.board.dto.command.DeleteBoardCommand;
import com.ryuqq.setof.application.board.manager.command.BoardPersistenceManager;
import com.ryuqq.setof.application.board.manager.query.BoardReadManager;
import com.ryuqq.setof.application.board.port.in.command.DeleteBoardUseCase;
import com.ryuqq.setof.domain.board.aggregate.Board;
import com.ryuqq.setof.domain.board.vo.BoardId;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import org.springframework.stereotype.Service;

/**
 * Board 삭제 Service
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class DeleteBoardService implements DeleteBoardUseCase {

    private final BoardReadManager boardReadManager;
    private final BoardPersistenceManager boardPersistenceManager;
    private final ClockHolder clockHolder;

    public DeleteBoardService(
            BoardReadManager boardReadManager,
            BoardPersistenceManager boardPersistenceManager,
            ClockHolder clockHolder) {
        this.boardReadManager = boardReadManager;
        this.boardPersistenceManager = boardPersistenceManager;
        this.clockHolder = clockHolder;
    }

    @Override
    public void execute(DeleteBoardCommand command) {
        BoardId boardId = BoardId.of(command.boardId());
        Board existing = boardReadManager.findById(boardId);
        Board deleted = existing.softDelete(command.deletedBy(), clockHolder.getClock().instant());
        boardPersistenceManager.persist(deleted);
    }
}
