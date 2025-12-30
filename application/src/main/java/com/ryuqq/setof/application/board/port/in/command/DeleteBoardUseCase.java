package com.ryuqq.setof.application.board.port.in.command;

import com.ryuqq.setof.application.board.dto.command.DeleteBoardCommand;

/**
 * Board 삭제 UseCase (Command)
 *
 * @author development-team
 * @since 1.0.0
 */
public interface DeleteBoardUseCase {

    /**
     * 게시글 삭제 (Soft Delete)
     *
     * @param command 삭제 커맨드
     */
    void execute(DeleteBoardCommand command);
}
