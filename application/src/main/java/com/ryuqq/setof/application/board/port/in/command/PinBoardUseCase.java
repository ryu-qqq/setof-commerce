package com.ryuqq.setof.application.board.port.in.command;

import com.ryuqq.setof.application.board.dto.command.PinBoardCommand;

/**
 * Board 상단 고정 UseCase (Command)
 *
 * @author development-team
 * @since 1.0.0
 */
public interface PinBoardUseCase {

    /**
     * 게시글 상단 고정
     *
     * @param command 상단 고정 커맨드
     */
    void execute(PinBoardCommand command);
}
