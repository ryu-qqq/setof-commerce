package com.ryuqq.setof.application.board.port.in.command;

import com.ryuqq.setof.application.board.dto.command.UnpinBoardCommand;

/**
 * Board 상단 고정 해제 UseCase (Command)
 *
 * @author development-team
 * @since 1.0.0
 */
public interface UnpinBoardUseCase {

    /**
     * 게시글 상단 고정 해제
     *
     * @param command 상단 고정 해제 커맨드
     */
    void execute(UnpinBoardCommand command);
}
