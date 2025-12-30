package com.ryuqq.setof.application.qna.port.in.command;

import com.ryuqq.setof.application.qna.dto.command.CloseQnaCommand;

/**
 * Close QnA UseCase (Command)
 *
 * <p>문의 종료를 담당하는 Inbound Port
 *
 * @author development-team
 * @since 1.0.0
 */
public interface CloseQnaUseCase {

    /**
     * 문의 종료 실행
     *
     * @param command 문의 종료 명령
     */
    void execute(CloseQnaCommand command);
}
