package com.ryuqq.setof.application.faq.port.in.command;

import com.ryuqq.setof.application.faq.dto.command.PublishFaqCommand;

/**
 * FAQ 게시 UseCase
 *
 * @author development-team
 * @since 1.0.0
 */
public interface PublishFaqUseCase {

    /**
     * FAQ 게시
     *
     * @param command 게시 커맨드
     */
    void execute(PublishFaqCommand command);
}
