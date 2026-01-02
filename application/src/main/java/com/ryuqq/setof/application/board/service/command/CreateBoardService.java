package com.ryuqq.setof.application.board.service.command;

import com.ryuqq.setof.application.board.dto.command.CreateBoardCommand;
import com.ryuqq.setof.application.board.factory.command.BoardCommandFactory;
import com.ryuqq.setof.application.board.manager.command.BoardPersistenceManager;
import com.ryuqq.setof.application.board.port.in.command.CreateBoardUseCase;
import com.ryuqq.setof.domain.board.aggregate.Board;
import org.springframework.stereotype.Service;

/**
 * Board 생성 Service
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class CreateBoardService implements CreateBoardUseCase {

    private final BoardCommandFactory boardCommandFactory;
    private final BoardPersistenceManager boardPersistenceManager;

    public CreateBoardService(
            BoardCommandFactory boardCommandFactory,
            BoardPersistenceManager boardPersistenceManager) {
        this.boardCommandFactory = boardCommandFactory;
        this.boardPersistenceManager = boardPersistenceManager;
    }

    @Override
    public Long execute(CreateBoardCommand command) {
        Board board = boardCommandFactory.createBoard(command);
        return boardPersistenceManager.persist(board).value();
    }
}
