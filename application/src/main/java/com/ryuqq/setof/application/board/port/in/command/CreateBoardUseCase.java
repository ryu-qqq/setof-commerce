package com.ryuqq.setof.application.board.port.in.command;

import com.ryuqq.setof.application.board.dto.command.CreateBoardCommand;

/**
 * Board 생성 UseCase (Command)
 *
 * @author development-team
 * @since 1.0.0
 */
public interface CreateBoardUseCase {

    /**
     * 게시글 생성
     *
     * @param command 생성 커맨드
     * @return 생성된 Board ID
     */
    Long execute(CreateBoardCommand command);
}
