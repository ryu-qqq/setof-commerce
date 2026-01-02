package com.ryuqq.setof.application.gnb.port.in.command;

import com.ryuqq.setof.application.gnb.dto.command.CreateGnbCommand;

/**
 * Gnb 생성 UseCase (Command)
 *
 * @author development-team
 * @since 1.0.0
 */
public interface CreateGnbUseCase {

    /**
     * GNB 생성
     *
     * @param command 생성 커맨드
     * @return 생성된 GNB ID
     */
    Long execute(CreateGnbCommand command);
}
