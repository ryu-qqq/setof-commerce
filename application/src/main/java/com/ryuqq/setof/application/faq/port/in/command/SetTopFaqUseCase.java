package com.ryuqq.setof.application.faq.port.in.command;

import com.ryuqq.setof.application.faq.dto.command.SetTopFaqCommand;

/**
 * FAQ 상단 노출 설정 UseCase
 *
 * @author development-team
 * @since 1.0.0
 */
public interface SetTopFaqUseCase {

    /**
     * FAQ 상단 노출 설정
     *
     * @param command 상단 노출 설정 커맨드
     */
    void execute(SetTopFaqCommand command);
}
