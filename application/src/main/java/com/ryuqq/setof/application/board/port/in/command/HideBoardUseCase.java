package com.ryuqq.setof.application.board.port.in.command;

import com.ryuqq.setof.application.board.dto.command.HideBoardCommand;

/**
 * Board 숨김 UseCase (Command)
 *
 * @author development-team
 * @since 1.0.0
 */
public interface HideBoardUseCase {

    /**
     * 게시글 숨김 (PUBLISHED → HIDDEN)
     *
     * @param command 숨김 커맨드
     */
    void execute(HideBoardCommand command);
}
