package com.ryuqq.setof.application.faq.port.in.command;

import com.ryuqq.setof.application.faq.dto.command.CreateFaqCommand;

/**
 * FAQ 생성 UseCase
 *
 * @author development-team
 * @since 1.0.0
 */
public interface CreateFaqUseCase {

    /**
     * FAQ 생성
     *
     * @param command 생성 커맨드
     * @return 생성된 FAQ ID
     */
    Long execute(CreateFaqCommand command);
}
