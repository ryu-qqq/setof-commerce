package com.ryuqq.setof.application.board.manager.command;

import com.ryuqq.setof.application.board.port.out.command.BoardPersistencePort;
import com.ryuqq.setof.domain.board.aggregate.Board;
import com.ryuqq.setof.domain.board.vo.BoardId;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Board 영속성 Manager (Command)
 *
 * <p>저장/수정은 persist()로 통합 (JPA merge 활용)
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
@Transactional
public class BoardPersistenceManager {

    private final BoardPersistencePort boardPersistencePort;

    public BoardPersistenceManager(BoardPersistencePort boardPersistencePort) {
        this.boardPersistencePort = boardPersistencePort;
    }

    /**
     * 게시글 저장/수정 (JPA merge 활용)
     *
     * <p>ID가 없으면 INSERT, 있으면 UPDATE
     *
     * @param board 저장/수정할 게시글
     * @return 저장된 Board ID
     */
    public BoardId persist(Board board) {
        return boardPersistencePort.persist(board);
    }
}
