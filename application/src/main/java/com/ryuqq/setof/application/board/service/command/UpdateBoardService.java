package com.ryuqq.setof.application.board.service.command;

import com.ryuqq.setof.application.board.dto.command.UpdateBoardCommand;
import com.ryuqq.setof.application.board.factory.command.BoardCommandFactory;
import com.ryuqq.setof.application.board.manager.command.BoardPersistenceManager;
import com.ryuqq.setof.application.board.manager.query.BoardReadManager;
import com.ryuqq.setof.application.board.port.in.command.UpdateBoardUseCase;
import com.ryuqq.setof.domain.board.aggregate.Board;
import com.ryuqq.setof.domain.board.vo.BoardId;
import org.springframework.stereotype.Service;

/**
 * Board 수정 Service
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class UpdateBoardService implements UpdateBoardUseCase {

    private final BoardReadManager boardReadManager;
    private final BoardCommandFactory boardCommandFactory;
    private final BoardPersistenceManager boardPersistenceManager;

    public UpdateBoardService(
            BoardReadManager boardReadManager,
            BoardCommandFactory boardCommandFactory,
            BoardPersistenceManager boardPersistenceManager) {
        this.boardReadManager = boardReadManager;
        this.boardCommandFactory = boardCommandFactory;
        this.boardPersistenceManager = boardPersistenceManager;
    }

    @Override
    public void execute(UpdateBoardCommand command) {
        BoardId boardId = BoardId.of(command.boardId());
        Board existing = boardReadManager.findById(boardId);
        Board updated = boardCommandFactory.applyUpdate(existing, command);
        boardPersistenceManager.persist(updated);
    }
}
