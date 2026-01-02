package com.ryuqq.setof.application.board.port.in.command;

import com.ryuqq.setof.application.board.dto.command.PublishBoardCommand;

/**
 * Board 발행 UseCase (Command)
 *
 * @author development-team
 * @since 1.0.0
 */
public interface PublishBoardUseCase {

    /**
     * 게시글 발행 (DRAFT → PUBLISHED)
     *
     * @param command 발행 커맨드
     */
    void execute(PublishBoardCommand command);
}
