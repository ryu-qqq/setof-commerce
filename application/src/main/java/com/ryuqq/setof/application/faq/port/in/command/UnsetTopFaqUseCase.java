package com.ryuqq.setof.application.faq.port.in.command;

import com.ryuqq.setof.application.faq.dto.command.UnsetTopFaqCommand;

/**
 * FAQ 상단 노출 해제 UseCase
 *
 * @author development-team
 * @since 1.0.0
 */
public interface UnsetTopFaqUseCase {

    /**
     * FAQ 상단 노출 해제
     *
     * @param command 상단 노출 해제 커맨드
     */
    void execute(UnsetTopFaqCommand command);
}
