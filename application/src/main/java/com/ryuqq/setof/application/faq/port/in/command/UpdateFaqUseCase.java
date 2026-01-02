package com.ryuqq.setof.application.faq.port.in.command;

import com.ryuqq.setof.application.faq.dto.command.UpdateFaqCommand;

/**
 * FAQ 수정 UseCase
 *
 * @author development-team
 * @since 1.0.0
 */
public interface UpdateFaqUseCase {

    /**
     * FAQ 수정
     *
     * @param command 수정 커맨드
     */
    void execute(UpdateFaqCommand command);
}
