package com.ryuqq.setof.application.faq.port.in.command;

import com.ryuqq.setof.application.faq.dto.command.DeleteFaqCommand;

/**
 * FAQ 삭제 UseCase
 *
 * @author development-team
 * @since 1.0.0
 */
public interface DeleteFaqUseCase {

    /**
     * FAQ 삭제 (Soft Delete)
     *
     * @param command 삭제 커맨드
     */
    void execute(DeleteFaqCommand command);
}
