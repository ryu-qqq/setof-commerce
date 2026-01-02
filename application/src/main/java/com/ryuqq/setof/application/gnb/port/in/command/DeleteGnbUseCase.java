package com.ryuqq.setof.application.gnb.port.in.command;

import com.ryuqq.setof.application.gnb.dto.command.DeleteGnbCommand;

/**
 * Gnb 삭제 UseCase (Command)
 *
 * @author development-team
 * @since 1.0.0
 */
public interface DeleteGnbUseCase {

    /**
     * GNB 삭제
     *
     * @param command 삭제 커맨드
     */
    void execute(DeleteGnbCommand command);
}
