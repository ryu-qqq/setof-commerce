package com.ryuqq.setof.application.faq.port.in.command;

import com.ryuqq.setof.application.faq.dto.command.HideFaqCommand;

/**
 * FAQ 숨김 UseCase
 *
 * @author development-team
 * @since 1.0.0
 */
public interface HideFaqUseCase {

    /**
     * FAQ 숨김 처리
     *
     * @param command 숨김 커맨드
     */
    void execute(HideFaqCommand command);
}
