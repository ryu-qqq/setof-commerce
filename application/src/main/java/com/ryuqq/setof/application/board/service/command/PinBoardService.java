package com.ryuqq.setof.application.board.service.command;

import com.ryuqq.setof.application.board.dto.command.PinBoardCommand;
import com.ryuqq.setof.application.board.manager.command.BoardPersistenceManager;
import com.ryuqq.setof.application.board.manager.query.BoardReadManager;
import com.ryuqq.setof.application.board.port.in.command.PinBoardUseCase;
import com.ryuqq.setof.domain.board.aggregate.Board;
import com.ryuqq.setof.domain.board.vo.BoardId;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import org.springframework.stereotype.Service;

/**
 * Board 상단 고정 Service
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class PinBoardService implements PinBoardUseCase {

    private final BoardReadManager boardReadManager;
    private final BoardPersistenceManager boardPersistenceManager;
    private final ClockHolder clockHolder;

    public PinBoardService(
            BoardReadManager boardReadManager,
            BoardPersistenceManager boardPersistenceManager,
            ClockHolder clockHolder) {
        this.boardReadManager = boardReadManager;
        this.boardPersistenceManager = boardPersistenceManager;
        this.clockHolder = clockHolder;
    }

    @Override
    public void execute(PinBoardCommand command) {
        BoardId boardId = BoardId.of(command.boardId());
        Board existing = boardReadManager.findById(boardId);
        Board pinned =
                existing.pin(
                        command.pinOrder(), command.updatedBy(), clockHolder.getClock().instant());
        boardPersistenceManager.persist(pinned);
    }
}
