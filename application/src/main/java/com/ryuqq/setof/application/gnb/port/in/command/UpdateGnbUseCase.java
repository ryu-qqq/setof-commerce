package com.ryuqq.setof.application.gnb.port.in.command;

import com.ryuqq.setof.application.gnb.dto.command.UpdateGnbCommand;

/**
 * Gnb 수정 UseCase (Command)
 *
 * @author development-team
 * @since 1.0.0
 */
public interface UpdateGnbUseCase {

    /**
     * GNB 수정
     *
     * @param command 수정 커맨드
     */
    void execute(UpdateGnbCommand command);
}
