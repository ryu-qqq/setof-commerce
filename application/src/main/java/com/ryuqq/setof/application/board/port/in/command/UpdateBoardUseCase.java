package com.ryuqq.setof.application.board.port.in.command;

import com.ryuqq.setof.application.board.dto.command.UpdateBoardCommand;

/**
 * Board 수정 UseCase (Command)
 *
 * @author development-team
 * @since 1.0.0
 */
public interface UpdateBoardUseCase {

    /**
     * 게시글 수정
     *
     * @param command 수정 커맨드
     */
    void execute(UpdateBoardCommand command);
}
